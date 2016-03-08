/*
 * Exp.java
 * 28.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Exp extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.exp(z);
    }

}
