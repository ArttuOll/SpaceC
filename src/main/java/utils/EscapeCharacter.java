package utils;

public enum EscapeCharacter {

    NULL('\0'),
    NEWLINE('\n'),
    TAB('\t'),
    CARRIAGE_RETURN('\r');

    public final char value;

    EscapeCharacter(char value) {
        this.value = value;
    }
}
