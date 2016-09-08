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

/**
 * Evaluator takes an eval-tree from a Parser and evaluates the tree producing
 * a complex number as a result.
 * @author Petri Aaltonen
 */
public class Evaluator {

	public static abstract class EvalNode {
		protected VarTable varTable = null;

		public abstract Complex eval() throws EvaluateException;

		void passTables(VarTable ref, FunctionTable fref) {
			varTable = ref;
		}
	}

	public static class EvalNodeVal extends EvalNode {
		private Complex value = null;

		public EvalNodeVal(Complex val) {
			value = new Complex(val);
		}

		@Override
		public Complex eval() throws EvaluateException {
			return value;//new Complex(value);
		}
	}

	public static class EvalNodeVar extends EvalNode {
		private String name = null;

		public EvalNodeVar(String name) {
			this.name = new String(name);
		}

		@Override
		public Complex eval() throws EvaluateException {
			return varTable.get(name);//new Complex(varTable.get(name));
		}
	}

	public static class EvalNodeFcn extends EvalNode {
		private String name = null;
		private EvalNode arg = null;
		private FunctionTable fcnTable = null;

		public EvalNodeFcn(String name, EvalNode arg) {
			this.name = new String(name);
			this.arg = arg;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return fcnTable.get(name).eval(arg.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			fcnTable = fref;
			arg.passTables(ref, fref);
		}
	}

	public static class EvalNodeNeg extends EvalNode {
		private EvalNode right = null;

		public EvalNodeNeg(EvalNode right) {
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.neg(right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	public static class EvalNodeAdd extends EvalNode {
		private EvalNode left = null;
		private EvalNode right = null;

		public EvalNodeAdd(EvalNode left, EvalNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.add(left.eval(), right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			left.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	public static class EvalNodeSub extends EvalNode {
		private EvalNode left = null;
		private EvalNode right = null;

		public EvalNodeSub(EvalNode left, EvalNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.sub(left.eval(), right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			left.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	public static class EvalNodeMul extends EvalNode {
		private EvalNode left = null;
		private EvalNode right = null;

		public EvalNodeMul(EvalNode left, EvalNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.mul(left.eval(), right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			left.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	public static class EvalNodeDiv extends EvalNode {
		private EvalNode left = null;
		private EvalNode right = null;

		public EvalNodeDiv(EvalNode left, EvalNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.div(left.eval(), right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			left.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	public static class EvalNodePow extends EvalNode {
		private EvalNode left = null;
		private EvalNode right = null;

		public EvalNodePow(EvalNode left, EvalNode right) {
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() throws EvaluateException {
			return Complex.pow(left.eval(), right.eval());
		}

		@Override
		void passTables(VarTable ref, FunctionTable fref) {
			super.passTables(ref, fref);
			left.passTables(ref, fref);
			right.passTables(ref, fref);
		}
	}

	private VarTable varTable = null;
	private FunctionTable fcnTable = null;
	private EvalNode evalRoot = null;

	/**
	 * Initialize the evaluator.
	 */
	public Evaluator() {
		varTable = new VarTable();
		fcnTable = new FunctionTable();
	}

	/**
	 * Set the root of a tree.
	 * @param root An EvalNode-object which is supposed to be a root of a tree.
	 */
	public void setRoot(EvalNode root) {
		evalRoot = root;
		root.passTables(varTable, fcnTable);
	}

	/**
	 * Evaluates the tree at complex point z.
	 * @param z A complex number
	 * @return The result of the evaluation
	 */
	public Complex evalAt(Complex z) {
		try {
			varTable.set("z", z);
			return evalRoot.eval();
		} catch (EvaluateException e) {
			// This should never happen if the logic is programmed
			// correctly. Ie. no user input should cause this line to
			// ever be executed.
			assert false : "Evaluator.evalAt exception should not be raised";
			System.out.print("Evaluator.evalAt failed because of an "
					+ "EvaluateException with message " + e.getMessage());
			System.exit(-1);
			return null;
		}
	}

}
