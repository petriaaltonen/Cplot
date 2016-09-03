import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the class Complex
 * @author Petri Aaltonen
 */
public class ComplexTest {

    private static final double TOL = 1.0e-9;

    /**
     * Compare the doubles for equality using a tolerance.
     * @param x
     * @param y
     * @return
     */
    public static boolean doubleEquals(double x, double y) {
        return (Math.abs(x - y) < TOL);
    }

    /**
     * Compare two complex numbers for equality using a tolerance.
     * @param z
     * @param w
     * @return
     */
    public static boolean complexEquals(Complex z, Complex w) {
        return (doubleEquals(z.x, w.x) && doubleEquals(z.y, w.y));
    }

    @Test
    public void testConstructorNoParams() {
        Complex z = new Complex();
        assertTrue(doubleEquals(z.x, 0.0));
        assertTrue(doubleEquals(z.y, 0.0));
    }

    @Test
    public void testConstructorOneParam() {
        Complex z = new Complex(1.0);
        assertTrue(doubleEquals(z.x, 1.0));
        assertTrue(doubleEquals(z.y, 0.0));
    }

    @Test
    public void testConstructorTwoParams() {
        Complex z = new Complex(1.0, -1.0);
        assertTrue(doubleEquals(z.x, 1.0));
        assertTrue(doubleEquals(z.y, -1.0));
    }

    @Test
    public void testConstructorComplexParam() {
        Complex z = new Complex(new Complex(1.0, -1.0));
        assertTrue(doubleEquals(z.x, 1.0));
        assertTrue(doubleEquals(z.y, -1.0));
    }

