import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.HashMap;

/**
 * The application main window
 * @author Petri Aaltonen
 */
public class MainWindow extends JFrame {

	private final int DEFAULT_XSIZE = 800;
	private final int DEFAULT_YSIZE = 600;
	static private final String DEFAULT_EXPRESSION = "z*z / (z - 0.25)";

	// Components to handle parsing, evaluation and plotting
	private Parser parser = null;
	private Evaluator evaluator = null;
	private Plot plot = null;

	// Sub components for the PlotPanel and a FileChooser
	private PlotPanel panel = null;
	JFileChooser fileChooser = null;

	// Menu items
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem saveItem;
	private JMenuItem exitItem;
	private JMenu plotMenu;
	private JMenuItem newPlot;
	private JMenuItem limitsItem;
	private JMenu coloringMenu;
	private JMenu leftMouseButtonMenu;
	private JRadioButtonMenuItem scanMenuItem;
	private JRadioButtonMenuItem zoomMenuItem;
	private JCheckBoxMenuItem boxItem;
	private JCheckBoxMenuItem crosshairItem;
	private JCheckBoxMenuItem toolTipItem;
	private JMenu helpMenu;
	private JMenuItem aboutItem;
	private HashMap<String, JMenuItem> coloringItems = null;

	// The status bar
	private StatusBar statusBar;

	/**
	 * The program has encountered a fatal error which it cannot recover from. Show an error
	 * message in a message dialog box and print the same message in the standard output
	 * stream. Then close the program.
	 * @param e The exception which caused the fatal error (can be null).
     */
	public static void bailOut(Exception e) {
		String msg = (e != null)
				? new String("Complex plot encountered a fatal error and can not continue running.\nException: "
				+ e.toString()
				+ "\nMessage: "
				+ e.getMessage())
				: new String("Complex plot encountered a fatal error and can not continue running.");

		JOptionPane.showMessageDialog(null, msg, "Fatal error", JOptionPane.ERROR_MESSAGE);
		System.out.println(msg);
		System.exit(-1);
	}

