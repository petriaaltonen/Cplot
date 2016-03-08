/*
 * CWAColoring.java
 * 15.4.2014
 * Petri Aaltonen
 */

import java.awt.Color;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class CWAColoring extends Coloring {

    private static final String name = "Color wheel with axes";

    @Override
    public String getName() { return new String(name); }

    @Override
    public boolean isConfigurable() { return false; }

    @Override
    public Color getColor(Complex z) {
        double h = Complex.arg2(z);
        double b = Math.pow(Math.abs(Math.cos(h) * Math.sin(h)), 0.25);
        return hsvToRgb(h, 1.0, b);
    }

}
