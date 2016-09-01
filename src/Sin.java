/**
 * The sine function
 * @author Petri Aaltonen
 */
public class Sin extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.sin(z);
    }

}
