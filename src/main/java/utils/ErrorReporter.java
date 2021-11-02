package utils;

import static lexer.tokenTypes.EndOfFile.EOF;

import lexer.Token;

public class ErrorReporter {

    private final PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");

    public void error(int line, String messageKey) {
        report(line, propertiesReader.getString(messageKey), "");
    }

    public void error(int line, String messageKey, String erroneusCharacter) {
        report(line, propertiesReader.getString(messageKey), erroneusCharacter);
    }

    public void error(Token token, String message) {
        if (token.type() == EOF) {
            report(token.line(), propertiesReader.getString("error_location_end"), message);
        } else {
            String errorLocation =
                propertiesReader.getString("error_location_at") + token.lexeme() + "'";
            report(token.line(), errorLocation, message);
        }
    }

    private void report(int line, String message, String erroneusPart) {
        System.err.printf(
            propertiesReader.getString("error_template"),
            line,
            message,
            erroneusPart
        );
    }
}
