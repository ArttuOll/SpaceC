package utils;

import parser.expression.Expression;
import parser.expression.LiteralExpression;

public class TestExpressions {

    public static final Expression zero = new LiteralExpression(0.0);
    public static final Expression one = new LiteralExpression(1.0);
    public static final Expression two = new LiteralExpression(2.0);
    public static final Expression three = new LiteralExpression(3.0);
    public static final Expression trueExpression = new LiteralExpression(true);

}
