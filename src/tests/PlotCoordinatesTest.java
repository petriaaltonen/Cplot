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

package tests;

import cplot.Complex;
import cplot.PlotCoordinates;
import cplot.PlotException;
import org.junit.Test;
import java.awt.Point;
import static org.junit.Assert.*;

/**
 * Test the PlotCoordinates class
 * @author Petri Aaltonen
 */
public class PlotCoordinatesTest {

    @Test
    public void testPlotCoordinates() {
        PlotCoordinates coord = new PlotCoordinates(100, 200);
        try { coord.setLimits(-1.0, 1.0, -2.0, 2.0); }
        catch (PlotException e) { fail(); }

        assertEquals(100, coord.getViewportWidth());
        assertEquals(200, coord.getViewportHeight());
        assertEquals(300, coord.getMatrixWidth());
        assertEquals(600, coord.getMatrixHeight());
        assertEquals(100, coord.getLeft());
        assertEquals(199, coord.getRight());
        assertEquals(200, coord.getTop());
        assertEquals(399, coord.getBottom());
        assertTrue(ComplexTest.doubleEquals(coord.getXmin(), -1.0));
        assertTrue(ComplexTest.doubleEquals(coord.getXmax(), 1.0));
        assertTrue(ComplexTest.doubleEquals(coord.getYmin(), -2.0));
        assertTrue(ComplexTest.doubleEquals(coord.getYmax(), 2.0));

        assertTrue(ComplexTest.complexEquals(coord.getComplexCoordinates(0, 199), new Complex(-1.0, -2.0)));
        assertTrue(ComplexTest.complexEquals(coord.getComplexCoordinates(0, 0), new Complex(-1.0, 2.0)));
        assertTrue(ComplexTest.complexEquals(coord.getComplexCoordinates(99, 0), new Complex(1.0, 2.0)));
        assertTrue(ComplexTest.complexEquals(coord.getComplexCoordinates(99, 199), new Complex(1.0, -2.0)));

        Point p = coord.getMatrixIndex(new Complex(-1.0, -2.0));
        assertEquals(0, p.x);
        assertEquals(199, p.y);

        p = coord.getMatrixIndex(new Complex(-1.0, 2.0));
        assertEquals(0, p.x);
        assertEquals(0, p.y);

        p = coord.getMatrixIndex(new Complex(1.0, 2.0));
        assertEquals(99, p.x);
        assertEquals(0, p.y);

        p = coord.getMatrixIndex(new Complex(1.0, -2.0));
        assertEquals(99, p.x);
        assertEquals(199, p.y);

        assertNull(coord.getMatrixIndex(new Complex(-1.1, 0.0)));
        assertNull(coord.getMatrixIndex(new Complex(1.1, 0.0)));
        assertNull(coord.getMatrixIndex(new Complex(0.0, -2.1)));
        assertNull(coord.getMatrixIndex(new Complex(0.0, 2.1)));

        assertNull(coord.getComplexCoordinates(-1, 0));
        assertNull(coord.getComplexCoordinates(100, 0));
        assertNull(coord.getComplexCoordinates(0, -1));
        assertNull(coord.getComplexCoordinates(0, 200));
    }

}