package parser.expression;

import lexer.Token;

class Unary extends Expression {

    final Token operator;
    final Expression right;

    Unary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}
