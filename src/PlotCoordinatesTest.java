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