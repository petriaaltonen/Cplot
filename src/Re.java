/**
 * The function which returns the real part of a complex number
 * @author Petri Aaltonen
 */
public class Re extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(z.x, 0.0);
    }

}
