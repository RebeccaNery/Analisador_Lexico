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
            if (token == null || token.getType() != TokenType.RIGHT_BRACE) {
                // Se token é null (EOF), ou não é '}', lance a exceção
                throw new SyntacticException("Expected '}', found " +
                        (token == null ? "EOF" : token.getType() + "(" + token.getText() + ")")
                );
            }
            // Se chegamos aqui, é o '}'. Consuma e avance.
            token = scanner.nextToken();
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
            token = scanner.nextToken();
            declaracao();
        } else if (token.getType() == TokenType.IDENTIFIER) {
            token = scanner.nextToken();
            atribuicao();
        } else if (token.getType() == TokenType.RESERVED_WORD_READ) {
            token = scanner.nextToken();
            leitura();
        } else if (token.getType() == TokenType.RESERVED_WORD_PRINT) {
            token = scanner.nextToken();
            escrita();
        } else if (token.getType() == TokenType.RESERVED_WORD_IF) {
            token = scanner.nextToken();
            condicional();
        } else if (token.getType() == TokenType.RESERVED_WORD_WHILE) {
            token = scanner.nextToken();
            repeticao();
        } else if (token.getType() == TokenType.LEFT_BRACE) {
            token = scanner.nextToken();
            bloco();
        }

    }

    public void declaracao() throws SyntacticException {
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
            } else {
                throw new SyntacticException("Expected ':', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected IDENTIFIER, found " + token.getType() + "(" + token.getText() + ")");
        }

    }

    public void atribuicao() throws Exception {

        if (token.getType() == TokenType.ASSIGNMENT) {
            token = scanner.nextToken();
            expressaoAritmetica();
            if (token.getType() == TokenType.SEMICOLON) {
                token = scanner.nextToken();
            } else {
                throw new SyntacticException("Expected ';', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected '=', found " + token.getType() + "(" + token.getText() + ")");
        }

    }

    public void condicional() throws Exception {
        if (token != null) {
            //expressaoRelacional();
            bloco();
            condicional_();
        }
    }

    public void condicional_() throws Exception {
        if (token.getType() == TokenType.RESERVED_WORD_ELSE) {
            token = scanner.nextToken();
            bloco();
        } else {
            throw new SyntacticException("Expected 'else', found " + token.getType() + "(" + token.getText() + ")");
        }
    }

    public void mutavel() throws SyntacticException {
        if (token != null) {
            if (token.getType() == TokenType.RESERVED_WORD_MUT) {
                token = scanner.nextToken();
            } else {
                throw new SyntacticException("Expected 'mut', found " + token.getType() + "(" + token.getText() + ")");
            }
        }
    }

    public void tipo() throws SyntacticException {
        if (token != null) {
            if (token.getType() == TokenType.RESERVED_WORD_INT || token.getType() == TokenType.RESERVED_WORD_FLOAT) {
                token = scanner.nextToken();
            } else {
                throw new SyntacticException("Expected type INT or FLOAT, found " + token.getType() + "(" + token.getText() + ")");
            }
        }
    }

    public void leitura() throws Exception {
        if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            token = scanner.nextToken(); // consume '('
            if (token.getType() == TokenType.IDENTIFIER) {
                token = scanner.nextToken(); // consume ID
                if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                    token = scanner.nextToken(); // consume ')'
                    if (token.getType() == TokenType.SEMICOLON) {
                        token = scanner.nextToken(); // consume ';'
                    } else {
                        throw new SyntacticException("Expected ';', found " + token.getType() + "(" + token.getText() + ")");
                    }
                } else {
                    throw new SyntacticException("Expected ')', found " + token.getType() + "(" + token.getText() + ")");
                }
            } else {
                throw new SyntacticException("Expected IDENTIFIER, found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected '(', found " + token.getType() + "(" + token.getText() + ")");
        }
    }

    public void escrita() throws Exception {
        if (token.getType() == TokenType.EXCLAMATION) {
            token = scanner.nextToken(); // consome '!'
            if (token.getType() == TokenType.LEFT_PARENTHESIS) {
                token = scanner.nextToken(); // consome '('
                if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.STRING) {
                    token = scanner.nextToken(); // consome ID, ainda nao colocou CADEIA PRECISA DE AJUSTE ESSA PARTE
                    if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                        token = scanner.nextToken(); // consome ')'
                        if (token.getType() == TokenType.SEMICOLON) {
                            token = scanner.nextToken(); // consome ';'
                        } else {
                            throw new SyntacticException("Expected ';', found "
                                    + token.getType() + "(" + token.getText() + ")");
                        }
                    } else {
                        throw new SyntacticException("Expected ')', found "
                                + token.getType() + "(" + token.getText() + ")");
                    }
                } else {
                    throw new SyntacticException("Expected IDENTIFIER, found "
                            + token.getType() + "(" + token.getText() + ")");
                }
            } else {
                throw new SyntacticException("Expected '(', found "
                        + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected '!', found "
                    + token.getType() + "(" + token.getText() + ")");
        }
    }

    public void repeticao() throws Exception {
        expressaoRelacional();
        bloco();
    }

    public void expressaoAritmetica() throws Exception {
        termo();
        expressaoAritmetica_();
    }

    public void expressaoAritmetica_() throws Exception {
        if (token != null) {
            if (token.getText().equals("+") || token.getText().equals("-")) {
                termo();
                expressaoAritmetica_();
            }
        }
    }

    public void termo() throws Exception {
        fator();
        termo_();
    }

    public void fator() throws Exception {
        if (token.getType() == TokenType.NUMBER || token.getType() == TokenType.IDENTIFIER) {
            token = scanner.nextToken();
        } else if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            token = scanner.nextToken();
            expressaoAritmetica();
            if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                token = scanner.nextToken();
            } else {
                throw new SyntacticException("Expected ')', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            throw new SyntacticException("Expected NUMBER or IDENTIFIER, found " + token.getType() + "(" + token.getText() + ")");
        }
    }

    public void termo_() throws Exception {
        if (token != null) {
            if (token.getText().equals("*") || token.getText().equals("/")) {
                token = scanner.nextToken();
                fator();
                termo_();
            }
        }
    }

    public void expressaoRelacional() throws Exception { //IMCOMPLETA 🔴🔴🔴🔴🔴
        if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            token = scanner.nextToken();
            expressaoRelacional();
            if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                token = scanner.nextToken();
                expressaoRelacional_();
            } else {
                throw new SyntacticException("Expected ')', found " + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            token = scanner.nextToken();
            expressaoAritmetica();
            if (token.getType() == TokenType.REL_OPERATOR) {
                token = scanner.nextToken();
                expressaoAritmetica();
                expressaoRelacional_();
            } else {
                throw new SyntacticException("Expected REL_OPERATOR, found " + token.getType() + "(" + token.getText() + ")");
            }
        }
    }

    public void expressaoRelacional_() throws Exception {
        if (token != null) {
            operadorLogico();
            termoRelacional();
            expressaoRelacional_();
        }
    }

    public void termoRelacional() throws Exception {
        if (token.getType() == TokenType.LEFT_PARENTHESIS) {
            token = scanner.nextToken(); // consome '('
            expressaoRelacional();
            if (token.getType() == TokenType.RIGHT_PARENTHESIS) {
                token = scanner.nextToken(); // consome ')'
            } else {
                throw new SyntacticException("Expected ')', found "
                        + token.getType() + "(" + token.getText() + ")");
            }
        } else {
            expressaoAritmetica();
            if (token.getType() == TokenType.REL_OPERATOR) {
                token = scanner.nextToken(); // consome o operador relacional
            } else {
                throw new SyntacticException("Expected RELATIONAL_OPERATOR, found "
                        + token.getType() + "(" + token.getText() + ")");
            }
            expressaoAritmetica();
        }
    }

    public void operadorLogico() throws Exception {
        if (token.getType() == TokenType.LOGIC_OPERATOR || token.getType() == TokenType.EXCLAMATION) {
            token = scanner.nextToken();
        } else {
            throw new SyntacticException("Expected LOGIC_OPERATOR, found " + token.getType() + "(" + token.getText() + ")");
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
