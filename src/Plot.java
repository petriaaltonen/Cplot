/*
 * Plot.java
 * 5.3.2014
 * Petri Aaltonen
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 *
 * @author Petri Aaltonen
 *
 */
public class Plot {

    // The minimum matrix width and height.
    private final int MIN_MATRIX_XSIZE = 50;
    private final int MIN_MATRIX_YSIZE = 50;

    // An AWT-image
    private BufferedImage plot = null;

    // The plot coordinates
    private PlotCoordinates coordinates = null;

    // The current colouring scheme.
    private Coloring [] coloringList = null;
    private Coloring activeColoring = null;

    // Holds a reference to an evaluator which'll be used to make
    // a plot.
    private Evaluator evaluator = null;

    // A PlotWorker-instance
    private PlotWorker worker = null;

    // A list of callbacks
    private ArrayList<StartCallback> startCallbacks = null;
    private ArrayList<DoneCallback> doneCallbacks = null;
    private ArrayList<ProgressChangedCallback> progressChangedCallbacks = null;

    // Variables used in logging.
    private boolean timeIt = true;
    private File logFile = null;
    private FileWriter logWriter = null;
    private long startTime = 0;
    private String expression = "";

    /**
     * Initialize and provide a reference to an Evaluator instance.
     * @param evaluator an Evaluator instance
     */
    public Plot(Evaluator evaluator) {

        assert evaluator != null : "evaluator is null in Plot constructor";

        coloringList = new Coloring[5];
        coloringList[0] = new CWColoring();
        coloringList[1] = new CWSColoring();
        coloringList[2] = new CWAColoring();
        coloringList[3] = new BRYColoring();
        coloringList[4] = new BRYSColoring();

        activeColoring = coloringList[0];

        startCallbacks = new ArrayList<>(4);
        doneCallbacks = new ArrayList<>(4);
        progressChangedCallbacks = new ArrayList<>(4);

        this.evaluator = evaluator;
        coordinates = new PlotCoordinates(100, 100);
    }

    /**
     * Return a list of the available coloring models.
     * @return a list of strings
     */
    public String [] listColoring() {
        String [] list = new String[coloringList.length];
        for (int i = 0; i < list.length; i++)
            list[i] = new String(coloringList[i].getName());
        return list;
    }

    /**
     * Return the name of the active coloring model.
     * @return a string
     */
    public String getColoring() { return new String(activeColoring.getName()); }

    /**
     * Set the active coloring model.
     * @param coloring name of the coloring model
     * @throws PlotException if 'coloring' does not correspond to a valid coloring model name
     */
    public void setColoring(String coloring) throws PlotException {
        for (int i = 0; i < coloringList.length; i++) {
            if (coloringList[i].getName().equals(coloring)) {
                activeColoring = coloringList[i];
                computeMatrix();
                return;
            }
        }
        throw new PlotException("Invalid coloring model");
    }

    //
    // Resize the matrix.
    //
    public void resize(int width, int height) {
        coordinates.resize(width, height);
        computeMatrix();
    }
    //
    // Reset the x and y limits.
    //
    public void setLimits(double xmin, double xmax, double ymin, double ymax)
            throws PlotException {

        coordinates.setLimits(xmin, xmax, ymin, ymax);
        computeMatrix();
    }

    //
    // Zoom the plot.
    //
    public void zoom(int x1, int y1, int x2, int y2) {
        coordinates.zoom(x1, y1, x2, y2);
        computeMatrix();
    }

    //
    // Scan the plot.
    //
    public void scan(int dx, int dy) {
        coordinates.scan(dx, dy);
    }

    //
    // After scanning the plot recompute the matrix.
    //
    public void postScanUpdate() {
        coordinates.postScanUpdate();
        computeMatrix();
    }

    //
    // Reset the viewport.
    //
    public void resetViewport() {
        coordinates.reset();
    }

    private void startTimer() { startTime = System.nanoTime(); }

    private void stopTimer() {
        double time = (System.nanoTime() - startTime) / 1000000.0;

        String fName = new String("log");
        if (logFile == null) {
            logFile = new File(fName);
            try {
                logWriter = new FileWriter(logFile);
            }
            catch (IOException e) {
                // TODO
            }
        }

        // If expression is uninitialized it means we just created the Plot-instance
        // with no contents yet to plot.
        if (expression == null) return;

        try {
            logWriter.write("Expression: " + expression + "\n");
            logWriter.write("Matrix width: " + coordinates.getMatrixWidth() + "\n");
            logWriter.write("Matrix height: " + coordinates.getMatrixHeight() + "\n");
            logWriter.write("Coloring: " + activeColoring.getName() + "\n");
            logWriter.write("Time (ms): " + time + "\n");
            logWriter.write("\n");
            logWriter.flush();
        }
        catch (IOException e) {
            // TODO
        }
    }

    /**
     * Add a new DoneCallback
     * @param callback a DoneCallback instance
     */
    public void addDoneCallback(DoneCallback callback) {
        assert callback != null;
        doneCallbacks.add(callback);
    }

    /**
     * Add a new StartCallback
     * @param callback a StartCallback instance
     */
    public void addStartCallback(StartCallback callback) {
        assert callback != null;
        startCallbacks.add(callback);
    }

    /**
     * Add a new ProgressChangedCallback.
     * @param callback a ProgressChangedCallback instance
     */
    public void addProgressChangedCallback(ProgressChangedCallback callback) {
        assert callback != null;
        progressChangedCallbacks.add(callback);
    }

    /**
     * This is where the actual computation of a new plot takes place. Note that
     * the computation is performed asynchronously. All registered DoneCallback-instances
     * will be fired when done.
     */
    private void computeMatrix() {

        // If a previous computation is ongoing, cancel it.
        if (worker != null && !worker.isDone() && !worker.isCancelled())
            worker.cancel(true);

        worker = new PlotWorker(evaluator, activeColoring, coordinates);

        worker.addDoneCallback(new DoneCallback() {
            @Override
            public void callback() {
                try {
                    plot = workerRef.get();
                }
                catch (InterruptedException ex) {
                    // TODO: Do something about exceptions
                }
                catch (ExecutionException ex) {
                    // TODO: Do something about exceptions
                }
            }
        });

        for (DoneCallback c : doneCallbacks) worker.addDoneCallback(c);
        for (ProgressChangedCallback c : progressChangedCallbacks) worker.addProgressChangedCallback(c);

        if (timeIt) {
            worker.addDoneCallback(new DoneCallback() {
                @Override
                public void callback() {
                    stopTimer();
                }
            });
            startTimer();
        }

        for (StartCallback c : startCallbacks) c.callback();
        worker.execute();
    }

    public BufferedImage getImage() { return plot; }
    public PlotCoordinates getCoordinates() { return coordinates; }
    public Complex getComplexValue(int x, int y) {
        return evaluator.evalAt(coordinates.getComplexCoordinates(x, y));
    }

}
 