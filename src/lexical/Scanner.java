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
            System.out.println("DEBUG: Leu o caractere: " + currentChar + " | Estado atual: " + state);
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
                        state = 5;
                        System.out.println("DEBUG: Transição para o estado 5 (MATH_OPERATOR)");
                    } else if (isRelOperator(currentChar)) {
                        content += currentChar;
                        state = 6;
                        System.out.println("DEBUG: Transição para o estado 6 (REL_OPERATOR)");
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
                        state = 2;
                        System.out.println("DEBUG: leu operador matemático, retornando para finalizar IDENTIFIER)");
                    } else {
                        state = 2;
                        System.out.println("DEBUG: Transição para o estado 2 (FINALIZA IDENTIFIER)");
                    }
                    break;
                case 2:
                    back();
                    System.out.println("DEBUG: Retornando TOKEN: IDENTIFIER | Valor: " + content);
                    return new Token(TokenType.IDENTIFIER, content);
                case 3:
                    if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 3;
                    } else {
                        state = 4;
                        System.out.println("DEBUG: Transição para o estado 4 (FINALIZA NUMBER)");
                    }
                    break;
                case 4:
                    back();
                    return new Token(TokenType.NUMBER, content);
                case 5:
                    back();
                    return new Token(TokenType.MATH_OPERATOR, content); //tratar esse erro
                case 6:
//                    if (currentChar == '=') {
//                        content += currentChar;
//                        return new Token(TokenType.REL_OPERATOR, content);
//                    } else {
//                        back();
//                        if (content.equals("=")) {
//                            return new Token(TokenType.ASSIGNMENT, content);
//                        } else {
//                            return new Token(TokenType.REL_OPERATOR, content);
//                        }
//
//                    }
                    if (currentChar == '>') {
    
                    }
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
