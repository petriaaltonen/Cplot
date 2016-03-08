//
// ParserTest.java
// Petri Aaltonen
//

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class ParserTest {

    Parser parser = null;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test (expected = ParserException.class)
    public void testNullStatement() throws Exception {
        parser.parseStatement(null);
    }

    @Test (expected = ParserException.class)
    public void testEmptyStatement() throws Exception {
        parser.parseStatement("");
    }

    //
    // TEST MIXED INVALID STATEMENTS
    //

    @Test (expected = ParserException.class)
    public void testInvalid1() throws Exception {
        parser.parseStatement("+");
    }

    @Test (expected = ParserException.class)
    public void testInvalid2() throws Exception {
        parser.parseStatement("-");
    }

    @Test (expected = ParserException.class)
    public void testInvalid3() throws Exception {
        parser.parseStatement("1+");
    }

    @Test (expected = ParserException.class)
    public void testInvalid4() throws Exception {
        parser.parseStatement("1-");
    }

    @Test (expected = ParserException.class)
    public void testInvalid5() throws Exception {
        parser.parseStatement("1+1-");
    }

    @Test (expected = ParserException.class)
    public void testInvalid6() throws Exception {
        parser.parseStatement("z+");
    }

    @Test (expected = ParserException.class)
    public void testInvalid7() throws Exception {
        parser.parseStatement(")*7");
    }

    @Test (expected = ParserException.class)
    public void testInvalid8() throws Exception {
        parser.parseStatement("sin)1(");
    }

    @Test (expected = ParserException.class)
    public void testInvalid9() throws Exception {
        parser.parseStatement("1*()");
    }

    @Test (expected = ParserException.class)
    public void testInvalid10() throws Exception {
        parser.parseStatement("1()+5");
    }

    //
    // TEST INVALID NUMBERS
    //

    @Test (expected = ParserException.class)
    public void testInvalidNumber1() throws Exception {
        parser.parseStatement("1.");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber2() throws Exception {
        parser.parseStatement("1.e");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber3() throws Exception {
        parser.parseStatement("1.0e");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber4() throws Exception {
        parser.parseStatement("1.e5");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber5() throws Exception {
        parser.parseStatement("1.e+");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber6() throws Exception {
        parser.parseStatement("1.e-");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber7() throws Exception {
        parser.parseStatement("1.e-5.");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber8() throws Exception {
        parser.parseStatement(".1");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumber9() throws Exception {
        parser.parseStatement("1.e");
    }

    //
    // TEST VALID NUMBERS
    //

    @Test
    public void testValidNumber1() throws Exception {
        assertTrue(parser.parseStatement("1") != null);
    }

    @Test
    public void testValidNumber2() throws Exception {
        assertTrue(parser.parseStatement("1.0") != null);
    }

    @Test
    public void testValidNumber3() throws Exception {
        assertTrue(parser.parseStatement("1.0e5") != null);
    }

    @Test
    public void testValidNumber4() throws Exception {
        assertTrue(parser.parseStatement("1.0e-5") != null);
    }

    @Test
    public void testValidNumber5() throws Exception {
        assertTrue(parser.parseStatement("1e5") != null);
    }

    @Test
    public void testValidNumber6() throws Exception {
        assertTrue(parser.parseStatement("1e-5") != null);
    }

    @Test
    public void testValidNumber7() throws Exception {
        assertTrue(parser.parseStatement("1234.567e89") != null);
    }

    @Test
    public void testValidNumber8() throws Exception {
        assertTrue(parser.parseStatement("1234.567e-89") != null);
    }

    //
    // TEST INVALID PARENTHESES
    //

    @Test (expected = ParserException.class)
    public void testInvalidPar1() throws Exception {
        parser.parseStatement("(1");
    }

    @Test (expected = ParserException.class)
    public void testInvalidPar2() throws Exception {
        parser.parseStatement("1)");
    }

    @Test (expected = ParserException.class)
    public void testInvalidPar3() throws Exception {
        parser.parseStatement("((1)");
    }

    @Test (expected = ParserException.class)
    public void testInvalidPar4() throws Exception {
        parser.parseStatement("(1))");
    }

    @Test (expected = ParserException.class)
    public void testInvalidPar5() throws Exception {
        parser.parseStatement("((1))+(2))");
    }

    //
    // TEST VALID PARENTHESES
    //

    @Test
    public void testValidPar1() throws Exception {
        assertTrue(parser.parseStatement("(1)") != null);
    }

    @Test
    public void testValidPar2() throws Exception {
        assertTrue(parser.parseStatement("(1)*(1-4)") != null);
    }

    @Test
    public void testValidPar3() throws Exception {
        assertTrue(parser.parseStatement("(((((1)*2)*3)*4)*5)") != null);
    }

    //
    // TEST UNARY VS. BINARY OPERATOR
    //

    @Test
    public void testOp1() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("-1");
        assertTrue(root.getClass() == Evaluator.EvalNodeNeg.class);
    }

    @Test
    public void testOp2() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("x-y");
        assertTrue(root.getClass() == Evaluator.EvalNodeSub.class);
    }

    @Test (expected = ParserException.class)
    public void testOp3() throws Exception {
        parser.parseStatement("*1");
    }

    @Test (expected = ParserException.class)
    public void testOp4() throws Exception {
        parser.parseStatement("/1");
    }

    @Test
    public void testOp5() throws Exception {
        assertTrue(parser.parseStatement("1+3") != null);
    }

    @Test
    public void testOp6() throws Exception {
        assertTrue(parser.parseStatement("1-2") != null);
    }

    @Test
    public void testOp7() throws Exception {
        assertTrue(parser.parseStatement("1*7") != null);
    }

    @Test
    public void testOp8() throws Exception {
        assertTrue(parser.parseStatement("1/100") != null);
    }

    //
    // TEST THE DISTINCTION BETWEEN FUNCTIONS AND VARIABLES
    //

    @Test
    public void testFunc1() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin");
        assertTrue(root.getClass() == Evaluator.EvalNodeVar.class);
    }

    @Test
    public void testFunc2() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin(1)");
        assertTrue(root.getClass() == Evaluator.EvalNodeFcn.class);
    }

    @Test
    public void testFunc3() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin(1+1)");
        assertTrue(root.getClass() == Evaluator.EvalNodeFcn.class);
    }

}