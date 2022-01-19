package interpreter;

import core.SpaceC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpretationTests {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final ByteArrayOutputStream error = new ByteArrayOutputStream();
    private final PrintStream systemOut = System.out;
    private final PrintStream systemError = System.err;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(output));
        System.setErr(new PrintStream(error));
    }

    @AfterEach
    void restoration() {
        System.setOut(systemOut);
        System.setErr(systemError);
    }

    @Test
    void basicArithmeticTest() throws IOException {
        SpaceC.main(new String[]{
                "/home/arttu/ohjelmistoprojektit/SpaceC/src/test/resources/interpreter/arithmetic.space"});
        assertEquals("12\n4\n-4\n32\n2\n4096\n", output.toString());
    }

    @Test
    void variableDeclarationsTest() throws IOException {
        SpaceC.main(new String[]{
            "/home/arttu/ohjelmistoprojektit/SpaceC/src/test/resources/interpreter/variableDeclarations.space"});
        assertEquals("2\n3\n", output.toString());
    }

    @Test
    void scopesTest() throws IOException {
        SpaceC.main(new String[]{
            "/home/arttu/ohjelmistoprojektit/SpaceC/src/test/resources/interpreter/scopes.space"});
        assertEquals("5\n2\n", output.toString());
    }

}
