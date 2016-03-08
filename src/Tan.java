/*
 * Tan.java
 * 4.3.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Tan extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.tan(z);
    }

}
