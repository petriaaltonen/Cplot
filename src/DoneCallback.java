/**
 * A sub-classed DoneCallback-instance is passed to a PlotWorker-thread which
 * calls all DoneCallback-instances' callback()-methods after completing its
 * execution.
 * @author Petri Aaltonen
 * TODO: Turn to a functional interface!
 */
public abstract class DoneCallback {

    protected PlotWorker workerRef;
    public abstract void callback();

}
