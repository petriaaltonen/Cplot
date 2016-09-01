import java.awt.Color;

/**
 * A coloring scheme which uses black, red and yellow colors and makes gradients to represent change
 * in absolute value.
 * @author Petri Aaltonen
 *
 */
public class BRYSColoring extends Coloring {

	private static final String name = "Red-yellow-black with steps";

	@Override
	public String getName() { return new String(name); }

	@Override
	public Color getColor(Complex z) {
		double h = Complex.arg2(z) / 6.0;
		double b = 1.0;
		if (h < Math.PI / 8.0)
			b = h / (Math.PI / 8.0);
		h /= (Math.PI / 3.0);
		h *= h;
		h *= (Math.PI / 3.0);
		double s = Math.log(Complex.abs(z)) - (double) Math.floor(Math.log(Complex.abs(z)));
		return hsvToRgb(h, s, b);
	}

}
