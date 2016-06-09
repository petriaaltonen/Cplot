/*
 * Complex.java
 * 26.2.2014
 * Petri Aaltonen
 */

/**
 *
 * @author Petri Aaltonen
 *
 */
public class Complex {

    // TODO: Find an optimal value for TOL.
    private static final double TOL = 1.0e-10;

    public double x = 0.0;
    public double y = 0.0;

    /**
     * Constructor which sets the complex number to 0.
     */
    public Complex() {}

    /**
     * Constructor which creates a real number, ie. sets the imaginary part to 0.
     * @param x the real part
     */
    public Complex(double x) {
        this.x = x;
    }

    /**
     * Constructor
     * @param x the real part
     * @param y the imaginary part
     */
    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A copy constructor
     * @param z another complex number
     */
    public Complex(Complex z) {
        this.x = z.x;
        this.y = z.y;
    }

    /**
     * Convert the polar representation of a complex number to the
     * rectangular coordinates.
     *
     * @param r the polar radius
     * @param t the polar angle in radians
     * @return the complex number r*exp(i*t)
     */
    public static Complex polarToRect(double r, double t) {
        return new Complex(r * Math.cos(t), r * Math.sin(t));
    }

    /**
     * Return the absolute value of a complex number.
     * @param z a complex number
     * @return the absolute value of the complex number z
     */
    public static double abs(Complex z) {
        return Math.sqrt(z.x * z.x + z.y * z.y);
    }

    /**
     * Argument of the complex number in the range from -PI to PI.
     * @param z a complex number
     * @return the argument of the complex number z
     */
    public static double arg(Complex z) {
        return Math.atan2(z.y, z.x);
    }

    /**
     * Argument of the complex number in the range from 0 to 2*PI.
     * @param z a complex number
     * @return the argument of the complex number z
     */
    public static double arg2(Complex z) {
        double t = arg(z);
        if (t >= 0.0) return t;
        else return (2.0 * Math.PI + t);
    }

    /**
     * Return the complex conjugate of the complex number z.
     * @param z a complex number
     * @return the complex conjugate of z
     */
    public static Complex conj(Complex z) {
        return new Complex(z.x, -z.y);
    }

    /**
     * Return the opposite value of the complex number z.
     * @param z a complex number
     * @return the opposite number of the the complex number z 
     */
    public static Complex neg(Complex z) {
        return new Complex(-z.x, -z.y);
    }

    /**
     * Return the sum of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z + w
     */
    public static Complex add(Complex z, Complex w) {
        return new Complex(z.x + w.x, z.y + w.y);
    }

    /**
     * Return the difference of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z - w
     */
    public static Complex sub(Complex z, Complex w) {
        return new Complex(z.x - w.x, z.y - w.y);
    }

    /**
     * Return the product of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z * w
     */
    public static Complex mul(Complex z, Complex w) {
        return new Complex(z.x * w.x - z.y * w.y, z.x * w.y + z.y * w.x);
    }

    /**
     * Return the quotient of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z / w
     */
    public static Complex div(Complex z, Complex w) {
        double t = w.x * w.x + w.y * w.y;
        return new Complex((z.x * w.x + z.y * w.y) / t, (z.y * w.x - z.x * w.y) / t);
    }

    /**
     * Return the complex power z^w.
     * @param z the base
     * @param w the exponent
     * @return the complex number z^w
     */
    public static Complex pow(Complex z, Complex w) {
        return exp(mul(w, log(z)));
    }

    /**
     * Return the complex exponential function exp(z).
     * @param z a complex number
     * @return result of the computation exp(z)
     */
    public static Complex exp(Complex z) {
        return polarToRect(Math.exp(z.x), z.y);
    }

    /**
     * Return the complex logarithm log(z).
     * @param z a complex number
     * @return result of the computation log(z)
     */
    public static Complex log(Complex z) {
        // TODO: log(0.0) is undefined! How do we deal with this?
        return new Complex(Math.log(abs(z)), arg(z));
    }

    /**
     * Return the complex square root.
     * @param z a complex number
     * @return result of the computation sqrt(z)
     */
    public static Complex sqrt(Complex z) {
        // Note: sqrt(0.0) should return 0.0 but it's not possible to compute
        // it using the formula exp(0.5*log(z)).
        if (z.x > -TOL && z.x < TOL && z.y > -TOL && z.y < TOL)
            return new Complex(0.0, 0.0);
        else
            return Complex.pow(z, new Complex(0.5, 0.0));
    }

    /**
     * Return the complex sine.
     * @param z a complex number
     * @return result of the computation sin(z)
     */
    public static Complex sin(Complex z) {
        return new Complex(Math.sin(z.x) * Math.cosh(z.y), Math.cos(z.x)
                * Math.sinh(z.y));
    }

    /**
     * Return the complex cosine.
     * @param z a complex number
     * @return result of the computation cos(z)
     */
    public static Complex cos(Complex z) {
        return new Complex(Math.cos(z.x) * Math.cosh(z.y), -Math.sin(z.x)
                * Math.sinh(z.y));
    }

    /**
     * Return the complex tangent.
     * @param z a complex number
     * @return result of the computation tan(z)
     */
    public static Complex tan(Complex z) {
        return Complex.div(sin(z), cos(z));
    }

}
