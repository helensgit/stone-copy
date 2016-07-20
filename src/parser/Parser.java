package parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lexer.Lexer;
import common.ast.ASTLeaf;
import common.ast.ASTList;
import common.ast.ASTree;
import common.ast.NumberLiteral;
import common.ast.StringLiteral;
import common.element.BinaryExpr;
import common.element.NegativeExpr;
import common.element.PrimaryExpr;
import common.token.Token;
import exception.ParseException;

@SuppressWarnings(value = "all")
public class Parser {

	protected abstract static class Element {
		public abstract void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException;

		public abstract boolean match(Lexer lexer) throws ParseException;
	}

	protected static class Tree extends Element {
		private Parser parser;

		public Tree(Parser p) {
			parser = p;
		}

		@Override
		public void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException {
			reserved.add(parser.parse(lexer));
		}

		@Override
		public boolean match(Lexer lexer) throws ParseException {
			return parser.match(lexer);
		}

	}

	protected static class OrTree extends Element {
		private Parser[] parsers;

		// 需要谨慎使用可变参数
		public OrTree(Parser[] parsers) {
			this.parsers = parsers;
		}

		@Override
		public void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException {
			Parser p = chooseParser(lexer);
			if (p != null)
				reserved.add(p.parse(lexer));
			else
				throw new ParseException(lexer.peek(0));
		}

		@Override
		public boolean match(Lexer lexer) throws ParseException {
			return chooseParser(lexer) != null;
		}

		public Parser chooseParser(Lexer lexer) throws ParseException {
			for (Parser p : parsers) {
				if (p.match(lexer))
					return p;
			}
			return null;
		}
	}

	protected static abstract class AToken extends Element {
		@Override
		public void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException {
			Token t = lexer.read();
			reserved.add(makeAST(t));
		}

		@Override
		public boolean match(Lexer lexer) throws ParseException {
			return test(lexer.peek(0));
		}

		public abstract boolean test(Token peek);

		public abstract ASTree makeAST(Token t);
	}

	protected static class Leaf extends Element {
		String[] tokens;

		public Leaf(String[] pat) {
			this.tokens = pat;
		}

		@Override
		public void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException {
			Token t = lexer.read();
			if (t.isIdentifier()) {
				for (String str : tokens) {
					if (str.equals(t.getText())) {
						find(t, reserved);
						return;
					}
				}
			}
			if (tokens.length > 0)
				throw new ParseException(tokens[0] + " expected.", t);
			else
				throw new ParseException(t);
		}

		protected void find(Token t, List<ASTree> reserved) {
			reserved.add(new ASTLeaf(t));
		}

		@Override
		public boolean match(Lexer lexer) throws ParseException {
			Token t = lexer.peek(0);
			if (!t.isIdentifier())
				return false;
			for (String s : tokens) {
				if (s.equals(t.getText()))
					return true;
			}
			return false;
		}

	}

	protected static class Skip extends Leaf {

		public Skip(String[] pat) {
			super(pat);
		}

		@Override
		protected void find(Token t, List<ASTree> reserved) {
		}
	}

	// protected static class Repeat extends Element {
	//
	// private Parser p;
	// private boolean onlyOnce;
	//
	// public Repeat(Parser p, boolean once) {
	// this.p = p;
	// this.onlyOnce = once;
	// }
	//
	// @Override
	// public void parse(Lexer lexer, List<ASTree> reserved)
	// throws ParseException {
	// while (p.match(lexer)) {
	// ASTree t = p.parse(lexer);
	// if (t.getClass() != ASTList.class || t.numOfChildren() > 0)
	// reserved.add(t);
	// if (onlyOnce)
	// break;
	// }
	// }
	//
	// @Override
	// public boolean match(Lexer lexer) throws ParseException {
	// return p.match(lexer);
	// }
	//
	// }

	protected static class Repeat extends Element {
		protected Parser parser;
		protected boolean onlyOnce;

		protected Repeat(Parser p, boolean once) {
			parser = p;
			onlyOnce = once;
		}

		public void parse(Lexer lexer, List<ASTree> res) throws ParseException {
			while (parser.match(lexer)) {
				ASTree t = parser.parse(lexer);
				if (t.getClass() != ASTList.class || t.numOfChildren() > 0) {
					res.add(t);
				}
				if (onlyOnce)
					break;
			}
		}

		public boolean match(Lexer lexer) throws ParseException {
			return parser.match(lexer);
		}
	}

	protected static class StrToken extends AToken {
		Factory factory;

		public StrToken(Class<? extends ASTLeaf> clazz) {
			this.factory = new Factory(clazz);
		}

		@Override
		public boolean test(Token peek) {
			return peek.isString();
		}

		@Override
		public ASTree makeAST(Token t) {
			return new StringLiteral(t);
		}

	}

	protected static class IdToken extends AToken {
		Set<String> id;
		Factory factory;

		public IdToken(Class<? extends ASTLeaf> clazz, Set<String> set) {
			id = set;
			factory = new Factory(clazz);
		}

		@Override
		public boolean test(Token peek) {
			return peek.isIdentifier() && !id.contains(peek.getText());
		}

		@Override
		public ASTree makeAST(Token t) {
			return factory.newInstance(t);
		}

	}

	protected static class NumToken extends AToken {
		Factory factory;

		public NumToken(Class<? extends ASTLeaf> clazz) {
			this.factory = new Factory(clazz);
		}

		@Override
		public boolean test(Token peek) {
			return peek.isNumber();
		}

		@Override
		public ASTree makeAST(Token t) {
			// System.out.println("->>>" + t.getText() + "===" + factory.clazz);
			return factory.newInstance(t);
		}

	}

	protected static class Precedence {
		int prec;
		boolean leftAssoc;

