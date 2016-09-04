import java.util.LinkedList;

/**
 * Parser takes a mathematical expression and forms an evaluation tree as a result.
 * @author Petri Aaltonen
 * TODO: Should be reimplemented using the shunting yard algorithm. Scala would probably make the code
 * much shorther and easier to read.
 */
public class Parser {

	private enum TokenGroup {
		NAME, OPERATOR, PARENTHESIS, VALUE
	}

	private enum TokenType {
		VAR, FCN, VAL, NEG, ADD, SUB, MUL, DIV, POW, LPAR, RPAR, UNKNOWN_MINUS, UNKNOWN_NAME
	}

	private static class Token {

		TokenGroup group = null;
		TokenType type = null;
		String string = null;

		//
		// Given a string 'statement' start parsing a token beginning at
		// index 'k'. Return the number of characters read. If we encounter
		// an error such as invalidly formatted number value throw a
		// ParserException.
		//
		int set(String statement, int k) throws ParserException {

			char c = statement.charAt(k);

			if (isAlphabetic(c) || c == '_') {
				group = TokenGroup.NAME;
				type = TokenType.UNKNOWN_NAME;
				string = readName(statement, k);
				return string.length();
			}
			else if (isNumeric(c)) {
				group = TokenGroup.VALUE;
				type = TokenType.VAL;
				string = readNumber(statement, k);
				return string.length();
			}
			else
				switch (c) {
					case '(':
						group = TokenGroup.PARENTHESIS;
						type = TokenType.LPAR;
						string = "(";
						return 1;
					case ')':
						group = TokenGroup.PARENTHESIS;
						type = TokenType.RPAR;
						string = ")";
						return 1;
					case '+':
						group = TokenGroup.OPERATOR;
						type = TokenType.ADD;
						string = "+";
						return 1;
					case '-':
						group = TokenGroup.OPERATOR;
						type = TokenType.UNKNOWN_MINUS;
						string = "-";
						return 1;
					case '*':
						group = TokenGroup.OPERATOR;
						type = TokenType.MUL;
						string = "*";
						return 1;
					case '/':
						group = TokenGroup.OPERATOR;
						type = TokenType.DIV;
						string = "/";
						return 1;
					case '^':
						group = TokenGroup.OPERATOR;
						type = TokenType.POW;
						string = "^";
						return 1;
					default:
						throw new ParserException("unsupported character " + c);
				}
		}

		@Override
		public String toString() {
			switch (type) {
				case VAR:
					return "VAR";
				case FCN:
					return "FCN";
				case VAL:
					return "VAL";
				case NEG:
					return "NEG";
				case ADD:
					return "ADD";
				case SUB:
					return "SUB";
				case MUL:
					return "MUL";
				case DIV:
					return "DIV";
				case POW:
					return "POW";
				case LPAR:
					return "LPAR";
				case RPAR:
					return "RPAR";
				case UNKNOWN_MINUS:
					return "MINUS";
				case UNKNOWN_NAME:
					return "NAME";
				default:
					assert false : "unexpected enum value in Token.toString";
					throw new Error("unexpected enum value");
			}
		}
	}

    /* HERE WE DEFINE SOME STATIC HELPER FUNCTIONS */

	//
	// Return true if a character is an alphanumeric character, ie.
	// if it is either a - z, A - Z or 0 - 9.
	//
	private static boolean isAlphanumeric(char c) {
		if (c >= 'A' && c <= 'Z')
			return true;
		else if (c >= 'a' && c <= 'z')
			return true;
		else if (c >= '0' && c <= '9')
			return true;
		else
			return false;
	}

	//
	// Return true if a character is an alphabetic character, ie.
	// if it is a - z or A - Z.
	//
	private static boolean isAlphabetic(char c) {
		if (c >= 'A' && c <= 'Z')
			return true;
		else if (c >= 'a' && c <= 'z')
			return true;
		else
			return false;
	}

	//
	// Return true if a character is a number character ie. 0 - 9.
	//
	private static boolean isNumeric(char c) {
		if (c >= '0' && c <= '9')
			return true;
		else
			return false;
	}

