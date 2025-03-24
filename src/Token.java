/**
 * @author mzhang27
 * @since 2025/3/13
 * 
 * token 类型的设计
 */
public class Token {
	/**
	 * token 类型
	 * 字符序列按顺序切块儿，有多少类的块儿
	 */
    public String type;
    
    /**
     * 每个 token 的 value 就是它的字面
     */
    public String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
}