/*
 * Im.java
 * 28.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Im extends Function {

    @Override
    public Complex eval(Complex z) {
	return new Complex(z.y, 0.0);
    }

}
