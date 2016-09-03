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

    public PlotWorker(Evaluator evaluator, Coloring coloring, PlotCoordinates coordinates) {
        super();
        this.evaluator = evaluator;
        this.coloring = coloring;
        this.coordinates = coordinates;
        doneCallbacks = new ArrayList<>(4);
        progressChangedCallbacks = new ArrayList<>(4);
    }

    public void addDoneCallback(DoneCallback callback) {
        callback.workerRef = this;
        doneCallbacks.add(callback);
    }

    public void addProgressChangedCallback(ProgressChangedCallback callback) {
        progressChangedCallbacks.add(callback);
    }

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
                    Color rgb = coloring.getColor(z);
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

    @Override
    protected void done() {
        if (!isCancelled())
            for (DoneCallback c : doneCallbacks) c.callback();
    }

}
