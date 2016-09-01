import java.util.LinkedList;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A window for specifying the limits of a plot
 * @author Petri Aaltonen
 */
public class SetLimitsWindow extends JFrame {

	/**
	 * Just a functional interface for using lambdas in registerCallback.
	 */
	public interface SetLimitsWindowCallback {
		void callback(double xmin, double xmax, double ymin, double ymax);
	}

	// Default window size
	private final int DEFAULT_XSIZE = 300;
	private final int DEFAULT_YSIZE = 200;

	// Text fields for user input.
	private JTextField xminField, xmaxField, yminField, ymaxField;

	// A list of registered callbacks.
	private LinkedList<SetLimitsWindowCallback> callbacks;

	/**
	 * Initialize the window.
	 */
	public SetLimitsWindow() {
		super("Set axes limits");
		setSize(DEFAULT_XSIZE, DEFAULT_YSIZE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);

		callbacks = new LinkedList<>();

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

		// Add buttons' behaviour when they are clicked.
		cancelButton.addActionListener(event -> dispose());
		plotButton.addActionListener(event -> onPlotButtonClick());

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		add(limitsPanel);
		add(buttonPanel);

		setVisible(true);
	}

	/**
	 * Respond to the user clicking the plot button. We shall check that the given values are valid.
	 * If not, show an error message and do nothing further. If the values are ok, call all registered
	 * callbacks and close the window.
	 */
	private void onPlotButtonClick() {
		double xmin, xmax, ymin, ymax;

		try {
			xmin = Double.parseDouble(xminField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "xmin is not a valid number", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			xmax = Double.parseDouble(xmaxField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "xmax is not a valid number", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			ymin = Double.parseDouble(yminField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "ymin is not a valid number", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			ymax = Double.parseDouble(ymaxField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "ymax is not a valid number", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (xmin >= xmax) {
			JOptionPane.showMessageDialog(this, "xmin must be less than xmax", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (ymin >= ymax) {
			JOptionPane.showMessageDialog(this, "ymin must be less than ymax", "Invalid limits", JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (SetLimitsWindowCallback c : callbacks)
			c.callback(xmin, xmax, ymin, ymax);

		dispose();
	}

	/**
	 * Register a new callback.
	 * @param callback If null, do nothing.
     */
	public void registerCallback(SetLimitsWindowCallback callback) {
		if (callback != null)
			callbacks.add(callback);
	}

}