		public Precedence(int prec, boolean leftAssoc) {
			this.prec = prec;
			this.leftAssoc = leftAssoc;
		}
	}

	public static class Operators extends HashMap<String, Precedence> {
		public static boolean LEFT = true;
		public static boolean RIGHT = false;

		public void add(String op, int prec, boolean left) {
			put(op, new Precedence(prec, left));
		}
	}

	protected static class Expr extends Element {
		private Parser factor;
		private Factory factory;
		private Operators operators;

		public Expr(Class<? extends ASTree> clazz, Parser factor,
				Operators operators) {
			factory = new Factory(clazz);
			this.factor = factor;
			this.operators = operators;
		}

		@Override
		public void parse(Lexer lexer, List<ASTree> reserved)
				throws ParseException {
			ASTree right = factor.parse(lexer);
			Precedence next;
			while ((next = nextOperation(lexer)) != null)
				right = doShift(lexer, right, next.prec);
			reserved.add(right);
		}

		private ASTree doShift(Lexer lexer, ASTree left, int prec)
				throws ParseException {
			List<ASTree> list = new ArrayList<ASTree>();
			list.add(left);
			list.add(new ASTLeaf(lexer.read()));
			ASTree right = factor.parse(lexer);
			Precedence next;
			while ((next = nextOperation(lexer)) != null
					&& rightIsExpr(next, prec))
				right = doShift(lexer, right, next.prec);
			list.add(right);
			return factory.make(list);
		}

		private boolean rightIsExpr(Precedence next, int prec) {
			if (next.leftAssoc)
				return next.prec > prec;
			else
				return next.prec >= prec;
		}

		@Override
		public boolean match(Lexer lexer) throws ParseException {
			return factor.match(lexer);
		}

		public Precedence nextOperation(Lexer lexer) throws ParseException {
			Token t = lexer.peek(0);
			if (t.isIdentifier())
				return operators.get(t.getText());
			else
				return null;
		}
	}

	private static class Factory {
		private Class<? extends ASTree> clazz;
		private String methodName = "createASTree";

		public Factory(Class<? extends ASTree> clazz) {
			if (clazz == null)
				clazz = ASTList.class;
			this.clazz = clazz;
		}

		public ASTree newInstance(Token t) {
			ASTree ret = null;
			try {
				Constructor constructor = clazz.getConstructor(Token.class);
				ret = (ASTree) constructor.newInstance(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return ret;
		}

		// 利用反射生成ast
		public ASTree make(List<ASTree> list) {
			Method method;
			ASTree ret = null;
//			if (clazz == NegativeExpr.class) {
//				System.out.println(list);
//			}
			if (list.size() == 1 && clazz == ASTList.class)
				return list.get(0);
			try {
				method = clazz.getMethod(methodName, List.class);
				ret = (ASTree) method.invoke(null, list);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return ret;
		}
	}

	private List<Element> elements;
	private Factory factory;
	// 显示parser信息
	private String msg = "";

	public Parser(Class<? extends ASTree> clazz) {
		reset(clazz);
	}

	public Parser(Parser p) {
		elements = p.elements;
		factory = p.factory;
	}

	public static Parser rule() {
		return rule(null);
	}

	public static Parser rule(Class<? extends ASTree> clazz) {
		return new Parser(clazz);
	}

	public Parser or(Parser... parsers) {
		elements.add(new OrTree(parsers));
		return this;
	}

	public Parser ast(Parser p) {
		elements.add(new Tree(p));
		return this;
	}

	private void reset(Class<? extends ASTree> clazz) {
		elements = new ArrayList<Element>();
		factory = new Factory(clazz);
	}

	public Parser number(Class<? extends ASTLeaf> clazz) {
		elements.add(new NumToken(clazz));
		return this;
	}

	public Parser string(Class<? extends ASTLeaf> clazz) {
		elements.add(new StrToken(clazz));
		return this;
	}

	public Parser identifier(Set<String> reserved) {
		elements.add(new IdToken(null, reserved));
		return this;
	}

	public Parser identifier(Class<? extends ASTLeaf> clazz,
			Set<String> reserved) {
		elements.add(new IdToken(clazz, reserved));
		return this;
	}

	public Parser sep(String... pat) {
		elements.add(new Skip(pat));
		return this;
	}

	public Parser expression(Parser factor, Operators ops) {
		elements.add(new Expr(BinaryExpr.class, factor, ops));
		return this;
	}

	public Parser option(Parser p) {
		elements.add(new Repeat(p, true));
		return this;
	}

	public Parser repeat(Parser p) {
		elements.add(new Repeat(p, false));
		return this;
	}

	public Parser descp(String msg) {
		this.msg = msg;
		return this;
	}

	public boolean match(Lexer lexer) throws ParseException {
		// System.out.println("===match======");
		if (elements.size() == 0)
			return true;
		else {
			// /一个element匹配就match，显然不合理额
			Element e = elements.get(0);
			return e.match(lexer);

			// 被我改成全局匹配,此方案有问题，由于match不会read token,所以peek token的时候总是一个token
			// for(Element e : elements) {
			// if(!e.match(lexer))
			// return false;
			// }
			// return true;
		}
	}

	public ASTree parse(Lexer lexer) throws ParseException {
		ArrayList<ASTree> ret = new ArrayList<ASTree>();
		for (Element element : elements) {
			element.parse(lexer, ret);
		}
		return factory.make(ret);
	}

	public Parser insertChoice(Parser def) {
		Parser temp = new Parser(this);
		elements = new ArrayList<Parser.Element>();
		elements.add(new OrTree(new Parser[] { def, temp }));
		return this;
	}

	public Parser maybe(Parser args) {
		elements = new ArrayList<Parser.Element>();
		return this;
	}

}
