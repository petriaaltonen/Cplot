/*
 * Arg.java
 * 28.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 * 
 */
public class Arg extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(Complex.arg(z), 0.0);
    }

}
