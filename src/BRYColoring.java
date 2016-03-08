/*
 * BRYColoring.java
 * 15.4.2014
 * Petri Aaltonen
 */

import java.awt.Color;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class BRYColoring extends Coloring {

    private static final String name = "Red-yellow-black";

    @Override
    public String getName() { return new String(name); }

    @Override
    public boolean isConfigurable() { return false; }

    @Override
    public Color getColor(Complex z) {
        double h = Complex.arg2(z) / 6.0;
        double b = 1.0;
        if (h < Math.PI / 8.0)
            b = h / (Math.PI / 8.0);
        h /= (Math.PI / 3.0);
        h *= h;
        h *= (Math.PI / 3.0);
        return hsvToRgb(h, 1.0, b);
    }

}
