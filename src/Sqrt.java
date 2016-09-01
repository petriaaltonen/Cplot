/**
 * The square root function
 * @author Petri Aaltonen
 */
public class Sqrt extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.sqrt(z);
    }

}
