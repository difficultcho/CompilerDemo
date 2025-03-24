/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.*;
import java.util.regex.*;

class C4 {

    static Token tokenizeCharacter(String type, String value, String input, int current) {
        return (value.equals(String.valueOf(input.charAt(current)))) ? new Token(type, value) : null;
    }

    static Token tokenizeParenOpen(String input, int current) {
        return tokenizeCharacter("paren", "(", input, current);
    }

    static Token tokenizeParenClose(String input, int current) {
        return tokenizeCharacter("paren", ")", input, current);
    }

    static Token tokenizePattern(String type, Pattern pattern, String input, int current) {
        char charAtCurrent = input.charAt(current);
        String charAsString = String.valueOf(charAtCurrent);
        if (pattern.matcher(charAsString).find()) {
            StringBuilder value = new StringBuilder();
            int consumedChars = 0;
            while (pattern.matcher(charAsString).find()) {
                value.append(charAtCurrent);
                consumedChars++;
                if (current + consumedChars >= input.length()) break;
                charAtCurrent = input.charAt(current + consumedChars);
                charAsString = String.valueOf(charAtCurrent);
            }
            return new Token(type, value.toString());
        }
        return null;
    }

    static Token tokenizeNumber(String input, int current) {
        return tokenizePattern("number", Pattern.compile("[0-9]"), input, current);
    }

    static Token tokenizeName(String input, int current) {
        return tokenizePattern("name", Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE), input, current);
    }

    static Token tokenizeString(String input, int current) {
        if (input.charAt(current) == '"') {
            StringBuilder value = new StringBuilder();
            int consumedChars = 1;
            char charAtCurrent = input.charAt(current + consumedChars);
            while (charAtCurrent != '"') {
                if (current + consumedChars >= input.length()) {
                    throw new IllegalArgumentException("unterminated string");
                }
                value.append(charAtCurrent);
                consumedChars++;
                charAtCurrent = input.charAt(current + consumedChars);
            }
            return new Token("string", value.toString());
        }
        return null;
    }

    static Token skipWhiteSpace(String input, int current) {
        return (Character.isWhitespace(input.charAt(current))) ? new Token(null, null) : null;
    }

    static List<Token> tokenizer(String input) {
        List<Token> tokens = new ArrayList<>();
        int current = 0;
        while (current < input.length()) {
            Token token = null;
            if (skipWhiteSpace(input, current) != null) {
                current++;
                continue;
            }
            if ((token = tokenizeParenOpen(input, current)) != null
                    || (token = tokenizeParenClose(input, current)) != null
                    || (token = tokenizeString(input, current)) != null
                    || (token = tokenizeNumber(input, current)) != null
                    || (token = tokenizeName(input, current)) != null) {
                tokens.add(token);
                current++;
            } else {
                throw new IllegalArgumentException("I don't know what this character is: " + input.charAt(current));
            }
        }
        return tokens;
    }

    static class ASTNode {
        String type;
        String value;
        List<ASTNode> params;

        ASTNode(String type, String value) {
            this.type = type;
            this.value = value;
            this.params = new ArrayList<>();
        }

        ASTNode(String type) {
            this.type = type;
            this.params = new ArrayList<>();
        }
    }

    static ASTNode parseToken(List<Token> tokens, int[] current) {
        Token token = tokens.get(current[0]);
        if (token.type.equals("number")) {
            current[0]++;
            return new ASTNode("NumberLiteral", token.value);
        } else if (token.type.equals("string")) {
            current[0]++;
            return new ASTNode("StringLiteral", token.value);
        } else if (token.type.equals("paren") && token.value.equals("(")) {
            return parseExpression(tokens, current);
        } else {
            throw new IllegalArgumentException("Unknown token type: " + token.type);
        }
    }

    static ASTNode parseExpression(List<Token> tokens, int[] current) {
        Token token = tokens.get(++current[0]);
        ASTNode node = new ASTNode("CallExpression", token.value);
        token = tokens.get(++current[0]);
        while (!(token.type.equals("paren") && token.value.equals(")"))) {
            node.params.add(parseToken(tokens, current));
            token = tokens.get(current[0]);
        }
        current[0]++;
        return node;
    }

    static ASTNode parseProgram(List<Token> tokens) {
        int[] current = {0};
        ASTNode ast = new ASTNode("Program");
        while (current[0] < tokens.size()) {
            ast.params.add(parseToken(tokens, current));
        }
        return ast;
    }

    static String emitNumber(ASTNode node) {
        return node.value;
    }

    static String emitString(ASTNode node) {
        return "\"" + node.value + "\"";
    }

    static String emitExpression(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.value).append("(");
        for (int i = 0; i < node.params.size(); i++) {
            sb.append(emitter(node.params.get(i)));
            if (i < node.params.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    static String emitProgram(ASTNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode exp : node.params) {
            sb.append(emitter(exp)).append(";\n");
        }
        return sb.toString();
    }

    static String emitter(ASTNode node) {
        switch (node.type) {
            case "Program":
                return emitProgram(node);
            case "CallExpression":
                return emitExpression(node);
            case "NumberLiteral":
                return emitNumber(node);
            case "StringLiteral":
                return emitString(node);
            default:
                throw new IllegalArgumentException("Unknown node type: " + node.type);
        }
    }

    static String compile(String input) {
        List<Token> tokens = tokenizer(input);

        tokens = new ArrayList<>();
        tokens.add(new Token("paren", "("));
        tokens.add(new Token("name", "add"));
        tokens.add(new Token("number", "2"));
        tokens.add(new Token("number", "1"));
        tokens.add(new Token("paren", ")"));

        ASTNode ast = parseProgram(tokens);
        return emitter(ast);
    }

    public static void main(String[] args) {
        String input = "(add 2 (subtract 4 2))";
        String output = compile(input);
        System.out.println(output);
    }
}