import java.awt.Color;

/**
 * A coloring scheme which uses a full color wheel and shows gradients to represent the
 * changes in absolute value.
 * @author Petri Aaltonen
 */
public class CWSColoring extends Coloring {

    private static final String name = "Color wheel with steps";

    @Override
    public String getName() { return new String(name); }

    @Override
    public Color getColor(Complex z) {
        double h = Complex.arg2(z);
        double b = Math.log(Complex.abs(z))
                - (double) Math.floor(Math.log(Complex.abs(z)));
        return hsvToRgb(h, 1.0, b);
    }

}
