import java.awt.Color;

/**
 * An abstract class to implement different coloring schemes.
 * @author Petri Aaltonen
 */
public abstract class Coloring {

    /**
     * @param h Hue in range [0, 2*pi)
     * @param s Saturation in range [0, 1]
     * @param v Value in range [0, 1]
     * @return RGB-colour value
     */
    protected static Color hsvToRgb(double h, double s, double v) {
        return Color.getHSBColor((float) (h / (2.0 * Math.PI)),
            (float) s, (float) v);
    }
    
    public abstract String getName();
    public abstract Color getColor(Complex z);
    
}
