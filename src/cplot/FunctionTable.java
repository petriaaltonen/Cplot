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

package cplot;

import java.util.HashMap;

/**
 * Store implementations of functions which shall be accessed with their names.
 * @author Petri Aaltonen
 */
public class FunctionTable {

	private HashMap<String, Function> map = null;

	/**
	 * Create a new function table and populate it with all known functions.
	 */
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

	/**
	 * Add a new function into the table.
	 * @param name name of the function
	 * @param fcn an instance of a subclass of Function
	 * @throws EvaluateException
     */
	public void set(String name, Function fcn) throws EvaluateException {
		map.put(name, fcn);
	}

	/**
	 * Get a function from the table.
	 * @param name function name
	 * @return an instance of a Function's subclass
	 * @throws EvaluateException
     */
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
