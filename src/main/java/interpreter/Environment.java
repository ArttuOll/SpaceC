package interpreter;

import lexer.Token;
import utils.PropertiesReader;

import java.util.HashMap;

public class Environment {

    private final HashMap<String, Object> values = new HashMap<>();
    private final PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");
    private final Environment enclosingEnvironment;

    public Environment() {
        this.enclosingEnvironment = null;
    }

    public Environment(Environment environment) {
        this.enclosingEnvironment = environment;
    }

    public Object get(Token name) {
        String key = name.lexeme();
        if (values.containsKey(key)) {
            return values.get(key);
        }

        // If the current scope does not contain the variable, recursively check the enclosing ones.
        if (enclosingEnvironment != null) {
            return enclosingEnvironment.get(name);
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

        // If the current scope does not contain the variable, recursively check the enclosing ones.
        if (enclosingEnvironment != null) {
            enclosingEnvironment.assign(name, value);
            return;
        }

        define(key, value);
    }

    private void define(String name, Object value) {
        values.put(name, value);
    }
}
