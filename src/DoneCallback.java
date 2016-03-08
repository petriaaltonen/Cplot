/*
 * DoneCallback.java
 * 10.9.2014
 * Petri Aaltonen
 */

/**
 * A sub-classed DoneCallback-instance is passed to a PlotWorker-thread which
 * calls all DoneCallback-instances' callback()-methods after completing its
 * execution.
 * @author petri
 */
public abstract class DoneCallback {
    protected PlotWorker workerRef;
    public abstract void callback();
}
