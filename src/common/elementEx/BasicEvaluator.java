package common.elementEx;
import java.util.List;

import javassist.gluonj.Reviser;
import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;
import common.ast.Name;
import common.ast.NumberLiteral;
import common.ast.StringLiteral;
import common.element.BinaryExpr;
import common.element.BlockStament;
import common.element.IfStatement;
import common.element.NegativeExpr;
import common.element.NullStatement;
import common.element.WhileStatement;
import common.env.Environment;
import common.token.Token;
import exception.StoneException;

@Reviser public class BasicEvaluator {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    @Reviser public static abstract class ASTreeEx extends ASTree {
        public abstract Object eval(Environment env);
    }
    @Reviser public static class ASTListEx extends ASTList {
        public ASTListEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
        	System.out.println(this.getClass());
            throw new StoneException("cannot eval:" + toString(), this);
        }
    }
    @Reviser public static class ASTLeafEx extends ASTLeaf {
        public ASTLeafEx(Token t) { super(t); }
        public Object eval(Environment env) {
            throw new StoneException("cannot eval: " + toString(), this);
        }
    }
    @Reviser public static class NumberEx extends NumberLiteral {
        public NumberEx(Token t) { super(t); }
        public Object eval(Environment e) { 
        	return value(); 
        }
    }
    @Reviser public static class StringEx extends StringLiteral {
        public StringEx(Token t) { super(t); }
        public Object eval(Environment e) { 
        	return value();
        }
    }
    @Reviser public static class NameEx extends Name {
        public NameEx(Token t) { super(t); }
        public Object eval(Environment env) {
            Object value = env.get(name());
            if (value == null)
                throw new StoneException("undefined name: " + name(), this);
            else
                return value;
        }
    }
    @Reviser public static class NegativeEx extends NegativeExpr {
        public NegativeEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object v = ((ASTreeEx)operand()).eval(env);
            if (v instanceof Integer)
                return new Integer(-((Integer)v).intValue());
            else
                throw new StoneException("bad type for -", this);
        }
    }
    @Reviser public static class BinaryEx extends BinaryExpr {
        public BinaryEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            String op = operator();
            if ("=".equals(op)) {
                Object right = ((ASTreeEx)right()).eval(env);
                return computeAssign(env, right);
            }
            else {
                Object left = ((ASTreeEx)left()).eval(env);
                Object right = ((ASTreeEx)right()).eval(env);
                return computeOp(left, op, right);
            }
            
        }
        
        @Override
        public String toString() {
        	return "binary" + super.toString();
        }
        protected Object computeAssign(Environment env, Object rvalue) {
            ASTree l = left();
            if (l instanceof Name) {
                env.put(((Name)l).name(), rvalue);
                return rvalue;
            }
            else
                throw new StoneException("bad assignment", this);
        }
        protected Object computeOp(Object left, String op, Object right) {
            if (left instanceof Integer && right instanceof Integer) {
                return computeNumber((Integer)left, op, (Integer)right);
            }
            else
                if (op.equals("+"))
                    return String.valueOf(left) + String.valueOf(right);
                else if (op.equals("==")) {
                    if (left == null)
                        return right == null ? TRUE : FALSE;
                    else
                        return left.equals(right) ? TRUE : FALSE;
                }
                else {
                    throw new StoneException("bad type", this);
                }
        }
        protected Object computeNumber(Integer left, String op, Integer right) {
            int a = left.intValue(); 
            int b = right.intValue();
            if (op.equals("+"))
                return a + b;
            else if (op.equals("-"))
                return a - b;
            else if (op.equals("*"))
                return a * b;
            else if (op.equals("/"))
                return a / b;
            else if (op.equals("%"))
                return a % b;
            else if (op.equals("=="))
                return a == b ? TRUE : FALSE;
            else if (op.equals(">"))
                return a > b ? TRUE : FALSE;
            else if (op.equals("<"))
                return a < b ? TRUE : FALSE;
            else
                throw new StoneException("bad operator", this);
        }
    }
    @Reviser public static class BlockEx extends BlockStament {
        public BlockEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object result = 0;
            for (ASTree t: this) {
                if (!(t instanceof NullStatement))
                    result = ((ASTreeEx)t).eval(env);
            }
            return result;
        }
    }
    @Reviser public static class IfEx extends IfStatement {
        public IfEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object c = ((ASTreeEx)condition()).eval(env);
            if (c instanceof Integer && ((Integer)c).intValue() != FALSE)
                return ((ASTreeEx)body()).eval(env);
            else {
                ASTree b = elseBody();
                if (b == null)
                    return 0;
                else
                    return ((ASTreeEx)b).eval(env);
            }
        }
    }
    @Reviser public static class WhileEx extends WhileStatement {
        public WhileEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object result = 0;
            for (;;) {
                Object c = ((ASTreeEx)condition()).eval(env);
                if (c instanceof Integer && ((Integer)c).intValue() == FALSE)
                    return result;
                else
                    result = ((ASTreeEx)body()).eval(env);
            }
        }
        @Override
        public String toString() {
        	return "while " +  super.toString();
        }
    }
}
