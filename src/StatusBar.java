//
// StatusBar.java
// Petri Aaltonen
//

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final int PREFERREX_XSIZE = 10;
    private final int PREFERRED_YSIZE = 23;

    private JLabel label;
    private JProgressBar progressBar;

    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PREFERREX_XSIZE, PREFERRED_YSIZE));
        label = new JLabel();
        add(label, BorderLayout.WEST);
        progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        add(progressBar, BorderLayout.EAST);
    }

    public void setExpression(String eq) {
        assert eq != null;
        label.setText(eq);
    }

    public void setProgressBarVisibility(boolean visible) {
        progressBar.setVisible(visible);
    }

    public void setProgressBarValue(int value) {
        assert value >= 0 && value <= 100;
        progressBar.setValue(value);
    }

}
