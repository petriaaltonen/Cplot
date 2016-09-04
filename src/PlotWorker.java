import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 * A worker-thread that does the heavy number-crunching job of computing the plot.
 * @author Petri Aaltonen
 */
public class PlotWorker extends SwingWorker<BufferedImage, Void> {

    private Evaluator evaluator;
    private Coloring coloring;
    private PlotCoordinates coordinates;
    private ArrayList<DoneCallback> doneCallbacks;
    private ArrayList<ProgressChangedCallback> progressChangedCallbacks;

    /**
     * Initialize a new worker thread.
     * @param evaluator
     * @param coloring
     * @param coordinates
     */
    public PlotWorker(Evaluator evaluator, Coloring coloring, PlotCoordinates coordinates) {
        super();
        this.evaluator = evaluator;
        this.coloring = coloring;
        this.coordinates = coordinates;
        doneCallbacks = new ArrayList<>(4);
        progressChangedCallbacks = new ArrayList<>(4);
    }

    /**
     * Register a new callback which is called when the worker has finished.
     * @param callback
     */
    public void addDoneCallback(DoneCallback callback) {
        doneCallbacks.add(callback);
    }

    /**
     * Register a new callback which is called when the worker's progress has been updated.
     * @param callback
     */
    public void addProgressChangedCallback(ProgressChangedCallback callback) {
        progressChangedCallbacks.add(callback);
    }

    /**
     * This is where the heavy number crunching is made.
     * @return
     */
    @Override
    public BufferedImage doInBackground() {
        BufferedImage img = new BufferedImage(
                coordinates.getMatrixWidth(),
                coordinates.getMatrixHeight(),
                BufferedImage.TYPE_INT_RGB);

        int n = coordinates.getMatrixWidth() * coordinates.getMatrixHeight();
        int progressed = 0;

        for (int y = 0; y < coordinates.getMatrixHeight(); y++)
            for (int x = 0; x < coordinates.getMatrixWidth(); x++) {
                // The following is a quick-and-dirty fix for a bug!!!
                Complex u = coordinates.matToCmplx(x, y);
                if (u != null) {
                    Complex z = evaluator.evalAt(u);
                    Color rgb = !Double.isNaN(z.x) && !Double.isNaN(z.y)
                            ? coloring.getColor(z)
                            : new Color(255, 255, 255);
                    int c = rgb.getRed() << 16 | rgb.getGreen() << 8 | rgb.getBlue();
                    img.setRGB(x, y, c);
                    ++progressed;
                    if (progressed % 10 == 0) {
                       int  progress = (int)Math.ceil((double)progressed / n * 100.0);
                        for (ProgressChangedCallback callback : progressChangedCallbacks)
                            callback.callback(progress);
                    }
                }
            }

        return img;
    }

    /**
     * Executes when the computation is ready.
     */
    @Override
    protected void done() {
        if (!isCancelled())
            for (DoneCallback c : doneCallbacks) c.callback(this);
    }

}