	//
	// Assuming that 'statement' begins with an alphabet or the underscore at
	// index 'k' read characters which are either alphanumeric characters or the underscore
	// character until none of these is read or the end of 'statement' is
	// reached.
	//
	private static String readName(String statement, int k) {

		StringBuilder str = new StringBuilder();
		int i = k;

		while (i < statement.length()) {
			char c = statement.charAt(i);
			if (isAlphanumeric(c) || c == '_') {
				if (i == 0 && c >= '0' && c <= 9) {
					// This is a programming error!
					assert false : "readName logic flaw";
					throw new Error("readName unexpected behaviour");
				}
				str.append(c);
			}
			else
				break;
			++i;
		}

		return str.toString();
	}

	//
	// Assuming that 'statement' starts with a number at index 'k' read
	// a number of the form xxxx[.yyyyyy][e/E[s]zzz]. The method does not parse
	// the number but returns it as a string.
	// TODO: Do this using regular expressions.
	//
	private static String readNumber(String statement, int k)
			throws ParserException {

		StringBuilder str = new StringBuilder();
		boolean periodFound = false;
		boolean exponentFound = false;
		boolean exponentSignFound = false;
		boolean zeroBeginning = false;
		int i = k;

		while (i < statement.length()) {
			char c = statement.charAt(i);
			if (c >= '0' && c <= '9')
				str.append(c);
			else if (c == '.') {
				if (periodFound)
					throw new ParserException("multiple periods in a number");
				else if (exponentFound)
					throw new ParserException("period after exponent");
				else {
					str.append(c);
					periodFound = true;
				}
			}
			else if (c == 'e' || c == 'E') {
				if (exponentFound)
					throw new ParserException("multiple exponent in a number");
				else if (str.charAt(str.length() - 1) == '.')
					throw new ParserException("exponent follows period");
				else {
					str.append(c);
					exponentFound = true;
				}
			}
			else if (c == '+' || c == '-') {
				if (!exponentFound)
					break; // number ends here
				else if (exponentSignFound)
					throw new ParserException(
							"multiple exponent signs in a number");
				else {
					if (str.charAt(str.length() - 1) != 'e'
							&& str.charAt(str.length() - 1) != 'E')
						throw new ParserException(
								"sign does not follow exponent");
					else {
						str.append(c);
						exponentSignFound = true;
					}
				}
			}
			else
				break;
			++i;
		}

		char c = str.charAt(str.length() - 1);
		if (c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-')
			throw new ParserException("number ends in " + c);

		return str.toString();
	}

	//
	// Return the length of whitespace beginning from index 'k' at
	// 'statement'.
	//
	private static int skipWhitespace(String statement, int k) {
		int i = k;
		while (i < statement.length()) {
			char c = statement.charAt(i);
			if (c == ' ')
				++i;
			else
				break;
		}
		return i - k;
	}

    /* FIELDS OF THE PARSER CLASS */

	private Token[] tokens = null;

    /* CONSTRUCTOR */

	public Parser() {
	}

    /* PROCESSING OF THE INPUT STATEMENT */

	//
	// Read the next token beginning from index 'k' in the string
	// 'statement'. Assign 'token' with the read token. Return the
	// number of characters read.
	//
	private static int nextToken(String statement, int k, Token token)
			throws ParserException {

		int i = k;
		char c = statement.charAt(i);

		if (c == ' ') {
			int tmp = skipWhitespace(statement, i);
			i += tmp;
			if (i == statement.length())
				return tmp;
			else
				c = statement.charAt(i);
		}

		int read = token.set(statement, i);
		return (i + read) - k;
	}

	//
	// Build an array of tokens. If there is an error throw ParserException.
	// We do not yet check for grammatical correctness so any errors that
	// are encountered here are parsing errors such as invalidly formatted
	// number value.
	//
	private void tokenize(String statement) throws ParserException {

		LinkedList<Token> list = new LinkedList<Token>();
		int i = 0;
		while (i < statement.length()) {
			Token token = new Token();
			int read = nextToken(statement, i, token);
			i += read;
			list.addLast(token);
		}

		tokens = (Token[]) list.toArray(new Token[0]);
		resolveMinuses();
		resolveNames();
	}

	//
	// Go through all tokens and determine if a minus '-' is a unary or
	// binary operator.
	//
	private void resolveMinuses() {
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].type == TokenType.UNKNOWN_MINUS) {
				if (i == 0 || tokens[i - 1].type == TokenType.LPAR)
					tokens[i].type = TokenType.NEG;
				else
					tokens[i].type = TokenType.SUB;
			}
		}
	}

	//
	// Go through all tokens and determine if a name is a variable or a
	// function name.
	//
	private void resolveNames() {
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].type == TokenType.UNKNOWN_NAME) {
				if (i < tokens.length - 1
						&& tokens[i + 1].type == TokenType.LPAR)
					tokens[i].type = TokenType.FCN;
				else
					tokens[i].type = TokenType.VAR;
			}
		}
	}

	//
	// Check the grammatical correctness of the statement which has already
	// been tokenized. If grammar is correct, return true. Otherwise throw a
	// ParserException with a message describing the grammatical error.
	//
	private boolean checkGrammar() throws ParserException {

		int parBalance = 0;

		// First we check the special case i = 0.
		TokenGroup groupCur = tokens[0].group;
		TokenType typeCur = tokens[0].type;
		String strCur = tokens[0].string;

		if (groupCur == TokenGroup.PARENTHESIS) {
			if (typeCur == TokenType.RPAR)
				throw new ParserException("statement can't begin with ')'");
			else
				++parBalance;
		}
		else if (groupCur == TokenGroup.OPERATOR && typeCur != TokenType.NEG)
			throw new ParserException(
					"statement can't begin with a binary operator");

		TokenGroup groupPrev = groupCur;
		TokenType typePrev = typeCur;
		String strPrev = strCur;

		for (int i = 1; i < tokens.length; i++) {
			groupPrev = groupCur;
			typePrev = typeCur;
			strPrev = strCur;
			groupCur = tokens[i].group;
			typeCur = tokens[i].type;
			strCur = tokens[i].string;

			switch (groupCur) {
				case PARENTHESIS:
					if (typeCur == TokenType.LPAR) {
						if (groupPrev == TokenGroup.VALUE)
							throw new ParserException("left parenthesis cant't "
									+ "follow a number");
						else
							++parBalance;
					}
					else {
						if (parBalance == 0)
							throw new ParserException("parentheses "
									+ "out of balance");
						else
							--parBalance;
					}
					break;

				case OPERATOR:
					if (groupPrev == TokenGroup.OPERATOR)
						throw new ParserException(
								"operator can't follow an operator");
					else if (typePrev == TokenType.LPAR && typeCur != TokenType.NEG)
						throw new ParserException(
								"binary operator can't follow '('");
					break;

				case VALUE:
					if (typePrev == TokenType.RPAR)
						throw new ParserException("number can't follow ')'");
					else if (groupPrev == TokenGroup.VALUE
							|| groupPrev == TokenGroup.NAME)
						throw new ParserException(
								"number can't follow a number or " + "a name");
					break;

				case NAME:
					if (typePrev == TokenType.RPAR)
						throw new ParserException("name can't follow ')'");
					else if (groupPrev == TokenGroup.VALUE
							|| groupPrev == TokenGroup.NAME)
						throw new ParserException("name can't follow a number of "
								+ "n name");
					break;

				default:
					assert false : "unexpected enum value in checkGrammmar";
					throw new Error("unexpected enum value");
			}
		}

		// Check the last token.
		if (groupCur == TokenGroup.OPERATOR)
			throw new ParserException("statement can't end in an operator");
		if (parBalance != 0)
			throw new ParserException("parentheses out of balance");

		return true;
	}

	/**
	 * Parse the statement and return an eval-tree root.
	 *
	 * @param statement A mathematical expression
	 * @return If statement is a valid mathematical expression return
	 * the root of a new evaluation tree.
	 * @throws ParserException If statement is grammatically incorrect.
	 */
	public Evaluator.EvalNode parseStatement(String statement)
			throws ParserException {

		if (statement == null || statement.equals(""))
			throw new ParserException("empty statement");
		tokenize(statement);
		checkGrammar();
		return buildEvalTree(0, tokens.length - 1);
	}

	/**
	 * Note: Only for debugging
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			str.append(tokens[i].toString());
			str.append(" ");
		}
		return str.toString();
	}

	//
	// Find the first operator or function call which has the highest
	// priority.
	//
	private int findNextOperator(int start, int end) {

		int curLevel = 0;
		int minLevel = Integer.MAX_VALUE;
		int minPos = -1;
		TokenType minOp = null;

		for (int i = end; i >= start; --i) {
			if (tokens[i].group == TokenGroup.PARENTHESIS) {
				if (tokens[i].type == TokenType.LPAR)
					--curLevel;
				else
					++curLevel;
			}
			else if (tokens[i].group == TokenGroup.OPERATOR) {
				if (curLevel < minLevel) {
					minLevel = curLevel;
					minOp = tokens[i].type;
					minPos = i;
				}
				else if (curLevel == minLevel) {
					switch (minOp) {
						case FCN:
							minLevel = curLevel;
							minOp = tokens[i].type;
							minPos = i;
							break;
						case NEG:
						case SUB:
						case MUL:
						case DIV:
						case POW:
							if (tokens[i].type == TokenType.ADD) {
								minLevel = curLevel;
								minOp = TokenType.ADD;
								minPos = i;
							}
							break;
					}
				}
			}
			else if (tokens[i].type == TokenType.FCN) {
				if (curLevel < minLevel) {
					minLevel = curLevel;
					minOp = TokenType.FCN;
					minPos = i;
				}
			}
		}

		return minPos;
	}

	//
	// Starting from index 'start' return the index of the next token which is
	// not a parenthesis.
	//
	private int findNextNonParenToken(int start, int end) {
		int i = start;
		while (i <= end && tokens[i].group == TokenGroup.PARENTHESIS)
			++i;
		return i;
	}

	//
	// Given a list of tokens return the root of the new evaluation tree.
	//
	private Evaluator.EvalNode buildEvalTree(int start, int end)
			throws ParserException {

		assert start >= 0 && end < tokens.length && start <= end : "start = "
				+ start + " end = " + end + " tokens.length = " + tokens.length;

		int i = findNextOperator(start, end);

		// There is no next operator which means that we must create a value
		// or a variable node.
		if (i < 0) {
			int j = findNextNonParenToken(start, end);
			if (j > end) {
				throw new ParserException("empty parentheses are not allowed");
			}
			assert tokens[j].type == TokenType.VAR
					|| tokens[j].type == TokenType.VAL : "Token should be VAR or VAL in buildEvalTree";
			if (tokens[j].type == TokenType.VAR)
				return new Evaluator.EvalNodeVar(tokens[j].string);
			else if (tokens[j].type == TokenType.VAL) {
				try {
					double tmp = Double.parseDouble(tokens[j].string);
					return new Evaluator.EvalNodeVal(new Complex(tmp, 0.0));
				} catch (NumberFormatException e) {
					throw new ParserException("can't parse value");
				}
			}
			else {
				assert false : "Token should be NAME or NUMBER in buildEvalTree";
				throw new Error("unexpected behaviour in buildEvalTree");
			}
		}
		else if (tokens[i].group == TokenGroup.OPERATOR) {
			Evaluator.EvalNode left = null;
			if (i > 0 && tokens[i].type != TokenType.NEG)
				left = buildEvalTree(start, i - 1);
			Evaluator.EvalNode right = buildEvalTree(i + 1, end);

			Evaluator.EvalNode node = null;
			switch (tokens[i].type) {
				case ADD:
					node = new Evaluator.EvalNodeAdd(left, right);
					return node;
				case NEG:
					node = new Evaluator.EvalNodeNeg(right);
					return node;
				case SUB:
					node = new Evaluator.EvalNodeSub(left, right);
					return node;
				case MUL:
					node = new Evaluator.EvalNodeMul(left, right);
					return node;
				case DIV:
					node = new Evaluator.EvalNodeDiv(left, right);
					return node;
				case POW:
					node = new Evaluator.EvalNodePow(left, right);
					return node;
				default:
					assert false : "buildEvalTree switch default case";
			}
		}
		else if (tokens[i].type == TokenType.FCN) {
			Evaluator.EvalNode right = buildEvalTree(i + 1, end);
			return new Evaluator.EvalNodeFcn(tokens[i].string, right);
		}

		assert false : "buildEvalTree should never reach this statement";
		return null;
	}

}
