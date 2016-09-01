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
