package utils;

public class ErrorReporter {

    private final PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");

    public void error(int line, String messageKey) {
        report(line, propertiesReader.getString(messageKey), "");
    }

    public void error(int line, String messageKey, String erroneusCharacter) {
        report(line, propertiesReader.getString(messageKey), erroneusCharacter);
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