	/**
	 * Constructor initializes all sub components.
	 */
	public MainWindow() {

		super("Complex plot");
		setSize(DEFAULT_XSIZE, DEFAULT_YSIZE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		parser = new Parser();
		evaluator = new Evaluator();
		try { evaluator.setRoot(parser.parseStatement(DEFAULT_EXPRESSION)); }
		catch (ParserException e) { bailOut(e); }

		plot = new Plot(evaluator);

		// Create a FileChooser-instance.
		fileChooser = new JFileChooser();

		// Create the menu
		createMenu();
		createStatusBar();

		// Create a new layout for the MainWindow.
		getContentPane().setLayout(new BorderLayout());

		// Add a status bar
		createStatusBar();

		// Add a new plot panel
		panel = new PlotPanel(plot);
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

	/**
	 * Create the application main menu.
	 */
	private void createMenu() {

		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(event -> onSaveMenuItemClick());
		fileMenu.add(saveItem);

		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(event -> onExitMenuItemClick());
		fileMenu.add(exitItem);

		plotMenu = new JMenu("Plot");
		newPlot = new JMenuItem("Create new plot");
		newPlot.addActionListener(event -> onNewPlotMenuItemClick());
		plotMenu.add(newPlot);

		limitsItem = new JMenuItem("Set limits");
		limitsItem.addActionListener(event -> onSetLimitsMenuItemClick());
		plotMenu.add(limitsItem);

		coloringMenu = new JMenu("Coloring");
		String [] coloringNames = plot.listColoring();
		coloringItems = new HashMap<String, JMenuItem>(2*coloringNames.length);
		for (int i = 0; i < coloringNames.length; i++) {
			JMenuItem item = new JMenuItem(coloringNames[i]);
			coloringItems.put(coloringNames[i], item);
			item.addActionListener(event -> onColoringMenuItemClick(event));
			item.setEnabled(true);
			coloringMenu.add(item);
		}
		coloringItems.get(plot.getColoring()).setEnabled(false);
		plotMenu.add(coloringMenu);

		plotMenu.addSeparator();

		leftMouseButtonMenu = new JMenu("Left mouse button action");
		scanMenuItem = new JRadioButtonMenuItem("Scan");
		zoomMenuItem = new JRadioButtonMenuItem("Zoom");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(scanMenuItem);
		buttonGroup.add(zoomMenuItem);
		scanMenuItem.addActionListener(event -> onScanMenuItemClick(event));
		zoomMenuItem.addActionListener(event -> onZoomMenuItemClick(event));
		leftMouseButtonMenu.add(scanMenuItem);
		leftMouseButtonMenu.add(zoomMenuItem);
		plotMenu.add(leftMouseButtonMenu);

		boxItem = new JCheckBoxMenuItem("Enable box");
		boxItem.addActionListener(event -> onBoxMenuItemClick(event));
		boxItem.setState(true);
		plotMenu.add(boxItem);

		crosshairItem = new JCheckBoxMenuItem("Enable crosshair");
		crosshairItem.addActionListener(event -> onCrosshairMenuItemClick(event));
		crosshairItem.setState(true);
		plotMenu.add(crosshairItem);

		toolTipItem = new JCheckBoxMenuItem("Enable tooltip");
		toolTipItem.addActionListener(event -> onToolTipMenuItemClick(event));
		toolTipItem.setState(true);
		plotMenu.add(toolTipItem);

		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(plotMenu);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	}

	/**
	 * Create a status bar for showing the progress of computation.
	 */
	private void createStatusBar() {

		statusBar = new StatusBar();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setExpression(DEFAULT_EXPRESSION);
		statusBar.setProgressBarVisibility(false);

		plot.addStartCallback(() -> statusBar.setProgressBarVisibility(true));
		plot.addProgressChangedCallback(progress -> statusBar.setProgressBarValue(progress));
		plot.addDoneCallback(ref -> statusBar.setProgressBarVisibility(false));
	}

	/**
	 * Respond to the user clicking the save menu item. Show a file chooser which lets the user to
	 * choose the file name and the path where the image will be written. If the chosen file already
	 * exists, show a dialog box which informs the user about this and lets them to choose whether to
	 * overwrite the existing data. If the user chooses no, do nothing. If the user wants to choose a
	 * different file name they have to click the save menu item again.
	 *
	 * We currently support only the png format. If the chosen file name does not end in .png, it will
	 * be added to the file name automatically. This leads to the following behaviour. If the user chooses
	 * an existing file with an extension different than .png, the existing file will not be overwritten
	 * because the file names are not the same but differ by having or not having the .png extension.
	 */
	private void onSaveMenuItemClick() {
		int chosenOption = fileChooser.showSaveDialog(MainWindow.this);
		if (chosenOption == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fileChooser.getSelectedFile();
				if (!Utils.getFileExtension(file.getName()).equals("png"))
					file = new File(file.getPath() + ".png");

				if (file.exists()) {
					if (JOptionPane.showOptionDialog(this, "File " + file.getName() + " already exists. " +
							"Do you want to overwrite the existing data?", "Overwrite existing data", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.NO_OPTION)
						return;
				}

				PlotCoordinates coord = plot.getCoordinates();
				ImageIO.write(plot.getImage().getSubimage(coord.getLeft(), plot.getCoordinates().getTop(),
						coord.getViewportWidth(), coord.getViewportHeight()), "png", file);
			}
			catch (IOException e) {
				String msg = new String("Could not save the imsage due to an IO error: " + e.getMessage());
				JOptionPane.showMessageDialog(this, msg, "Could not save image", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Respond to the user clicking the exit menu item.
	 */
	private void onExitMenuItemClick() {
		System.exit(0);
	}

	/**
	 * Respond to the user clicking the new plot menu item.
	 */
	private void onNewPlotMenuItemClick() {
		NewPlotWindow plotWindow = new NewPlotWindow(plot.listColoring());
		plotWindow.registerCallback(params -> {
			statusBar.setExpression(params.expression);
			plot(params);
		});
	}

	/**
	 * Respond to the user clicking the set limits menu item.
	 */
	private void onSetLimitsMenuItemClick() {
		SetLimitsWindow limitsWindow = new SetLimitsWindow();
		limitsWindow.registerCallback((xmin, xmax, ymin, ymax) -> {
			try {
				plot.setLimits(xmin, xmax, ymin, ymax);
			}
			catch (PlotException e) {
				// SetLimitsWindow has already validated user input data so we really should not
				// get an exception here.
				bailOut(e);
			}
		});
	}

	/**
	 * Respond to the user clicking one of the coloring options menu item.
	 * @param e
     */
	private void onColoringMenuItemClick(ActionEvent e) {
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

	/**
	 * Respond to the user clicking the scan radio button menu item.
	 * @param e
     */
	private void onScanMenuItemClick(ActionEvent e) {
		((JRadioButtonMenuItem)e.getSource()).setSelected(true);
		panel.setDraggingTool(PlotPanel.DraggingTool.SCAN);
	}

	/**
	 * Respond to the user clicking the zoom radio button menu item.
	 * @param e
     */
	private void onZoomMenuItemClick(ActionEvent e) {
		((JRadioButtonMenuItem)e.getSource()).setSelected(true);
		panel.setDraggingTool(PlotPanel.DraggingTool.ZOOM);
	}

	/**
	 * Respond to the user clicking the show box checkbox menu item.
	 * @param e
     */
	private void onBoxMenuItemClick(ActionEvent e) {
		panel.enableBox(((JCheckBoxMenuItem)e.getSource()).getState());
		try { panel.updateBackgroundImage(); }
		catch (PlotException ex) { bailOut(ex); }
		panel.repaint();
	}

	/**
	 * Respond to the user clicking the show corsshair checkbox menu item.
	 * @param e
     */
	private void onCrosshairMenuItemClick(ActionEvent e) {
		panel.enableCrosshair(((JCheckBoxMenuItem)e.getSource()).getState());
		panel.repaint();
	}

	/**
	 * Respond to the user clicking the show tooltip checkbox menu item.
	 * @param e
     */
	private void onToolTipMenuItemClick(ActionEvent e) {
		panel.enableToolTip(((JCheckBoxMenuItem)e.getSource()).getState());
		panel.repaint();
	}

	/**
	 * Make a new plot.
	 * @param params parameters for the new plot
     */
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

	/**
	 * The main function
	 * @param args Not used
     */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow window = new MainWindow();
			}
		});
	}

}
