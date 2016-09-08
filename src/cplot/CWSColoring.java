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
        double b = Math.log(Complex.abs(z)) - Math.floor(Math.log(Complex.abs(z)));
        return hsvToRgb(h, 1.0, b);
    }

}
