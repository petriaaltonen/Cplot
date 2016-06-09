/*
 * Re.java
 * 28.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Re extends Function {
    @Override
    public Complex eval(Complex z) {
	return new Complex(z.x, 0.0);
    }
}
