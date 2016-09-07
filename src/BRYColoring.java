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

import java.awt.Color;

/**
 * A simple coloring scheme which uses black, red and yellow colors.
 * @author Petri Aaltonen
 */
public class BRYColoring extends Coloring {

    private static final String name = "Red-yellow-black";

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
        return hsvToRgb(h, 1.0, b);
    }

}
