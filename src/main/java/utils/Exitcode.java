package utils;

/**
 * Exitcodes based on Unix sysexit.h, viewable here: https://www.freebsd.org/cgi/man.cgi?query=sysexits&apropos=0&sektion=0&manpath=FreeBSD+4.3-RELEASE&format=html
 */
public enum Exitcode {
    /**
     * Indicates incorrect usage of the program.
     */
    EX_USAGE(64),
    /**
     * Indicates incorrect input data, in this case, source code.
     */
    EX_DATAERR(65),
    /**
     * Indicates internal software error, not related to OS.
     */
    EX_SOFTWARE(70);

    public final int value;

    Exitcode(int value) {
        this.value = value;
    }
}
