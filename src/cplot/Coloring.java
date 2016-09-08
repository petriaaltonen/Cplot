/*
    Copyright (C) 2016  Petri Aaltonen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/
 */

package cplot;

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
