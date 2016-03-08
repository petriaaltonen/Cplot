/*
 * CWColoring.java
 * 15.4.2014
 * Petri Aaltonen
 */

import java.awt.Color;

public class CWColoring extends Coloring {

    private static final String name = "Color wheel";
    
    @Override
    public String getName() { return new String(name); }

    @Override
    public boolean isConfigurable() { return false; }

    @Override
    public Color getColor(Complex z) {
	return hsvToRgb(Complex.arg2(z), 1.0, 1.0);
    }

}
