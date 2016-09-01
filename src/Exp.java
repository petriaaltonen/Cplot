/**
 * The exponential function.
 * @author Petri Aaltonen
 */
public class Exp extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.exp(z);
    }

}
