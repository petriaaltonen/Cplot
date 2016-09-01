/**
 * A sub-classed StartCallback-instance is passed to a Plot-instance which
 * calls all StartCallback-instances' callback()-methods before starting 
 * computing a plot.
 * @author Petri Aaltonen
 * TODO: Consider turning to a functional interface.
 */
public abstract class StartCallback {

    protected PlotWorker workerRef;
    public abstract void callback();

}