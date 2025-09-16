package lexical;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import util.TokenType;

public class Scanner {
    private int state;
    private char[] sourceCode;
    private int pos;

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
                        System.out.println("DEBUG: Transição para o estado 5 desnecessária(MATH_OPERATOR)");
                        return new Token(TokenType.MATH_OPERATOR, content);
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
                    } else {
                        System.out.println("DEBUG: Caractere inválido: " + currentChar);

                    }
                    break;
                case 1:
                    if (isLetter(currentChar) || isDigit(currentChar) || isUnderline(currentChar)) {
                        content += currentChar;
                        state = 1;
                        System.out.println("DEBUG: CONTINUA NO ESTADO 1 (IDENTIFIER)");
                    } else if (isMathOperator(currentChar)) {
                        back();
                        System.out.println("DEBUG: Retornando TOKEN: IDENTIFIER | Valor: " + content);
                        return new Token(TokenType.IDENTIFIER, content);
//
                    } else {
                        back();
                        System.out.println("DEBUG: Retornando TOKEN: IDENTIFIER | Valor: " + content);
                        return new Token(TokenType.IDENTIFIER, content);
//
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

            }
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
