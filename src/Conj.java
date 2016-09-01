/**
 * The complex conjugate function
 * @author Petri Aaltonen
 */
public class Conj extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.conj(z);
    }

}
