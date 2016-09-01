/**
 * The absolute value function
 * @author Petri Aaltonen
 */
public class Abs extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(Complex.abs(z), 0.0);
    }

}
