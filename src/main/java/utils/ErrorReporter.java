package utils;

public class ErrorReporter {

    private final PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");

    public void error(int line, String messageKey) {
        report(line, "", propertiesReader.getString(messageKey));
    }

    private void report(int line, String where, String message) {
        System.err.printf(propertiesReader.getString("error_template"), line, where, message);
    }
}
