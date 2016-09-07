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

import java.util.HashMap;

/**
 * A table of variables
 * @author Petri Aaltonen
 */
public class VarTable {

	HashMap<String, Complex> map = null;

	public VarTable() {
		map = new HashMap<String, Complex>(16);
		map.put("i", new Complex(0.0, 1.0));
	}

	public void set(String var, Complex val) throws EvaluateException {
		if (var.equals("i"))
			throw new EvaluateException("variable name i is reserved");
		map.put(var, val);
	}

	public Complex get(String var) throws EvaluateException {
		Complex val = map.get(var);
		if (val == null) {
			StringBuilder s = new StringBuilder();
			s.append("VarTable contains no variable ");
			s.append(var);
			throw new EvaluateException(s.toString());
		}
		return val;
	}

}
