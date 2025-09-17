package lexical;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import util.TokenType;

public class Scanner {
    private int state;
    private char[] sourceCode;
    private int pos;

    // >>> TABELA DE PALAVRAS RESERVADAS <<<
    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("int", TokenType.RESERVED_WORD);
        reservedWords.put("float", TokenType.RESERVED_WORD);
        reservedWords.put("print", TokenType.RESERVED_WORD);
        reservedWords.put("if", TokenType.RESERVED_WORD);
        reservedWords.put("else", TokenType.RESERVED_WORD);
    }

    public Scanner(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            sourceCode = content.toCharArray();
            pos = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() {
        char currentChar;
        String content = "";
        state = 0;
        System.out.println("\n--- Chamando nextToken() ---");

        while (true) {
            if (isEoF()) {
                return null;
            }
            currentChar = nextChar();
            System.out.println("DEBUG: Estado atual: " + state + " | Leu o caractere: " + currentChar);

            switch (state) {
                case 0:
                    if (isWhitespace(currentChar)) {
                        System.out.println("DEBUG: Caractere é whitespace. Ignorando.");
                        continue;
                    }
                    if (isLetter(currentChar)) {
                        content += currentChar;
                        state = 1;
                        System.out.println("DEBUG: Transição para o estado 1 (IDENTIFIER)");
                    } else if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 3;
                        System.out.println("DEBUG: Transição para o estado 3 (NUMBER)");
                    } else if (isMathOperator(currentChar)) {
                        content += currentChar;
                        state = 7;
                        System.out.println("DEBUG: Transição para o estado 7(COMENTARIO DE UMA LINHA)");
                    } else if (isRelOperator(currentChar)) {
                        content += currentChar;
                        state = 5;
                        System.out.println("DEBUG: Transição para o estado 5 (REL_OPERATOR)");
                    } else if (isParenthesis(currentChar)) {
                        content += currentChar;
                        if (currentChar == '(') {
                            System.out.println("DEBUG: Retornando TOKEN: PARÊNTESIS ESQUERDO | Valor: " + content);
                        } else {
                            System.out.println("DEBUG: Retornando TOKEN: PARÊNTESIS DIREITO | Valor: " + content);
                        }
                        return new Token(TokenType.PARENTHESIS, content);
                    } else if (isPoint(currentChar)) {
                        content += currentChar;
                        state = 6;
                        System.out.println("DEBUG: ENCONTREI UM PONTO | Valor: " + currentChar);
                    } else {
                        System.out.println("DEBUG: Caractere inválido: " + currentChar);

                    }
                    break;
                case 1:
                    if (isLetter(currentChar) || isDigit(currentChar) || isUnderline(currentChar)) {
                        content += currentChar;
                        state = 1;
                        System.out.println("DEBUG: CONTINUA NO ESTADO 1 (IDENTIFIER)");
                    } else {
                        back();
                        TokenType finalType = reservedWords.getOrDefault(content, TokenType.IDENTIFIER);
                        System.out.println("DEBUG: Retornando TOKEN:" + finalType + " | Valor: " + content);
                        return new Token(finalType, content);
                    }
                    break;
                case 2:
                    back();
                    System.out.println("DEBUG: Retornando TOKEN: IDENTIFIER | Valor: " + content);
                    return new Token(TokenType.IDENTIFIER, content);
                case 3:
                    if (isPoint(currentChar)) {
                        content += currentChar;
                        state = 6;
                        System.out.println("DEBUG: ENCONTREI UM PONTO | Valor: " + currentChar);
                    } else if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 3;
                    } else {
                        back();
                        System.out.println("DEBUG: Transição para o estado 4 desnecessária(FINALIZA NUMBER)");
                        return new Token(TokenType.NUMBER, content);
                    }
                    break;
                case 5:
                    if (currentChar == '=') {
                        content += currentChar;
                        return new Token(TokenType.REL_OPERATOR, content); //é ==
                    } else {
                        back();
                        if (content.equals("=")) {
                            return new Token(TokenType.ASSIGNMENT, content);
                        } else {
                            return new Token(TokenType.REL_OPERATOR, content);
                        }
                    }
                case 6:
                    if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 6;
                    } else if (isPoint(currentChar)) {
                        content += currentChar;
                        System.out.println("DEBUG: não pode haver dois pontos! | Leu o caractere: " + currentChar + " | Valor atual: " + content);
                        return null; // Erro léxico
                    } else {
                        back();
                        System.out.println("DEBUG: Retornando TOKEN: NUMBER decimal | Valor: " + content);
                        return new Token(TokenType.NUMBER, content);
                    }
                    break;
                case 7:
                    if (isBar(currentChar)) {
                        content += currentChar;
                        state = 8;
                        System.out.println("DEBUG: Duas barras ==> início do comentário de uma linha | Valor: " + content);
                    } else {
                        back();
                        return new Token(TokenType.MATH_OPERATOR, content);
                    }
                    break;
                case 8:
                    if (currentChar == '\n' || currentChar == '\r') {
                        back();
                        System.out.println("DEBUG: Fim do comentário de uma linha! | Valor: " + content);
                        return new Token(TokenType.ONE_LINE_COMMENT, content);
                    } else {
                        state = 8;
                        content += currentChar;
                        System.out.println("DEBUG: Duas barras ==> comentário de uma linha! | Valor: " + content);
                    }
                    break;

            }//switch
        }
    }


    private boolean isWhitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isUnderline(char c) {
        return c == '_';
    }

    private boolean isMathOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isRelOperator(char c) {
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    private boolean isParenthesis(char c) {
        return c == '(' || c == ')';
    }

    private boolean isPoint(char c) {
        return c == '.';
    }

    private boolean isBar(char c) {
        return (c == '/');
    }

    private char nextChar() {
        return sourceCode[pos++];
    }

    private void back() {
        pos--;
    }

    private boolean isEoF() {
        return pos >= sourceCode.length;
    }

}
