/*
    Copyright (C) 2016  Petri Aaltonen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/
 */

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
