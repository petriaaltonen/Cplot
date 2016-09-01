import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test the Parser class
 * @author Petri Aaltonen
 */
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
    public void testInvalidOnlyAddOp() throws Exception {
        parser.parseStatement("+");
    }

    @Test (expected = ParserException.class)
    public void testInvalidOnlySubOp() throws Exception {
        parser.parseStatement("-");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNoSecondArgToAdd() throws Exception {
        parser.parseStatement("1+");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNoSecondArgToSub() throws Exception {
        parser.parseStatement("1-");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNoSecondArgToSubAfterGoodSubExpression() throws Exception {
        parser.parseStatement("1+1-");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNoSecondArgToAddAfterVariableName() throws Exception {
        parser.parseStatement("z+");
    }

    @Test (expected = ParserException.class)
    public void testInvalidExpressionStartsWithRightParen() throws Exception {
        parser.parseStatement(")*7");
    }

    @Test (expected = ParserException.class)
    public void testInvalidFunctionParamParensWrongWay() throws Exception {
        parser.parseStatement("sin)1(");
    }

    @Test (expected = ParserException.class)
    public void testInvalidEmptyParens() throws Exception {
        parser.parseStatement("1*()");
    }

    @Test (expected = ParserException.class)
    public void testInvalidParensAfterValue() throws Exception {
        parser.parseStatement("1()+5");
    }

    //
    // TEST INVALID NUMBERS
    //

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitsAfterDot() throws Exception {
        parser.parseStatement("1.");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberEAfterDot() throws Exception {
        parser.parseStatement("1.e");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitsAfterE() throws Exception {
        parser.parseStatement("1.0e");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitsBetweenDotAndE() throws Exception {
        parser.parseStatement("1.e5");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitsAfterEAndPlus() throws Exception {
        parser.parseStatement("1.e+");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitsAfterEAndMinus() throws Exception {
        parser.parseStatement("1.e-");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberAdditionalDotAfterExponentPart() throws Exception {
        parser.parseStatement("1.e-5.");
    }

    @Test (expected = ParserException.class)
    public void testInvalidNumberNoDigitBeforeDot() throws Exception {
        parser.parseStatement(".1");
    }

    //
    // TEST VALID NUMBERS
    //

    @Test
    public void testValidNumber1() throws Exception {
        assertTrue(parser.parseStatement("1") != null);
    }

    @Test
    public void testValidNumber1Dot0() throws Exception {
        assertTrue(parser.parseStatement("1.0") != null);
    }

    @Test
    public void testValidNumber1Dot0E5() throws Exception {
        assertTrue(parser.parseStatement("1.0e5") != null);
    }

    @Test
    public void testValidNumber1Dot0EMinus5() throws Exception {
        assertTrue(parser.parseStatement("1.0e-5") != null);
    }

    @Test
    public void testValidNumber1E5() throws Exception {
        assertTrue(parser.parseStatement("1e5") != null);
    }

    @Test
    public void testValidNumber1EMinus5() throws Exception {
        assertTrue(parser.parseStatement("1e-5") != null);
    }

    @Test
    public void testValidNumber1234Dot567E89() throws Exception {
        assertTrue(parser.parseStatement("1234.567e89") != null);
    }

    @Test
    public void testValidNumber1234Dot567EMinus89() throws Exception {
        assertTrue(parser.parseStatement("1234.567e-89") != null);
    }

    //
    // TEST INVALID PARENTHESES
    //

    @Test (expected = ParserException.class)
    public void testInvalidParSingleLeftParen() throws Exception {
        parser.parseStatement("(1");
    }

    @Test (expected = ParserException.class)
    public void testInvalidParSingleRightParen() throws Exception {
        parser.parseStatement("1)");
    }

    @Test (expected = ParserException.class)
    public void testInvalidParTwoLeftParensOneRightParen() throws Exception {
        parser.parseStatement("((1)");
    }

    @Test (expected = ParserException.class)
    public void testInvalidParOneLeftParenTwoRightParens() throws Exception {
        parser.parseStatement("(1))");
    }

    @Test (expected = ParserException.class)
    public void testInvalidParThreeLeftParensFourRightParens() throws Exception {
        parser.parseStatement("((1))+(2))");
    }

    //
    // TEST VALID PARENTHESES
    //

    @Test
    public void testValidParOneParens() throws Exception {
        assertTrue(parser.parseStatement("(1)") != null);
    }

    @Test
    public void testValidParTwoParens() throws Exception {
        assertTrue(parser.parseStatement("(1)*(1-4)") != null);
    }

    @Test
    public void testValidParManyNestedParens() throws Exception {
        assertTrue(parser.parseStatement("(((((1)*2)*3)*4)*5)") != null);
    }

    //
    // TEST UNARY VS. BINARY OPERATOR
    //

    @Test
    public void testParsesToNegOp() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("-1");
        assertTrue(root.getClass() == Evaluator.EvalNodeNeg.class);
    }

    @Test
    public void testParsesToSubOp() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("x-y");
        assertTrue(root.getClass() == Evaluator.EvalNodeSub.class);
    }

    @Test (expected = ParserException.class)
    public void testMulOpHasNoLeftOperand() throws Exception {
        parser.parseStatement("*1");
    }

    @Test (expected = ParserException.class)
    public void testDivOpHasNoLeftOperand() throws Exception {
        parser.parseStatement("/1");
    }

    @Test
    public void testValidExpressionWithAddOp() throws Exception {
        assertTrue(parser.parseStatement("1+3") != null);
    }

    @Test
    public void testValidExpressionWithSubOp() throws Exception {
        assertTrue(parser.parseStatement("1-2") != null);
    }

    @Test
    public void testValidExpressionWithMulOp() throws Exception {
        assertTrue(parser.parseStatement("1*7") != null);
    }

    @Test
    public void testValidExpressionWithDivOp() throws Exception {
        assertTrue(parser.parseStatement("1/100") != null);
    }

    //
    // TEST THE DISTINCTION BETWEEN FUNCTIONS AND VARIABLES
    //

    @Test
    public void testSinParsesToVariable() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin");
        assertTrue(root.getClass() == Evaluator.EvalNodeVar.class);
    }

    @Test
    public void testSinParsesToFunctionValueAsParam() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin(1)");
        assertTrue(root.getClass() == Evaluator.EvalNodeFcn.class);
    }

    @Test
    public void testSinParsesToFunctionComplexExpressionAsParam() throws Exception {
        Evaluator.EvalNode root = parser.parseStatement("sin(1+1)");
        assertTrue(root.getClass() == Evaluator.EvalNodeFcn.class);
    }

}