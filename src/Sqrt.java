/*
 * Sqrt.java
 * 4.3.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Sqrt extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.sqrt(z);
    }

}
