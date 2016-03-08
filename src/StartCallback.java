/*
 * StartCallback.java
 * 27.9.2014
 * Petri Aaltonen
 */

/**
 * A sub-classed StartCallback-instance is passed to a Plot-instance which
 * calls all StartCallback-instances' callback()-methods before starting 
 * computing a plot.
 * @author petri
 */
public abstract class StartCallback {
    protected PlotWorker workerRef;
    public abstract void callback();
}