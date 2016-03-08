//
// UtilsTest.java
// Petri Aaltonen
//

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


public class UtilsTest {

    private double randDouble(int sig) {
        StringBuilder str = new StringBuilder();
        Random rand = new Random();
        str.append(rand.nextInt(10));
        str.append('.');
        for (int i = 0; i < sig; ++i) {
            str.append(rand.nextInt(9) + 1);
        }
        return Double.parseDouble(str.toString());
    }

    @Test
    public void testSignificantDecimalDigits() {
        assertEquals(Utils.significantDecimalDigits(1.0), 0);
        assertEquals(Utils.significantDecimalDigits(1.1), 1);
        assertEquals(Utils.significantDecimalDigits(1.12), 2);
        assertEquals(Utils.significantDecimalDigits(1.123), 3);
        assertEquals(Utils.significantDecimalDigits(1.1234), 4);
        assertEquals(Utils.significantDecimalDigits(1.12345), 5);
        assertEquals(Utils.significantDecimalDigits(1.123456), 6);
        assertEquals(Utils.significantDecimalDigits(1.1234567), 7);
        assertEquals(Utils.significantDecimalDigits(1.12345678), 8);
        assertEquals(Utils.significantDecimalDigits(8.69923669), 8);

        assertEquals(Utils.significantDecimalDigits(-1.0), 0);
        assertEquals(Utils.significantDecimalDigits(-1.1), 1);
        assertEquals(Utils.significantDecimalDigits(-1.12), 2);

        Random rand = new Random();
        for (int i = 0; i < 10000; ++i) {
            int n = rand.nextInt(10);
            double testCase = randDouble(n);
            assertEquals(Double.toString(testCase), n, Utils.significantDecimalDigits(testCase));
        }
    }

}