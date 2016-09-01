/**
 * The cosine function
 * @author Petri Aaltonen
 */
public class Cos extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.cos(z);
    }

}
