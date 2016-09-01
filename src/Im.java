/**
 * The function which returns the imaginary part of a complex number
 * @author Petri Aaltonen
 */
public class Im extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(z.y, 0.0);
    }

}
