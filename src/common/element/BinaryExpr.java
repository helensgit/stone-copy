package common.element;

import java.util.List;

import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;
import common.ast.Name;
import common.env.Environment;
import exception.StoneException;

public class BinaryExpr extends ASTList {

	public BinaryExpr(List<ASTree> children) {
		super(children);
	}

	public ASTree left() {
		return child(0);
	}

	public String operator() {
		return ((ASTLeaf) child(1)).getToken().getText();
	}

	public ASTree right() {
		return child(2);
	}

	public static ASTree createASTree(List<ASTree> list) {
		return new BinaryExpr(list);
	}

	@Override
	public String toString() {
		return "binary" + super.toString();
	}

	@Override
	public Object eval(Environment env) {
		String op = operator();
		if (op.equals("=")) {
			Object rightEval = right().eval(env);
			computeAssign(env, rightEval);
			return rightEval;
		} else {
			Object leftEval = left().eval(env);
			Object rightEval = right().eval(env);
			return computeOp(leftEval, op, rightEval);
		}
	}

	private Object computeOp(Object leftEval, String op, Object rightEval) {
		if (leftEval instanceof Integer && rightEval instanceof Integer)
			return computeNumber((Integer) leftEval, op, (Integer) rightEval);
		else {
			if (op.equals("+"))
				return String.valueOf(leftEval) + String.valueOf(rightEval);
			else if (op.equals("==")) {
				if (leftEval == null) {
					return rightEval == null ? TRUE : FALSE;
				} else {
					return leftEval.equals(rightEval) ? TRUE : FALSE;
				}
			} else
				throw new StoneException("bad type", this);
		}
	}

	public static final int TRUE = 1;
	public static final int FALSE = 0;

	private Object computeNumber(Integer leftEval, String op, Integer rightEval) {
		int a = leftEval.intValue();
		int b = rightEval.intValue();
		switch (op) {
		case "==":
			return a == b ? TRUE : FALSE;
		case "<":
			return a < b ? TRUE : FALSE;
		case ">":
			return a > b ? TRUE : FALSE;
		case "%":
			return a % b;
		case "*":
			return a * b;
		case "/":
			return a / b;
		case "+":
			return a + b;
		case "-":
			return a - b;
		default:
			throw new StoneException("bad operator ", this);
		}
	}

	private void computeAssign(Environment env, Object rightEval) {
		ASTree left = left();
		if (left instanceof Name) {
			env.put(((Name) left).name(), rightEval);
		} else
			throw new StoneException("bad assignment", this);
	}

}