    @Test
    public void testPolarToRectZero() {
        assertTrue(complexEquals(
                Complex.polarToRect(1.0, 0.0),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPolarToRectUnitIm() {
        assertTrue(complexEquals(
                Complex.polarToRect(1.0, 0.5 * Math.PI),
                new Complex(0.0, 1.0)));
    }

    @Test
    public void testPolarToRectMinusUnitReal() {
        assertTrue(complexEquals(
                Complex.polarToRect(1.0, Math.PI),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testPolarToRectMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.polarToRect(1.0, -0.5 * Math.PI),
                new Complex(0.0, -1.0)));
    }

    @Test
    public void testPolarToRectUnitUnit() {
        assertTrue(complexEquals(
                Complex.polarToRect(Math.sqrt(2.0), 0.25 * Math.PI),
                new Complex(1.0, 1.0)));
    }

    @Test
    public void testAbsZero() {
        assertTrue(doubleEquals(
                Complex.abs(new Complex(0.0, 0.0)),
                0.0));
    }

    @Test
    public void testAbsUnitReal() {
        assertTrue(doubleEquals(
                Complex.abs(new Complex(1.0, 0.0)),
                1.0));
    }

    @Test
    public void testAbsUnitIm() {
        assertTrue(doubleEquals(
                Complex.abs(new Complex(0.0, 1.0)),
                1.0));
    }

    @Test
    public void testAbsSqrt2() {
        assertTrue(doubleEquals(
                Complex.abs(new Complex(1.0, 1.0)),
                Math.sqrt(2.0)));
    }

    @Test
    public void testArgZeroRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(1.0, 0.0)),
                0.0));
    }

    @Test
    public void testArgQuarterPiRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(1.0, 1.0)),
                0.25 * Math.PI));
    }

    @Test
    public void testArgHalfPiRads() {
        assertTrue(doubleEquals(Complex.arg(
                new Complex(0.0, 2.0)),
                0.5 * Math.PI));
    }

    @Test
    public void testArgPiRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(-10.0, 0.0)),
                Math.PI));
    }

    @Test
    public void testArgMinusQuarterPiRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(100.0, -100.0)),
                -0.25 * Math.PI));
    }

    @Test
    public void testArgMinusHalfPiRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(0.0,- 1.0)),
                -0.5 * Math.PI));
    }

    @Test
    public void testArgMinusThreeQuartersPiRads() {
        assertTrue(doubleEquals(
                Complex.arg(new Complex(-1.0, -1.0)),
                -0.75 * Math.PI));
    }

    @Test
    public void testArg2ZeroRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(1.0, 0.0)),
                0.0));
    }

    @Test
    public void testArg2QuarterPiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(1.0, 1.0)),
                0.25 * Math.PI));
    }

    @Test
    public void testArg2HalfPiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(0.0, 2.0)),
                0.5 * Math.PI));
    }

    @Test
    public void testArg2PiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(-10.0, 0.0)),
                Math.PI));
    }

    @Test
    public void testArg21Point75PiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(100.0, -100.0)),
                1.75 * Math.PI));
    }

    @Test
    public void testArg2OneAndHalfPiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(0.0, -1.0)),
                1.5 * Math.PI));
    }

    @Test
    public void testArg21Point25PiRads() {
        assertTrue(doubleEquals(
                Complex.arg2(new Complex(-1.0, -1.0)),
                1.25 * Math.PI));
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
    public void testAddZeroAndZero() {
        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testAddUnitReAndZero() {
        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testAddUnitImAndZero() {
        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.1), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.1)));
    }

    @Test
    public void testAddZeroAndUnitRe() {
        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testAddZeroAndUnitIm() {
        assertTrue(complexEquals(
                Complex.add(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0)));
    }

    @Test
    public void testAddUnitUnitAndSame() {
        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, 1.0), new Complex(1.0, 1.0)),
                new Complex(2.0, 2.0)));
    }

    @Test
    public void testAddUnitUnitAndItsNegative() {
        assertTrue(complexEquals(
                Complex.add(new Complex(1.0, 1.0), new Complex(-1.0, -1.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSubZeroAndZero() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSubUnitReAndZero() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testSubUnitImAndZero() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.1), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.1)));
    }

    @Test
    public void testSubZeroAndUnitRe() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testSubZeroAndUnitIm() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, -1.0)));
    }

    @Test
    public void testSubUnitUnitAndSame() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, 1.0), new Complex(1.0, 1.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSubUnitUnitAndItsNegative() {
        assertTrue(complexEquals(
                Complex.sub(new Complex(1.0, 1.0), new Complex(-1.0, -1.0)),
                new Complex(2.0, 2.0)));
    }

    @Test
    public void testMulZeroAndUnitRe() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulZeroAndUnitIm() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulZeroAndUnitUnit() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 0.0), new Complex(1.0, 1.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulUnitReAndZero() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulUnitImAndZero() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulUnitUnitAndZero() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testMulRealOneTimesOne() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testMulRealTenTimesOne() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(10.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(10.0, 0.0)));
    }

    @Test
    public void testMulRealOneTimesTen() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(10.0, 0.0)),
                new Complex(10.0, 0.0)));
    }

    @Test
    public void testMulRealMinusTenTimesTen() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(-10.0, 0.0), new Complex(10.0, 0.0)),
                new Complex(-100.0, 0.0)));
    }

    @Test
    public void testMulRealTenTimesMinusTen() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(10.0, 0.0), new Complex(-10.0, 0.0)),
                new Complex(-100.0, 0.0)));
    }

    @Test
    public void testMulImTimesIm() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, 1.0), new Complex(0.0, 1.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testMulMinusImTimesIm() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(0.0, -1.0), new Complex(0.0, 1.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testMulUnitRealTimesIm() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0)));
    }

    @Test
    public void testMulMinusTwoRealTimesTwoIm() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(-2.0, 0.0), new Complex(0.0, 2.0)),
                new Complex(0.0, -4.0)));
    }

    @Test
    public void testMulUnitUnitTimesUnitUnit() {
        assertTrue(complexEquals(
                Complex.mul(new Complex(1.0, 1.0), new Complex(1.0, 1.0)),
                new Complex(0.0, 2.0)));
    }

    @Test
    public void testDivUnitRealByZero() {
        Complex z = Complex.div(new Complex(1.0, 0.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testDivMinusUnitRealByZero() {
        Complex z = Complex.div(new Complex(-1.0, 0.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testDivUnitImByZero() {
        Complex z = Complex.div(new Complex(0.0, 1.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testDivMinusUnitImByZero() {
        Complex z = Complex.div(new Complex(1.0, -1.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testDivZeroByZero() {
        Complex z = Complex.div(new Complex(0.0, 0.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testDivZeroDivUnitReal() {
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, 0.0), new Complex(1.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testDivFourRealDivTwoReal() {
        assertTrue(complexEquals(
                Complex.div(new Complex(4.0, 0.0), new Complex(2.0, 0.0)),
                new Complex(2.0, 0.0)));
    }

    @Test
    public void testDivFourRealDivMinusTwoReal() {
        assertTrue(complexEquals(
                Complex.div(new Complex(4.0, 0.0), new Complex(-2.0, 0.0)),
                new Complex(-2.0, 0.0)));
    }

    @Test
    public void testDivMinusFourRealDivTwoReal() {
        assertTrue(complexEquals(
                Complex.div(new Complex(-4.0, 0.0), new Complex(2.0, 0.0)),
                new Complex(-2.0, 0.0)));
    }

    @Test
    public void testDivMinusFourRealDivMinusTwoReal() {
        assertTrue(complexEquals(
                Complex.div(new Complex(-4.0, 0.0), new Complex(-2.0, 0.0)),
                new Complex(2.0, 0.0)));
    }

    @Test
    public void testDivFourImDivTwoIm() {
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, 4.0), new Complex(0.0, 2.0)),
                new Complex(2.0, 0.0)));
    }

    @Test
    public void testDivFourImDivMinusTwoIm() {
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, 4.0), new Complex(0.0, -2.0)),
                new Complex(-2.0, 0.0)));
    }

    @Test
    public void testDivMinusFourImDivTwoIm() {
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, -4.0), new Complex(0.0, 2.0)),
                new Complex(-2.0, 0.0)));
    }

    @Test
    public void testDivMinusFourImDivMinusTwoIm() {
        assertTrue(complexEquals(
                Complex.div(new Complex(0.0, -4.0), new Complex(0.0, -2.0)),
                new Complex(2.0, 0.0)));
    }

    @Test
    public void testDivMinusUnitRealDivUnitIm() {
        assertTrue(complexEquals(
                Complex.div(new Complex(-1.0, 0.0), new Complex(0.0, 1.0)),
                new Complex(0.0, 1.0))); // Basically i^2 / i
    }

    @Test
    public void testDivByItself() {
        assertTrue(complexEquals(
                Complex.div(new Complex(4.0, 2.0), new Complex(4.0, 2.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPowZeroToZero() {
        Complex z = Complex.pow(new Complex(0.0, 0.0), new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testPowZeroToUnitRe() {
        Complex z = Complex.pow(new Complex(0.0, 0.0), new Complex(1.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testPowZeroToUnitIm() {
        Complex z = Complex.pow(new Complex(0.0, 0.0), new Complex(0.0, 1.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testPowUnitReToZero() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(1.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPowTenReToZero() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(10.0, 0.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPowUnitImToZero() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testPowFourReToTwoRe() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(4.0, 0.0), new Complex(2.0, 0.0)),
                new Complex(16.0, 0.0)));
    }

    @Test
    public void testPowUnitImToTwoReal() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(2.0, 0.0)),
                new Complex(-1.0, 0.0))); // i^2 = -1
    }

    @Test
    public void testPowUnitImToUnitIm() {
        assertTrue(complexEquals(
                Complex.pow(new Complex(0.0, 1.0), new Complex(0.0, 1.0)),
                new Complex(Math.exp(-0.5 * Math.PI), 0.0))); // i^i = exp(-PI/2)
    }

    @Test
    public void testExpZero() {
        assertTrue(complexEquals(
                Complex.exp(new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testExpUnitRe() {
        assertTrue(complexEquals(
                Complex.exp(new Complex(1.0, 0.0)),
                new Complex(Math.E, 0.0)));
    }

    @Test
    public void testExpPiIm() {
        assertTrue(complexEquals(
                Complex.exp(new Complex(0.0, Math.PI)),
                new Complex(-1.0, 0.0))); // exp(i*PI) = -1
    }

    @Test
    public void testExpUnitReMinusPiIm() {
        assertTrue(complexEquals(
                Complex.exp(new Complex(1.0, -Math.PI)),
                new Complex(-Math.E, 0.0))); // exp(1 - i*PI) = -e
    }

    @Test
    public void testLogZero() {
        Complex z = Complex.log(new Complex(0.0, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testLogUnitRe() {
        assertTrue(complexEquals(
                Complex.log(new Complex(1.0, 0.0)),
                new Complex()));
    }

    @Test
    public void testLogERe() {
        assertTrue(complexEquals(
                Complex.log(new Complex(Math.E, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testLogEIm() {
        assertTrue(complexEquals(
                Complex.log(new Complex(0.0, Math.E)),
                new Complex(1.0, 0.5 * Math.PI)));
    }

    @Test
    public void testLogMinusEIm() {
        assertTrue(complexEquals(
                Complex.log(new Complex(0.0, -Math.E)),
                new Complex(1.0, -0.5 * Math.PI)));
    }

    @Test
    public void testLogMinusERe() {
        assertTrue(complexEquals(
                Complex.log(new Complex(-Math.E, 0.0)),
                new Complex(1.0, Math.PI)));
    }

    @Test
    public void testLogMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.log(new Complex(0, 1.0)),
                new Complex(0.0, 0.5 * Math.PI)));
    }

    @Test
    public void testSqrtZero() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex()),
                new Complex()));
    }

    @Test
    public void testSqrtUnitRe() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex(1.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testSqrtFourRe() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex(4.0, 0.0)),
                new Complex(2.0, 0.0)));
    }

    @Test
    public void testSqrtUnitIm() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 1.0)),
                new Complex(Math.cos(0.25 * Math.PI), Math.sin(0.25 * Math.PI))));
    }

    @Test
    public void testSqrtFourIm() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 4.0)),
                new Complex(2.0 * Math.cos(0.25 * Math.PI), 2.0 * Math.sin(0.25 * Math.PI))));
    }

    @Test
    public void testSqrtTwoIm() {
        assertTrue(complexEquals(
                Complex.sqrt(new Complex(0.0, 2.0)),
                new Complex(1.0, 1.0))); // (1 + i)*(1 + i) = 2i
    }

    @Test
    public void testSinZero() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSinHalfPiRe() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.5*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testSinPiRe() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSinTwoPiRe() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(2*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSinMinusHalfPiRe() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(-0.5*Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testSinMinusPiRe() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(-Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testSinUnitIm() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 1.0)),
                new Complex(0.0, Math.sinh(1.0))));
    }

    @Test
    public void testSinTwoIm() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, 2.0)),
                new Complex(0.0, Math.sinh(2.0))));
    }

    @Test
    public void testSinMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.0, -1.0)),
                new Complex(0.0, -Math.sinh(1.0))));
    }

    @Test
    public void testSinHalfPiReUnitIm() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(0.5*Math.PI, 1.0)),
                new Complex(Math.cosh(1.0), 0.0)));
    }

    @Test
    public void testSinMinusHalfPiReMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.sin(new Complex(-Math.PI, -1.0)),
                new Complex(0.0, Math.sinh(1))));
    }

    @Test
    public void testCosZero() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testCosHalfPiRe() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.5*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testCosPiRe() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testCosTwoPiRe() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(2*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testCosMinusHalfPiRe() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(-0.5*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testCosMinusPiRe() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(-Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testCosUnitIm() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 1.0)),
                new Complex(Math.cosh(1), 0.0)));
    }

    @Test
    public void testCosTwoIm() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, 2.0)),
                new Complex(Math.cosh(2.0), 0.0)));
    }

    @Test
    public void testCosMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.0, -1.0)),
                new Complex(Math.cosh(1.0), 0.0)));
    }

    @Test
    public void testCosHalfPiReUnitIm() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(0.5*Math.PI, 1.0)),
                new Complex(0.0, -Math.sinh(1.0))));
    }

    @Test
    public void testCosMinusHalfPiReMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.cos(new Complex(-Math.PI, -1.0)),
                new Complex(-Math.cosh(1.0), 0.0)));
    }

    @Test
    public void testTanZero() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testTanQuarterPiRe() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.25*Math.PI, 0.0)),
                new Complex(1.0, 0.0)));
    }

    @Test
    public void testTanHalfPiRe() {
        Complex z = Complex.tan(new Complex(0.5*Math.PI, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testTanPiRe() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testTanTwoPiRe() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(2*Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testTanMinusQuarterPiRe() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(-0.25*Math.PI, 0.0)),
                new Complex(-1.0, 0.0)));
    }

    @Test
    public void testTanMinusHalfPiRe() {
        Complex z = Complex.tan(new Complex(-0.5*Math.PI, 0.0));
        assertTrue(Double.isNaN(z.x));
        assertTrue(Double.isNaN(z.y));
    }

    @Test
    public void testTanMinusPiRe() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(-Math.PI, 0.0)),
                new Complex(0.0, 0.0)));
    }

    @Test
    public void testTanUnitIm() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 1.0)),
                new Complex(0.0, Math.sinh(1)/Math.cosh(1))));
    }

    @Test
    public void testTanTwoIm() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, 2.0)),
                new Complex(0.0, Math.sinh(2)/Math.cosh(2))));
    }

    @Test
    public void testTanMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.0, -1.0)),
                new Complex(0.0, -Math.sinh(1)/Math.cosh(1))));
    }

    @Test
    public void testTanHalfPiReUnitIm() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(0.5*Math.PI, 1.0)),
                new Complex(0.0, Math.cosh(1)/Math.sinh(1))));
    }

    @Test
    public void testTanMinusPiReMinusUnitIm() {
        assertTrue(complexEquals(
                Complex.tan(new Complex(-Math.PI, -1.0)),
                new Complex(0.0, -Math.sinh(1)/Math.cosh(1))));
    }

}