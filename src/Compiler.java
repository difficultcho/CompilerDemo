import java.util.List;

/**
 * @author mzhang27
 * @since 2025/3/13
 */
public class Compiler {
    public String compile(String input) {
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser();
        Transformer transformer = new Transformer();
        CodeGenerator generator = new CodeGenerator();

        List<Token> tokens = tokenizer.tokenize(input);
        Node ast = parser.parse(tokens);
        Node newAst = transformer.transform(ast);
        return generator.generate(newAst);
    }
}