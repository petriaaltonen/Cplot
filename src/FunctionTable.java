/*
 * FunctionTable.java
 * 26.2.2014
 * Petri Aaltonen
 */

import java.util.HashMap;

/**
 *
 * @author Petri Aaltonen
 *
 */
public class FunctionTable {

	HashMap<String, Function> map = null;

	public FunctionTable() {
		try {
			map = new HashMap<String, Function>(32);
			set("abs", new Abs());
			set("re", new Re());
			set("im", new Im());
			set("arg", new Arg());
			set("conj", new Conj());
			set("exp", new Exp());
			set("log", new Log());
			set("sqrt", new Sqrt());
			set("sin", new Sin());
			set("cos", new Cos());
			set("tan", new Tan());
		} catch (EvaluateException e) {
			assert false : "FunctionTable.FunctionTable exception should not be raised";
			System.out.print("FunctionTable constructor failed because of an "
					+ "EvaluateException with message " + e.getMessage());
			System.exit(-1);
		}
	}

	public void set(String name, Function fcn) throws EvaluateException {
		map.put(name, fcn);
	}

	public Function get(String name) throws EvaluateException {
		Function fcn = map.get(name);
		if (fcn == null) {
			StringBuilder s = new StringBuilder();
			s.append("FunctionTable contains no function ");
			s.append(name);
			throw new EvaluateException(s.toString());
		}
		return fcn;
	}

}
