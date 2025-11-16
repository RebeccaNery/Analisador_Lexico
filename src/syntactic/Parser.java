package syntactic;

import exceptions.SyntacticException;
import lexical.Scanner;
import lexical.Token;
import util.TokenType;

public class Parser {

    private Scanner scanner;
    private Token token;

    public Parser(Scanner scanner) throws Exception {
        this.scanner = scanner;
        token = this.scanner.nextToken();
    }

    public void programa() throws Exception {
        if (token.getType() == TokenType.RESERVED_WORD_FN) {
            token = scanner.nextToken();
            if (token.getType() == TokenType.RESERVED_WORD_MAIN) {
                token = scanner.nextToken();
                if (token.getType() == TokenType.LEFT_PARENTHESIS) {
                    token = scanner.nextToken();
                    if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                        token = scanner.nextToken();
                        bloco();
                    } else {
                        throw new SyntacticException("Expected ')', found " + token.getType() + "(" + token.getText() + ")");
                    }
                } else {
                    throw new SyntacticException("Expected '(', found " + token.getType() + "(" + token.getText() + ")");
                }
            } else {
                throw new SyntacticException("Expected 'main', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected 'fn', found " + token.getType() + "(" + token.getText() + ")");
        }

    }

    public void bloco() throws Exception {
        if (token.getType() == TokenType.LEFT_BRACE) {
            token = scanner.nextToken();
            listaComandos();
            if (token.getType() == TokenType.RIGHT_BRACE) {
                token = scanner.nextToken();
            } else {
                throw new SyntacticException("Expected '}', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected '{', found " + token.getType() + "(" + token.getText() + ")");
        }
    }

    public void listaComandos() throws Exception {
        comando();
        if (token != null && token.getType() != TokenType.RIGHT_BRACE) {
            listaComandos();
        }
    }

    public void comando() throws Exception {
        if (token.getType() == TokenType.RESERVED_WORD_LET) {
            declaracao();
        } else if (token.getType() == TokenType.IDENTIFIER) {
            //atribuicao();
        } else if (token.getType() == TokenType.RESERVED_WORD_READ) {
            leitura();
        } else if (token.getType() == TokenType.RESERVED_WORD_PRINT) {
            //escrita();
        } else if (token.getType() == TokenType.RESERVED_WORD_IF) {
            //condicional();
        } else if (token.getType() == TokenType.RESERVED_WORD_WHILE) {
            //repeticao();
        } else if (token.getType() == TokenType.LEFT_BRACE) {
            bloco();
        }

    }

    public void declaracao() throws SyntacticException {
        if (token.getType() == TokenType.RESERVED_WORD_LET) {
            token = scanner.nextToken();
            mutavel();
            if (token.getType() == TokenType.IDENTIFIER) {
                token = scanner.nextToken();
                if (token.getType() == TokenType.TWOPOINTS) {
                    token = scanner.nextToken();
                    tipo();
                    if (token.getType() == TokenType.SEMICOLON) {
                        token = scanner.nextToken();
                    } else {
                        throw new SyntacticException("Expected ';', found " + token.getType() + "(" + token.getText() + ")");
                    }
                }
            } else {
                throw new SyntacticException("Expected IDENTIFIER, found " + token.getType() + "(" + token.getText() + ")");
            }
        }
    }
    public void condicional() throws SyntacticException{
        if(token !=null) {
            if (token.getType() == TokenType.RESERVED_WORD_IF) {
                token = scanner.nextToken();
                expressaoRelacional();
                bloco();
                condicional_();

            }
        }
    }

    public void leitura() throws Exception {
        if (token.getType() != TokenType.RESERVED_WORD_READ) {
            throw new SyntacticException("Expected 'read', found " + token.getType() + "(" + token.getText() + ")");
        }
        token = scanner.nextToken(); //consome read e vai para a proxima se tiver

        if (token.getType() != TokenType.LEFT_PARENTHESIS) {
            throw new SyntacticException("Expected '(', found " + token.getType() + "(" + token.getText() + ")");
        }
        token = scanner.nextToken(); // consume '('

        if (token.getType() != TokenType.IDENTIFIER) {
            throw new SyntacticException("Expected IDENTIFIER, found " + token.getType() + "(" + token.getText() + ")");
        }
        token = scanner.nextToken(); // consume ID

        if (token.getType() != TokenType.RIGHT_PARENTHESIS) {
            throw new SyntacticException("Expected ')', found " + token.getType() + "(" + token.getText() + ")");
        }
        token = scanner.nextToken(); // consume ')'
    }





    public void mutavel() {
        if (token != null) {
            if (token.getType() == TokenType.RESERVED_WORD_MUT) {
                token = scanner.nextToken();
            }
        }
    }

    public void tipo() {
        if (token != null) {
            if (token.getType() == TokenType.IDENTIFIER) {
            }
        }
    }

    public void T() throws Exception, SyntacticException {
        if (token == null) {
            return;
        } else if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.NUMBER) {
            token = scanner.nextToken();
        } else {
            throw new SyntacticException("Expected ID or Number, found " + token.getType() + "(" + token.getText() + ")");
        }

    }

    public void OP() throws Exception, SyntacticException {
        if (token.getType() == TokenType.MATH_OPERATOR) {
            token = scanner.nextToken();
        } else {
            throw new SyntacticException("Expected MATH_OPERATOR, found " + token.getType() + "(" + token.getText() + ")");
        }
    }

//    public void E() throws Exception {
//        T();
//        El();
//    }
//
//    public void El() throws Exception {
//        if (token != null) {
//            OP();
//            T();
//            El();
//        }
//    }
//
//    public void T() throws Exception, SyntacticException {
//        if (token == null) {
//            return;
//        } else if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.NUMBER) {
//            token = scanner.nextToken();
//        } else {
//            throw new SyntacticException("Expected ID or Number, found " + token.getType() + "(" + token.getText() + ")");
//        }
//
//    }
//
//    public void OP() throws Exception, SyntacticException {
//        if (token.getType() == TokenType.MATH_OPERATOR) {
//            token = scanner.nextToken();
//        } else {
//            throw new SyntacticException("Expected MATH_OPERATOR, found " + token.getType() + "(" + token.getText() + ")");
//        }
//    }


}
