/*
 * MainWindow.java
 * 5.3.2014
 * Petri Aaltonen
 */

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.HashMap;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class MainWindow extends JFrame {

	private final int DEFAULT_XSIZE = 800;
	private final int DEFAULT_YSIZE = 600;
	static private final String DEFAULT_EXPRESSION = "z*z / (z - 0.25)";

	// Components to handle parsing, evaluation and plotting
	private Parser parser = null;
	private Evaluator evaluator = null;
	private Plot plot = null;

	// Menu items for choosing coloring.
	private HashMap<String, JMenuItem> coloringItems = null;

	//
	// Fatal error, bail out.
	//
	protected void bailOut(Exception e) {
		String msg = "Complex plot encountered a fatal error and can not continue "
				+ "running.\nException: "
				+ e.toString()
				+ "\nMessage: "
				+ e.getMessage();
		JOptionPane.showMessageDialog(this, msg, "Fatal error",
				JOptionPane.ERROR_MESSAGE);
		System.out.println(msg);
		System.exit(-1);
	}

	private PlotPanel panel = null;
	JFileChooser fileChooser = null;

	//
	// Constructor initialises the components parser, evaluator
	// and plot.
	//
	public MainWindow() {

		super("Complex plot");
		setSize(DEFAULT_XSIZE, DEFAULT_YSIZE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialise sub-components.
		parser = new Parser();
		evaluator = new Evaluator();
		try {
			evaluator.setRoot(parser.parseStatement(DEFAULT_EXPRESSION));
		}
		catch (ParserException e) {
			bailOut(e);
		}

		plot = new Plot(evaluator);

		// Create a FileChooser-instance.
		fileChooser = new JFileChooser();

		// Create the menu
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int tmp = fileChooser.showSaveDialog(MainWindow.this);
				if (tmp == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						ImageIO.write(panel.plotImage, "png", file);
					}
					catch (IOException e) {
						// TODO: do something
					}
				}
			}
		});
		fileMenu.add(saveItem);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		JMenu plotMenu = new JMenu("Plot");
		JMenuItem newPlot = new JMenuItem("Create new plot");
		newPlot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewPlotWindow plotWindow = new NewPlotWindow(MainWindow.this, plot);
			}
		});
		plotMenu.add(newPlot);

		JMenuItem limitsItem = new JMenuItem("Set limits");
		limitsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SetLimitsWindow limitsWindow = new SetLimitsWindow(
						MainWindow.this);
			}
		});
		plotMenu.add(limitsItem);

		JMenu coloringMenu = new JMenu("Coloring");
		String [] coloringNames = plot.listColoring();
		coloringItems = new HashMap<String, JMenuItem>(2*coloringNames.length);
		for (int i = 0; i < coloringNames.length; i++) {
			JMenuItem item = new JMenuItem(coloringNames[i]);
			coloringItems.put(coloringNames[i], item);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String oldColoring = plot.getColoring();
					try {
						plot.setColoring(((JMenuItem) e.getSource()).getText());
						panel.updateBackgroundImage();
					}
					catch (PlotException ex) { bailOut(ex); }
					panel.repaint();
					coloringItems.get(oldColoring).setEnabled(true);
					((JMenuItem) e.getSource()).setEnabled(false);
				}
			});
			item.setEnabled(true);
			coloringMenu.add(item);
		}
		coloringItems.get(plot.getColoring()).setEnabled(false);
		plotMenu.add(coloringMenu);

		plotMenu.addSeparator();

		// TODO: Set the status of check and radio boxes according to the default values of PlotPanel.

		JMenu leftMouseButtonMenu = new JMenu("Left mouse button action");
		JRadioButtonMenuItem scanMenuItem = new JRadioButtonMenuItem("Scan");
		JRadioButtonMenuItem zoomMenuItem = new JRadioButtonMenuItem("Zoom");
		//scanMenuItem.setEnabled(panel.getDraggingTool().equals(PlotPanel.DraggingTool.SCAN));
		//zoomMenuItem.setEnabled(panel.getDraggingTool().equals(PlotPanel.DraggingTool.ZOOM));
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(scanMenuItem);
		buttonGroup.add(zoomMenuItem);
		scanMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				panel.setDraggingTool(PlotPanel.DraggingTool.SCAN);
			}
		});
		zoomMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				panel.setDraggingTool(PlotPanel.DraggingTool.ZOOM);
			}
		});
		leftMouseButtonMenu.add(scanMenuItem);
		leftMouseButtonMenu.add(zoomMenuItem);
		plotMenu.add(leftMouseButtonMenu);

		JCheckBoxMenuItem boxItem = new JCheckBoxMenuItem("Enable box");
		boxItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.enableBox(((JCheckBoxMenuItem)e.getSource()).getState());
				try { panel.updateBackgroundImage(); }
				catch (PlotException ex) { bailOut(ex); }
				panel.repaint();
			}
		});
		boxItem.setState(true);
		plotMenu.add(boxItem);

		JCheckBoxMenuItem crosshairItem = new JCheckBoxMenuItem("Enable crosshair");
		crosshairItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.enableCrosshair(((JCheckBoxMenuItem)e.getSource()).getState());
				panel.repaint();
			}
		});
		crosshairItem.setState(true);
		plotMenu.add(crosshairItem);

		JCheckBoxMenuItem toolTipItem = new JCheckBoxMenuItem("Enable tooltip");
		toolTipItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.enableToolTip(((JCheckBoxMenuItem)e.getSource()).getState());
				panel.repaint();
			}
		});
		toolTipItem.setState(true);
		plotMenu.add(toolTipItem);

		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(plotMenu);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		// Create a new layout for the MainWindow.
		getContentPane().setLayout(new BorderLayout());

		// Add a status bar
		StatusBar statusBar = new StatusBar();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setExpression(DEFAULT_EXPRESSION);
		statusBar.setProgressBarVisibility(false);

		plot.addProgressChangedCallback(new ProgressChangedCallback() {
			@Override
			public void callback(int progress) {
				statusBar.setProgressBarValue(progress);
			}
		});

		plot.addStartCallback(new StartCallback() {
			@Override
			public void callback() {
				statusBar.setProgressBarVisibility(true);
			}
		});

		plot.addDoneCallback(new DoneCallback() {
			@Override
			public void callback() {
				statusBar.setProgressBarVisibility(false);
			}
		});

		// Add a new plot panel
		panel = new PlotPanel(this, plot);
		getContentPane().add(panel);
		setVisible(true);

		try { panel.updateBackgroundImage(); }
		catch (PlotException e) { bailOut(e); }

		// At this point we can query panel to set the radio and check box
		// menu items to default values.
		scanMenuItem.setSelected(panel.getDraggingTool() == PlotPanel.DraggingTool.SCAN);
		boxItem.setSelected(panel.isBoxEnabled());
		crosshairItem.setSelected(panel.isCrosshairEnabled());
		toolTipItem.setSelected(panel.isToolTipEnabled());
	}

	//
	// Make a new plot.
	//
	protected void plot(PlotParams params) {
		assert params.xmin < params.xmax : "xmin >= xmax";
		assert params.ymin < params.ymax : "ymin >= ymax";

		try {
			String oldColoring = plot.getColoring();
			evaluator.setRoot(parser.parseStatement(params.expression));
			plot.setColoring(params.color);
			plot.resetViewport();
			plot.setLimits(params.xmin, params.xmax, params.ymin, params.ymax);
			panel.updateBackgroundImage();
			panel.repaint();
			coloringItems.get(oldColoring).setEnabled(true);
			coloringItems.get(params.color).setEnabled(false);
		} catch (ParserException e) {
			String msg = "Expression is invalid for:\n" + e.getMessage();
			JOptionPane.showMessageDialog(this, msg, "Invalid expression",
					JOptionPane.ERROR_MESSAGE);
		}
		catch (PlotException e) {
			bailOut(e);
		}
	}

	//
	// Set new limits for the plot and recreate the plot.
	//
	public void setLimits(double xmin, double xmax, double ymin, double ymax) {
		assert xmin < xmax : "xmin >= xmax";
		assert ymin < ymax : "ymin >= ymax";

		try {
			plot.setLimits(xmin, xmax, ymin, ymax);
			panel.updateBackgroundImage();
			panel.repaint();
		}
		catch (PlotException e) { bailOut(e); }
	}

	//
	// main-function
	//
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow window = new MainWindow();
			}
		});
	}

}
