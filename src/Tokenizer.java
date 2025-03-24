/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer {
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int current = 0;

        while (current < input.length()) {
            char ch = input.charAt(current);

            if (ch == '(') {
                tokens.add(new Token("parenthesis", "("));
                current++;
                continue;
            }

            if (ch == ')') {
                tokens.add(new Token("parenthesis", ")"));
                current++;
                continue;
            }

            if (Character.isWhitespace(ch)) {
                current++;
                continue;
            }

            if (Character.isDigit(ch)) {
                StringBuilder value = new StringBuilder();
                while (Character.isDigit(ch)) {
                    value.append(ch);
                    current++;
                    if (current >= input.length()) break;
                    ch = input.charAt(current);
                }
                tokens.add(new Token("number", value.toString()));
                continue;
            }

            if (ch == '"') {
                StringBuilder value = new StringBuilder();
                current++;
                ch = input.charAt(current);
                while (ch != '"') {
                    value.append(ch);
                    current++;
                    ch = input.charAt(current);
                }
                current++;
                tokens.add(new Token("string", value.toString()));
                continue;
            }

            if (Character.isLetter(ch)) {
                StringBuilder value = new StringBuilder();
                while (Character.isLetter(ch)) {
                    value.append(ch);
                    current++;
                    if (current >= input.length()) break;
                    ch = input.charAt(current);
                }
                tokens.add(new Token("name", value.toString()));
                continue;
            }

            throw new RuntimeException("Unknown character: " + ch);
        }

        return tokens;
    }
}