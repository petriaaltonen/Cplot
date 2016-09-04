import javax.swing.*;
import java.awt.*;

/**
 * Implementation of status bar which the Java standard library lacks.
 * @author Petri Aaltonen
 */
public class StatusBar extends JPanel {

    private final int PREFERREX_XSIZE = 10;
    private final int PREFERRED_YSIZE = 23;

    private JLabel label;
    private JProgressBar progressBar;

    /**
     * Initialize a new status bar.
     */
    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PREFERREX_XSIZE, PREFERRED_YSIZE));
        label = new JLabel();
        add(label, BorderLayout.WEST);
        progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.EAST);
    }

    /**
     * Set the expression which is shown in the left side of the status bar.
     * @param eq
     */
    public void setExpression(String eq) {
        assert eq != null;
        label.setText(eq);
    }

    /**
     * Show or hide the proress bar in the right side of the status bar.
     * @param visible
     */
    public void setProgressBarVisibility(boolean visible) {
        progressBar.setVisible(visible);
    }

    /**
     * Set the progress of the progress bar.
     * @param value an integer value between 0 and 100 inclusive
     */
    public void setProgressBarValue(int value) {
        assert value >= 0 && value <= 100;
        progressBar.setValue(value);
    }

}
