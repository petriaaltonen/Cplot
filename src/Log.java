/*
 * Log.java
 * 28.2.2014
 * Petri Aaltonen
 */

/**
 * 
 * @author Petri Aaltonen
 *
 */
public class Log extends Function {

    @Override
    public Complex eval(Complex z) {
	return Complex.log(z);
    }

}
