/**
 * The logarithm function
 * @author Petri Aaltonen
 */
public class Log extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.log(z);
    }

}
