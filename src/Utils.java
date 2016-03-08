
import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author petri
 */
public class Utils {
    
    private static final double MYFLOOR_TOL = 1.0e-3;
    
    /*
     * A floor function which returns the ceiling of x if x is very close that
     * value. Otherwise return the floor of x. Eg.
     * myFloor(1 - 10^-3) = 0
     * myFloor(1 - 10^-10) = 1
     */
    private static double myFloor(double x) {
	    if (Math.ceil(x) - x < MYFLOOR_TOL)
            return Math.ceil(x);
	    else
            return Math.floor(x);
    }
    
    /*
     * Return the number of significant decimal digits in x. Ie.
     * 1.0      : 0 sig. digits
     * 1.1      : 1 sig. digit
     * 1.12     : 2 sig. digits
     * NOTE: 10.12 is an example of a case where we need to use myFloor because
     * using floor will incorrectly result with 3 significant digits. This 
     * happens because floor(10.12 * 100) returns 1011 instead of 1012 due to
     * numerical inaccuracy of floating point numbers.
     * 
     * NOTE: This algo is not working correctly. Invent a better one. Eg. the number
     * 8.69923669 should have 8 significant decimal digits but the algo is not able
     * to find out that. It appears that 8.69923669 is actually represented as
     * 8.6992366899999... which causes myFloor of 8.69923889*10^8 to return a number
     * off by one.
     * 
     * NOTE: After relaxing the tolerance in myFloor from 1e-9 to 1e-3 the algo
     * seems to work correctly in the previous case and in all cases in the unit
     * tests. However, we should proof that our solution is correct.
     */
    public static int significantDecimalDigits(double x) {
        int i = 0;
            x = Math.abs(x);
        double y = myFloor(x * Math.pow(10.0, i));
        while (Math.abs(y - x*Math.pow(10.0, i)) > 1e-6) {
            y = myFloor(x * Math.pow(10.0, ++i));
        }
        return i;
    }
    
}
