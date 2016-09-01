import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the Complex class
 * @author Petri Aaltonen
 */
public class ComplexTest {

    private static final double TOL = 1.0e-9;

    public static boolean doubleEquals(double x, double y) {
        return (Math.abs(x - y) < TOL);
    }

    public static boolean complexEquals(Complex z, Complex w) {
        return (doubleEquals(z.x, w.x) && doubleEquals(z.y, w.y));
    }

    @Test
    public void testConstructor() {
        Complex z1 = new Complex();
        Complex z2 = new Complex(1.0);
        Complex z3 = new Complex(1.0, -1.0);
        Complex z4 = new Complex(z3);

        assertTrue(doubleEquals(z1.x, 0.0));
        assertTrue(doubleEquals(z1.y, 0.0));

        assertTrue(doubleEquals(z2.x, 1.0));
        assertTrue(doubleEquals(z2.y, 0.0));

        assertTrue(doubleEquals(z3.x, 1.0));
        assertTrue(doubleEquals(z3.y, -1.0));

        assertTrue(doubleEquals(z4.x, 1.0));
        assertTrue(doubleEquals(z4.y, -1.0));
    }

    @Test
    public void testPolarToRect() {
        assertTrue(complexEquals(Complex.polarToRect(1.0, 0.0), new Complex(1.0, 0.0)));
        assertTrue(complexEquals(Complex.polarToRect(1.0, 0.5 * Math.PI), new Complex(0.0, 1.0)));
        assertTrue(complexEquals(Complex.polarToRect(1.0, Math.PI), new Complex(-1.0, 0.0)));
        assertTrue(complexEquals(Complex.polarToRect(1.0, -0.5 * Math.PI), new Complex(0.0, -1.0)));
        assertTrue(complexEquals(Complex.polarToRect(Math.sqrt(2.0), 0.25 * Math.PI), new Complex(1.0, 1.0)));
    }

    @Test
    public void testAbs() {
        Complex z = new Complex(0.0, 0.0);
        assertTrue(Complex.abs(z) < TOL);

        z = new Complex(1.0, 0.0);
        assertTrue(Complex.abs(z) - 1.0 < TOL);

        z = new Complex(0.0, 1.0);
        assertTrue(Complex.abs(z) - 1.0 < TOL);

        z = new Complex(1.0, 1.0);
        assertTrue(Complex.abs(z) - Math.sqrt(2.0) < TOL);
    }

    @Test
    public void testArg() {
        assertTrue(Complex.arg(new Complex(1.0, 0.0)) < TOL);
        assertTrue(Complex.arg(new Complex(1.0, 1.0)) - 0.25 * Math.PI < TOL);
        assertTrue(Complex.arg(new Complex(0.0, 2.0)) - 0.5 * Math.PI < TOL);
        assertTrue(Complex.arg(new Complex(-10.0, 0.0)) - Math.PI < TOL);
        assertTrue(Complex.arg(new Complex(100.0, -100.0)) + 0.25 * Math.PI < TOL);
        assertTrue(Complex.arg(new Complex(0.0, -1.0)) + 0.5 * Math.PI < TOL);
        assertTrue(Complex.arg(new Complex(-1.0, -1.0)) + 0.75 * Math.PI < TOL);
    }

    @Test
    public void testArg2() {
        assertTrue(Complex.arg2(new Complex(1.0, 0.0)) < TOL);
        assertTrue(Complex.arg2(new Complex(1.0, 1.0)) - 0.25 * Math.PI < TOL);
        assertTrue(Complex.arg2(new Complex(0.0, 2.0)) - 0.5 * Math.PI < TOL);
        assertTrue(Complex.arg2(new Complex(-10.0, 0.0)) - Math.PI < TOL);
        assertTrue(Complex.arg2(new Complex(100.0, -100.0)) - 1.75 * Math.PI < TOL);
        assertTrue(Complex.arg2(new Complex(0.0, -1.0)) - 1.5 * Math.PI < TOL);
        assertTrue(Complex.arg2(new Complex(-1.0, -1.0)) - 1.25 * Math.PI < TOL);
    }

    @Test
    public void testConj() {
        assertTrue(complexEquals(
                Complex.conj(new Complex(1.0, -1.0)),
                new Complex(1.0, 1.0)));
    }

    @Test
    public void testNeg() {
        assertTrue(complexEquals(
                Complex.neg(new Complex(1.0, -1.0)),
                new Complex(-1.0, 1.0)));
    }

