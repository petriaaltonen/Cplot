/**
 * Implements a complex number and a number of functions for them.
 * @author Petri Aaltonen
 */
public class Complex {

    // TODO: Find an optimal value for TOL.
    private static final double TOL = 1.0e-10;

    public double x = 0.0;
    public double y = 0.0;

    /**
     * Set both the real and the imaginary part to 0.
     */
    public Complex() {}

    /**
     * Create a real number, ie. sets the imaginary part to 0.
     * @param x the real part
     */
    public Complex(double x) {
        this.x = x;
    }

    /**
     * Set both the real and imaginary part to given values.
     * @param x the real part
     * @param y the imaginary part
     */
    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the complex number to the value of another.
     * @param z another complex number, which must not be null
     */
    public Complex(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.Complex");
        this.x = z.x;
        this.y = z.y;
    }

    /**
     * Convert the polar representation of a complex number to the
     * rectangular coordinates.
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
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.abs");
        return Math.sqrt(z.x * z.x + z.y * z.y);
    }

    /**
     * Argument of the complex number in the range from -PI to PI.
     * @param z a complex number
     * @return the argument of the complex number z
     */
    public static double arg(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.arg");
        return Math.atan2(z.y, z.x);
    }

    /**
     * Argument of the complex number in the range from 0 to 2*PI.
     * @param z a complex number
     * @return the argument of the complex number z
     */
    public static double arg2(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.arg2");
        double t = arg(z);
        return t >= 0.0 ? t : 2.0 * Math.PI + t;
    }

    /**
     * Return the complex conjugate of the complex number z.
     * @param z a complex number
     * @return the complex conjugate of z
     */
    public static Complex conj(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.conj");
        return new Complex(z.x, -z.y);
    }

    /**
     * Return the opposite value of the complex number z.
     * @param z a complex number
     * @return the opposite number of the the complex number z 
     */
    public static Complex neg(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.neg");
        return new Complex(-z.x, -z.y);
    }

    /**
     * Return the sum of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z + w
     */
    public static Complex add(Complex z, Complex w) {
        if (z == null || w == null)
            throw new IllegalArgumentException("z and w must not be null in Complex.sum");
        return new Complex(z.x + w.x, z.y + w.y);
    }

    /**
     * Return the difference of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z - w
     */
    public static Complex sub(Complex z, Complex w) {
        if (z == null || w == null)
            throw new IllegalArgumentException("z and w must not be null in Complex.sub");
        return new Complex(z.x - w.x, z.y - w.y);
    }

    /**
     * Return the product of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z * w
     */
    public static Complex mul(Complex z, Complex w) {
        if (z == null || w == null)
            throw new IllegalArgumentException("z and w must not be null in Complex.mul");
        return new Complex(z.x * w.x - z.y * w.y, z.x * w.y + z.y * w.x);
    }

    /**
     * Return the quotient of two complex numbers.
     * @param z a complex number
     * @param w a complex number
     * @return  a new complex number z / w
     * TODO: How to handle the case w equals 0 + 0i?
     */
    public static Complex div(Complex z, Complex w) {
        if (z == null || w == null)
            throw new IllegalArgumentException("z and w must not be null in Complex.div");
        double t = w.x * w.x + w.y * w.y;
        return new Complex((z.x * w.x + z.y * w.y) / t, (z.y * w.x - z.x * w.y) / t);
    }

    /**
     * Return the complex power z^w.
     * @param z the base
     * @param w the exponent
     * @return the complex number z^w
     * TODO: Check any boundary cases!
     */
    public static Complex pow(Complex z, Complex w) {
        if (z == null || w == null)
            throw new IllegalArgumentException("z and w must not be null in Complex.pow");
        return exp(mul(w, log(z)));
    }

    /**
     * Return the complex exponential function exp(z).
     * @param z a complex number
     * @return result of the computation exp(z)
     * TODO: Check any boundary cases!
     */
    public static Complex exp(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.exp");
        return polarToRect(Math.exp(z.x), z.y);
    }

    /**
     * Return the complex logarithm log(z).
     * @param z a complex number
     * @return result of the computation log(z)
     * TODO: log(0.0) is undefined! How do we deal with this?
     */
    public static Complex log(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.log");
        return new Complex(Math.log(abs(z)), arg(z));
    }

    /**
     * Return the complex square root.
     * @param z a complex number
     * @return result of the computation sqrt(z)
     * TODO: Check the boundary cases!
     */
    public static Complex sqrt(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.sqrt");
        // Note: sqrt(0.0) should return 0.0 but it's not possible to compute
        // it using the formula exp(0.5*log(z)).
        return (z.x > -TOL && z.x < TOL && z.y > -TOL && z.y < TOL)
            ? new Complex(0.0, 0.0);
            : Complex.pow(z, new Complex(0.5, 0.0));
    }

    /**
     * Return the complex sine.
     * @param z a complex number
     * @return result of the computation sin(z)
     */
    public static Complex sin(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.sin");
        return new Complex(Math.sin(z.x) * Math.cosh(z.y), Math.cos(z.x)
                * Math.sinh(z.y));
    }

    /**
     * Return the complex cosine.
     * @param z a complex number
     * @return result of the computation cos(z)
     */
    public static Complex cos(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.cos");
        return new Complex(Math.cos(z.x) * Math.cosh(z.y), -Math.sin(z.x)
                * Math.sinh(z.y));
    }

    /**
     * Return the complex tangent.
     * @param z a complex number
     * @return result of the computation tan(z)
     * TODO: How do we handle z equal to 0 + 0i?
     */
    public static Complex tan(Complex z) {
        if (z == null)
            throw new IllegalArgumentException("z must not be null in Complex.tan");
        return Complex.div(sin(z), cos(z));
    }

}
