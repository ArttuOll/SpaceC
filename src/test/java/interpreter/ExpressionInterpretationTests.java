package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.SpaceC;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpressionInterpretationTests {

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
        assertEquals("12\n4\n-4\n32\n2\n", output.toString());
    }

}
