/**
 * Used as a callback in the middle of computing a plot to indicate the progress of
 * computation.
 * @author Petri Aaltonen
 */
public interface ProgressChangedCallback {

    void callback(int progress);

}
