/**
 * @author mzhang27
 * @since 2025/3/13
 * 
 * token 类型的设计
 */
public class Token {
    public String type;
    public String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
}