import java.awt.Color;

/**
 * A coloring scheme which uses a full color wheel.
 * @author Petri Aaltonen
 */
public class CWColoring extends Coloring {

    private static final String name = "Color wheel";
    
    @Override
    public String getName() { return new String(name); }

    @Override
    public Color getColor(Complex z) {
	return hsvToRgb(Complex.arg2(z), 1.0, 1.0);
    }

}
