//
// UtilsTest.java
// Petri Aaltonen
//

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


public class UtilsTest {

    @Test
    public void testSignificantDecimalDigits0Digits() {
        assertEquals(Utils.significantDecimalDigits(1.0), 0);
    }

    @Test
    public void testSignificantDecimalDigits0DigitsNegative() {
        assertEquals(Utils.significantDecimalDigits(-1.0), 0);
    }

    @Test
    public void testSignificantDecimalDigits1Digit() {
        assertEquals(Utils.significantDecimalDigits(1.1), 1);
    }

    @Test
    public void testSignificantDecimalDigits1DigitNegative() {
        assertEquals(Utils.significantDecimalDigits(-1.1), 1);
    }

    @Test
    public void testSignificantDecimalDigits2Digits() {
        assertEquals(Utils.significantDecimalDigits(1.12), 2);
    }

    @Test
    public void testSignificantDecimalDigits2DigitsNegative() {
        assertEquals(Utils.significantDecimalDigits(-1.12), 2);
    }

    @Test
    public void testSignificantDecimalDigits3Digits() {
        assertEquals(Utils.significantDecimalDigits(1.123), 3);
    }

    @Test
    public void testSignificantDecimalDigits4Digits() {
        assertEquals(Utils.significantDecimalDigits(1.1234), 4);
    }

    @Test
    public void testSignificantDecimalDigits5Digits() {
        assertEquals(Utils.significantDecimalDigits(1.12345), 5);
    }

    @Test
    public void testSignificantDecimalDigits6Digits() {
        assertEquals(Utils.significantDecimalDigits(1.123456), 6);
    }

    @Test
    public void testSignificantDecimalDigits7Digits() {
        assertEquals(Utils.significantDecimalDigits(1.1234567), 7);
    }

    @Test
    public void testSignificantDecimalDigits8Digits() {
        assertEquals(Utils.significantDecimalDigits(1.12345678), 8);
        assertEquals(Utils.significantDecimalDigits(8.69923669), 8);
    }

}