    @Test
    public void testAdd() {
        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 1.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, 1.0), new Complex(-1.0, -1.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, -1.0), new Complex(1.0, -1.0)),
                new Complex(2.0, -2.0)));
    }

    @Test
    public void testSub() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 1.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, -1.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, 1.0), new Complex(-1.0, -1.0)),
                new Complex(2.0, 2.0)));

        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, -1.0), new Complex(1.0, -1.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMul() {
        // Cases where one of the arguments is zero.
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(1.0, 1.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        // Real-valued cases
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(10.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(10.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(10.0, 0.0)),
                new Complex(10.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(-10.0, 0.0), new Complex(10.0, 0.0)),
                new Complex(-100.0, 0.0)));

        // Purely imaginary cases
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 1.0), new Complex(0.0, 1.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, -1.0), new Complex(0.0, 1.0)),
                new Complex(1.0, 0.0)));

        // Other cases
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(-2.0, 0.0), new Complex(0.0, 2.0)),
                new Complex(0.0, -4.0)));

        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 1.0), new Complex(1.0, 1.0)),
                new Complex(0.0, 2.0)));
    }

    @Test
    public void testDiv() {
        // Purely real cases
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.div(new Complex(4.0, 0.0), new Complex(-2.0, 0.0)),
                new Complex(-2.0, 0.0)));

        assertTrue(complexEquals(
                Complex.div(new Complex(-10.0, 0.0), new Complex(5.0, 0.0)),
                new Complex(-2.0, 0.0)));

        assertTrue(complexEquals(
                Complex.div(new Complex(-10.0, 0.0), new Complex(-10.0, 0.0)),
                new Complex(1.0, 0.0)));

        // Purely imaginary cases
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, 2.0), new Complex(0.0, 1.0)),
                new Complex(2.0, 0.0)));

        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, -4.0), new Complex(0.0, 2.0)),
                new Complex(-2.0, 0.0)));

        // Other cases
        assertTrue(complexEquals(
                Complex.div(new Complex(-1.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0))); // Basically i^2 / i

        assertTrue(complexEquals(
                Complex.div(new Complex(10.0, 5.0), new Complex(10.0, 5.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPow() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.pow(new Complex(10.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.pow(new Complex(2.0, 0.0), new Complex(2.0, 0.0)),
                new Complex(4.0, 0.0)));

        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(2.0, 0.0)),
                new Complex(-1.0, 0.0))); // i^2 = -1

        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(0.0, 1.0)),
                new Complex(Math.exp(-0.5 * Math.PI), 0.0))); // i^i = exp(-PI/2)
    }

    @Test
    public void testExp() {
        assertTrue(complexEquals(
                Complex.exp(new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.exp(new Complex(1.0, 0.0)),
                new Complex(Math.E, 0.0)));

        assertTrue(complexEquals(
                Complex.exp(new Complex(0.0, Math.PI)),
                new Complex(-1.0, 0.0))); // exp(i*PI) = -1

        assertTrue(complexEquals(
                Complex.exp(new Complex(1.0, -Math.PI)),
                new Complex(-Math.E, 0.0))); // exp(1 - i*PI) = -e
    }

    @Test
    public void testLog() {
        assertTrue(complexEquals(
                Complex.log(new Complex(1.0, 0.0)),
                new Complex()));

        assertTrue(complexEquals(
                Complex.log(new Complex(Math.E, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.log(new Complex(0.0, Math.E)),
                new Complex(1.0, 0.5 * Math.PI)));

        assertTrue(complexEquals(
                Complex.log(new Complex(0.0, -Math.E)),
                new Complex(1.0, -0.5 * Math.PI)));

        assertTrue(complexEquals(
                Complex.log(new Complex(-Math.E, 0.0)),
                new Complex(1.0, Math.PI)));

        assertTrue(complexEquals(
                Complex.log(new Complex(0, 1.0)),
                new Complex(0.0, 0.5 * Math.PI)));
    }

    @Test
    public void testSqrt() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex()),
                new Complex()));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(4.0, 0.0)),
                new Complex(2.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(100.0 * 100.0, 0.0)),
                new Complex(100.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 1.0)),
                new Complex(Math.cos(0.25 * Math.PI), Math.sin(0.25 * Math.PI))));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 4.0)),
                new Complex(2.0 * Math.cos(0.25 * Math.PI), 2.0 * Math.sin(0.25 * Math.PI))));

        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 2.0)),
                new Complex(1.0, 1.0))); // (1 + i)*(1 + i) = 2i
    }

    @Test
    public void testSin() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(0.5*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(2*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(-0.5*Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(-Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 1.0)),
                new Complex(0.0, Math.sinh(1.0))));

        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 2.0)),
                new Complex(0.0, Math.sinh(2.0))));

        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, -1.0)),
                new Complex(0.0, -Math.sinh(1.0))));

        assertTrue(complexEquals(
                Complex.sin(new Complex(0.5*Math.PI, 1.0)),
                new Complex(Math.cosh(1.0), 0.0)));

        assertTrue(complexEquals(
                Complex.sin(new Complex(-Math.PI, -1.0)),
                new Complex(0.0, Math.sinh(1))));
    }

    @Test
    public void testCos() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(0.5*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(2*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(-0.5*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(-Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 1.0)),
                new Complex(Math.cosh(1), 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 2.0)),
                new Complex(Math.cosh(2.0), 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, -1.0)),
                new Complex(Math.cosh(1.0), 0.0)));

        assertTrue(complexEquals(
                Complex.cos(new Complex(0.5*Math.PI, 1.0)),
                new Complex(0.0, -Math.sinh(1.0))));

        assertTrue(complexEquals(
                Complex.cos(new Complex(-Math.PI, -1.0)),
                new Complex(-Math.cosh(1.0), 0.0)));
    }

    @Test
    public void testTan() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(0.25*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(2*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(-0.25*Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(-Math.PI, 0.0)),
                new Complex(0.0, 0.0)));

        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 1.0)),
                new Complex(0.0, Math.sinh(1)/Math.cosh(1))));

        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 2.0)),
                new Complex(0.0, Math.sinh(2)/Math.cosh(2))));

        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, -1.0)),
                new Complex(0.0, -Math.sinh(1)/Math.cosh(1))));

        assertTrue(complexEquals(
                Complex.tan(new Complex(0.5*Math.PI, 1.0)),
                new Complex(0.0, Math.cosh(1)/Math.sinh(1))));

        assertTrue(complexEquals(
                Complex.tan(new Complex(-Math.PI, -1.0)),
                new Complex(0.0, -Math.sinh(1)/Math.cosh(1))));
    }

}