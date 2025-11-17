package lexical;

import util.TokenType;

public class Token {
    private TokenType type;
    private String text;
    private int line;
    private int column;

    public Token(TokenType type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Token [type=" + type + ", text='" + text + "'" + ", line=" + line + ", column=" + column + "]";
    }

}
