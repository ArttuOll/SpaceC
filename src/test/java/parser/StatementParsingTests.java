package parser;

import org.junit.jupiter.api.Test;
import parser.expression.LiteralExpression;
import parser.statement.PrintStatement;
import parser.statement.Statement;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static parser.TestUtils.initializeParser;
import static utils.TestTokens.*;

public class StatementParsingTests {

    @Test
    void parsesPrintStatements() {
        Parser parser = initializeParser(printToken, twoToken, semicolonToken, eofToken);

        List<Statement> actual = parser.parse();
        List<PrintStatement> expected = List.of(new PrintStatement(new LiteralExpression(twoToken.literal())));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
