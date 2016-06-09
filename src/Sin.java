/*
 * Sin.java
 * 4.3.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Sin extends Function {
    @Override
    public Complex eval(Complex z) {
	return Complex.sin(z);
    }
}
