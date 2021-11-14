package interpreter;

import lexer.Token;

public class RuntimeError extends RuntimeException {

    public final Token token;
    public final String erroneousPart;

    public RuntimeError(Token token, String message, String erroneousPart) {
        super(message);
        this.token = token;
        this.erroneousPart = erroneousPart;
    }

}
