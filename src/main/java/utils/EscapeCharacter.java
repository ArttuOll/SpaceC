package utils;

public enum EscapeCharacter {

    NULL('\0'),
    NEWLINE('\n');

    public final char value;

    EscapeCharacter(char value) {
        this.value = value;
    }
}
