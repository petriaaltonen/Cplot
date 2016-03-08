/*
 * Abs.java
 * 26.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Abs extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(Complex.abs(z), 0.0);
    }

}
