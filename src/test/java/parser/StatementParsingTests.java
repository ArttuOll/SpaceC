package parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static parser.TestUtils.initializeParser;
import static utils.TestTokens.colonToken;
import static utils.TestTokens.eofToken;
import static utils.TestTokens.identifierToken;
import static utils.TestTokens.printToken;
import static utils.TestTokens.semicolonToken;
import static utils.TestTokens.twoToken;

import java.util.List;
import org.junit.jupiter.api.Test;
import parser.expression.LiteralExpression;
import parser.statement.PrintStatement;
import parser.statement.Statement;
import parser.statement.VariableDeclarationStatement;

public class StatementParsingTests {

    @Test
    void parsesVariableDeclarations() {
        Parser parser = initializeParser(
            identifierToken,
            colonToken,
            twoToken,
            semicolonToken,
            eofToken
        );

        List<Statement> actual = parser.parse();
        List<VariableDeclarationStatement> expected = List.of(new VariableDeclarationStatement(
            identifierToken,
            new LiteralExpression(twoToken.literal())
        ));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesPrintStatements() {
        Parser parser = initializeParser(printToken, twoToken, semicolonToken, eofToken);

        List<Statement> actual = parser.parse();
        List<PrintStatement> expected = List.of(new PrintStatement(new LiteralExpression(twoToken.literal())));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
