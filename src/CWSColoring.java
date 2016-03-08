/*
 * CWSColoring.java
 * 15.4.2014
 * Petri Aaltonen
 */

import java.awt.Color;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class CWSColoring extends Coloring {

    private static final String name = "Color wheel with steps";

    @Override
    public String getName() { return new String(name); }

    @Override
    public boolean isConfigurable() { return false; }

    @Override
    public Color getColor(Complex z) {
        double h = Complex.arg2(z);
        double b = Math.log(Complex.abs(z))
                - (double) Math.floor(Math.log(Complex.abs(z)));
        return hsvToRgb(h, 1.0, b);
    }

}
