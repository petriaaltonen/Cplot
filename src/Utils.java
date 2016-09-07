/*
    Copyright (C) 2016  Petri Aaltonen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/
 */

/**
 * Some utility functions
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

    /**
     * Given a file name or a full file path, return the file extension defined as the substring
     * following the last dot in the string.
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename) {
        if (filename.length() == 0)
            return "";
        String[] array = filename.split("\\."); // Note that split takes a regex as its argument so we need to be careful here.
        return (array.length > 1) ? array[array.length - 1] : "";
    }

}
