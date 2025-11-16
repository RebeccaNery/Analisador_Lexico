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
    private int line;
    private int column;

    // >>> TABELA DE PALAVRAS RESERVADAS <<<
    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("i32", TokenType.RESERVED_WORD_INT);
        reservedWords.put("f64", TokenType.RESERVED_WORD_FLOAT);
        reservedWords.put("print", TokenType.RESERVED_WORD_PRINT);
        reservedWords.put("if", TokenType.RESERVED_WORD_IF);
        reservedWords.put("else", TokenType.RESERVED_WORD);
        reservedWords.put("fn", TokenType.RESERVED_WORD_FN);
        reservedWords.put("main", TokenType.RESERVED_WORD_MAIN);
        reservedWords.put("let", TokenType.RESERVED_WORD_LET);
        reservedWords.put("mut", TokenType.RESERVED_WORD_MUT);
        reservedWords.put("read", TokenType.RESERVED_WORD_READ);
        reservedWords.put("while", TokenType.RESERVED_WORD_WHILE);
    }

    public Scanner(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            sourceCode = content.toCharArray();
            pos = 0;
            line = 1;
            column = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() {
        char currentChar;
        String content = "";
        state = 0;
        //System.out.println("\n--- Chamando nextToken() ---");

        while (true) {

            int errorLine = line;
            int errorColumn = column;

            currentChar = nextChar();
            //System.out.println("DEBUG: Estado atual: " + state + " | Leu o caractere: " + currentChar);

            switch (state) {
                case 0:
                    if (isWhitespace(currentChar)) {
                        //System.out.println("DEBUG: Caractere é whitespace. Ignorando.");
                        continue;
                    }
                    if (currentChar == '\0') {
                        return null;
                    }
                    if (isLetter(currentChar) || isUnderline(currentChar)) {
                        content += currentChar;
                        state = 1;
                        //System.out.println("DEBUG: Transição para o estado 1 (IDENTIFIER)");
                    } else if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 2;
                        //System.out.println("DEBUG: Transição para o estado 2 (NUMBER)");
                    } else if (isBar(currentChar)) {
                        content += currentChar;
                        state = 5;
                        //System.out.println("DEBUG: Transição para o estado 5 (COMENTARIO DE UMA LINHA, MULTILINHA OU MATH_OPERATOR)");
                    } else if (isMathOperator(currentChar)) { //se entrou nesse else if, é porque não é barra, então automaticamente é um operador matemático MATH_OPERATOR
                        content += currentChar;
                        return new Token(TokenType.MATH_OPERATOR, content);
                    } else if (isRelOperator(currentChar)) { //pode ser >, <, = ou !
                        content += currentChar;
                        state = 4;
                        //System.out.println("DEBUG: Transição para o estado 4 (REL_OPERATOR)");
                    } else if (isParenthesis(currentChar)) {
                        content += currentChar;
                        if (currentChar == '(') {
                            //System.out.println("DEBUG: Retornando TOKEN: PARÊNTESIS ESQUERDO | Valor: " + content);
                            return new Token(TokenType.LEFT_PARENTHESIS, content);
                        } else {
                            //System.out.println("DEBUG: Retornando TOKEN: PARÊNTESIS DIREITO | Valor: " + content);
                            return new Token(TokenType.RIGHT_PARENTHESIS, content);
                        }

                    } else if (isPoint(currentChar)) { // verifica os casos de número decimal tipo .950 ou .48 (que é aceito), mas vai pro estado 3 verificar se é um decimal mesmo ou só um ponto aleatório
                        content += currentChar;
                        state = 3;
                        //System.out.println("DEBUG: Encontrei um ponto! Transição para o estado 3 | Valor: " + currentChar);
                    } else if (currentChar == ';') {
                        return new Token(TokenType.SEMICOLON, content);

                    } else if (currentChar == ':') {
                        return new Token(TokenType.TWOPOINTS, content);

                    } else if (currentChar == '{') {
                        return new Token(TokenType.LEFT_BRACE, content);

                    } else if (currentChar == '}') {
                        return new Token(TokenType.RIGHT_BRACE, content);
                    } else if (currentChar == '!') {
                        return new Token(TokenType.EXCLAMATION, content);
                    } else if (currentChar == '"') {
                        content += currentChar;
                        state = 9;
                    } else {
                        //System.out.println("DEBUG: Caractere inválido: " + currentChar);
                        String errorMessage = String.format(
                                "Lexical Error on line %d, column %d: Invalid character '%c'",
                                errorLine, errorColumn, currentChar
                        );
                        throw new RuntimeException(errorMessage);
                    }
                    break;
                case 1: //IDENTIFIER e RESERVED_WORD
                    if (isLetter(currentChar) || isDigit(currentChar) || isUnderline(currentChar)) {
                        content += currentChar;
                        state = 1;
                        //System.out.println("DEBUG: Continua no Estado 1 (IDENTIFIER)");
                    } else {
                        if (currentChar != '\0') {
                            back();
                        }
                        TokenType finalType = reservedWords.getOrDefault(content, TokenType.IDENTIFIER);
                        //System.out.println("DEBUG: Retornando TOKEN:" + finalType + " | Valor: " + content);
                        return new Token(finalType, content);
                    }
                    break;
                case 2: //NUMBER
                    if (isPoint(currentChar)) {
                        content += currentChar;
                        state = 3;
                        //System.out.println("DEBUG: ENCONTREI UM PONTO | Valor: " + currentChar);
                    } else if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 2;
                    } else {
                        if (currentChar != '\0') {
                            back();
                        }
                        return new Token(TokenType.NUMBER, content);
                    }
                    break;

                case 3: // NUMBER - número decimal
                    if (isDigit(currentChar)) {
                        content += currentChar;
                        state = 3;
                    } else if (isPoint(currentChar)) {
                        content += currentChar;
                        System.out.println("DEBUG: não pode haver dois pontos! | Leu o caractere: " + currentChar + " | Valor atual: " + content);
                        String errorMessage = String.format(
                                "Lexical Error on line %d, column %d: Invalid character '%c'",
                                errorLine, errorColumn, currentChar
                        );
                        throw new RuntimeException(errorMessage);
                    } else {
                        if (content.endsWith(".")) {
                            String errorMessage = String.format(
                                    "Lexical Error on line %d, column %d: Number cannot end with a decimal point '%s'",
                                    errorLine, errorColumn, content
                            );
                            throw new RuntimeException(errorMessage);
                        }
                        if (currentChar != '\0') {
                            back();
                        }
                        //System.out.println("DEBUG: Retornando TOKEN: NUMBER decimal | Valor: " + content);
                        return new Token(TokenType.NUMBER, content);
                    }
                    break;
                case 4: //REL_OPERATOR ou ASSIGNMENT
                    if (currentChar == '=') {
                        content += currentChar;
                        return new Token(TokenType.REL_OPERATOR, content); //é ==, >=, <=, !=
                    } else {
                        if (currentChar != '\0') {
                            back();
                        }
                        if (content.equals("=")) {
                            return new Token(TokenType.ASSIGNMENT, content);
                        } else {
                            return new Token(TokenType.REL_OPERATOR, content);
                        }
                    }
                case 5:
                    if (isBar(currentChar)) {
                        content += currentChar;
                        state = 6;
                        //System.out.println("DEBUG: Duas barras ==> início do comentário de uma linha | Valor: " + content);
                    } else if (isAsterisk(currentChar)) {
                        content += currentChar;
                        //System.out.println("DEBUG: Barra seguida de asterisco ==> comentário multilinha --> Indo para o Estado 9 | Valor: " + content);
                        state = 7;
                    } else {
                        if (currentChar != '\0') {
                            back();
                        }

                        return new Token(TokenType.MATH_OPERATOR, content);
                    }
                    break;
                case 6:
                    if (currentChar == '\0') {
                        //System.out.println("DEBUG: Fim do comentário de uma linha! | Valor: " + content);
                        return null;
                    }
                    if (currentChar == '\n' || currentChar == '\r') {
                        //System.out.println("DEBUG: Fim do comentário de uma linha! | Valor: " + content);
                        state = 0;
                        content = "";
                        continue;
                    } else {
                        state = 6;
                        content += currentChar;
                        //System.out.println("DEBUG: Duas barras ==> comentário de uma linha! | Valor: " + content);
                    }
                    break;
                case 7:
                    if (currentChar == '\0') {
                        String errorMessage = String.format(
                                "Lexical Error on line %d, column %d: Unterminated multi-line comment",
                                errorLine, errorColumn
                        );
                        throw new RuntimeException(errorMessage);
                    }
                    if (currentChar == '*') {
                        content += currentChar;
                        state = 8;
                        //System.out.println("Encontrei um ASTERISCO --> Indo para o estado 8 | Valor: " + content);
                    } else {
                        state = 7;
                        content += currentChar;
                        //System.out.println("DEBUG: Estado 7 | Dentro do comentário multilinha! | Valor: " + content);
                    }
                    break;
                case 8:
                    if (currentChar == '\0') {
                        String errorMessage = String.format(
                                "Lexical Error on line %d, column %d: Unterminated multi-line comment",
                                errorLine, errorColumn
                        );
                        throw new RuntimeException(errorMessage);
                    }
                    if (currentChar == '/') {
                        content += currentChar;
                        //System.out.println("DEBUG: Fim do comentário multilinha! | Valor: " + content);
                        state = 0;
                        content = "";
                        continue;
                    } else {
                        state = 7;
                        content += currentChar;
                        //System.out.println("DEBUG: Estado 8 -> voltando pro Estado 7 | Dentro do comentário multilinha! | Valor: " + content);
                    }
                    break;
                case 9:
                    if (currentChar == '"') {
                        content += currentChar;
                        return new Token(TokenType.STRING, content);
                    } else if (currentChar == '\0') {
                        String errorMessage = String.format(
                                "Lexical Error on line %d, column %d: Unclosed string",
                                errorLine, errorColumn
                        );
                        throw new RuntimeException(errorMessage);
                    } else {
                        content += currentChar;
                        state = 9;
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

    private boolean isAsterisk(char c) {
        return (c == '*');
    }

    private char nextChar() {
        if (isEoF()) {
            //System.out.println("DEBUG: Fim do arquivo alcançado. Retornando '\\0'.");
            return '\0';
        }
        char currentChar = sourceCode[pos];

        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        pos++;
        return currentChar;
    }

    private void back() {
        if (pos <= 0) {
            return;
        }

        pos--;
        if (sourceCode[pos] == '\n') {
            line--;
            int tempPos = -1;
            for (int i = pos - 1; i >= 0; i--) {
                if (sourceCode[i] == '\n') {
                    tempPos = i;
                    break;
                }

            }
            column = pos - tempPos;
        } else {
            column--;
        }
    }

    private boolean isEoF() {
        return pos >= sourceCode.length;
    }

    private boolean isInvalidChar(char c) {
        return (c == '@' || c == '#' || c == '$' || c == '%' || c == '^' || c == '&' || c == '~' || c == '`' || c == '\\' || c == '|' || c == ';' || c == ':' || c == '"' || c == '\'');
    }


}
