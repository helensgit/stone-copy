package exception;

import common.ast.ASTree;

public class StoneException extends RuntimeException{
	
	public StoneException(String msg) {
		super(msg);
	}

    public StoneException(String m, ASTree t) {
        super(m + " " + t.location());
    }
	
}
