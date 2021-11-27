package interpreter;

import java.util.HashMap;
import lexer.Token;
import utils.PropertiesReader;

public class Environment {

    private final HashMap<String, Object> values = new HashMap<>();
    private final PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        String key = name.lexeme();
        if (values.containsKey(key)) {
            return values.get(key);
        }

        throw new RuntimeError(
            name,
            propertiesReader.getString("interpreter_error_undefined_variable"),
            key
        );
    }

    public void assign(Token name, Object value) {
        String key = name.lexeme();
        if (values.containsKey(key)) {
            values.put(key, value);
            return;
        }

        throw new RuntimeError(name, "interpreter_error_undefined_variable", name.lexeme());
    }
}
