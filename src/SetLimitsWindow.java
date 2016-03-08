/*
 * SetLimitsWindow.java
 * 15.3.2014
 * Petri Aaltonen
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class SetLimitsWindow extends JFrame {

	private final int DEFAULT_XSIZE = 300;
	private final int DEFAULT_YSIZE = 200;

	MainWindow mainWindow;
	SetLimitsWindow selfRef = this;
	JTextField xminField, xmaxField, yminField, ymaxField;

	public SetLimitsWindow(MainWindow mainRef) {
		super("Set axes limits");
		setSize(DEFAULT_XSIZE, DEFAULT_YSIZE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);

		mainWindow = mainRef;

		JPanel limitsPanel = new JPanel();
		limitsPanel.setLayout(new GridLayout(2, 2));
		Border limitsBorder = BorderFactory.createTitledBorder("Axes limits");
		limitsPanel.setBorder(limitsBorder);

		JPanel xminPanel = new JPanel();
		Border xminBorder = BorderFactory.createTitledBorder("xmin");
		xminPanel.setBorder(xminBorder);
		xminField = new JTextField("-1.0", 10);
		xminPanel.add(xminField);

		JPanel xmaxPanel = new JPanel();
		Border xmaxBorder = BorderFactory.createTitledBorder("xmax");
		xmaxPanel.setBorder(xmaxBorder);
		xmaxField = new JTextField("1.0", 10);
		xmaxPanel.add(xmaxField);

		JPanel yminPanel = new JPanel();
		Border yminBorder = BorderFactory.createTitledBorder("ymin");
		yminPanel.setBorder(yminBorder);
		yminField = new JTextField("-1.0", 10);
		yminPanel.add(yminField);

		JPanel ymaxPanel = new JPanel();
		Border ymaxBorder = BorderFactory.createTitledBorder("ymax");
		ymaxPanel.setBorder(ymaxBorder);
		ymaxField = new JTextField("1.0", 10);
		ymaxPanel.add(ymaxField);

		limitsPanel.add(xminPanel);
		limitsPanel.add(xmaxPanel);
		limitsPanel.add(yminPanel);
		limitsPanel.add(ymaxPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton cancelButton = new JButton("Cancel");
		JButton plotButton = new JButton("Plot");
		buttonPanel.add(cancelButton);
		buttonPanel.add(plotButton);

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		plotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				double xmin, xmax, ymin, ymax;

				try {
					xmin = Double.parseDouble(xminField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(selfRef,
							"xmin is not a valid number", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					xmax = Double.parseDouble(xmaxField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(selfRef,
							"xmax is not a valid number", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					ymin = Double.parseDouble(yminField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(selfRef,
							"ymin is not a valid number", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					ymax = Double.parseDouble(ymaxField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(selfRef,
							"ymax is not a valid number", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (xmin >= xmax) {
					JOptionPane.showMessageDialog(selfRef,
							"xmin must be less than xmax", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ymin >= ymax) {
					JOptionPane.showMessageDialog(selfRef,
							"ymin must be less than ymax", "Invalid limits",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				dispose();
				mainWindow.setLimits(xmin, xmax, ymin, ymax);
			}
		});

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(limitsPanel);
		add(buttonPanel);

		setVisible(true);
	}

}
