/**
 * The tangent function
 * @author Petri Aaltonen
 */
public class Tan extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.tan(z);
    }

}
