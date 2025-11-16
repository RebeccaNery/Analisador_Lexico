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
        T();
        bloco();
    }

    public void bloco() throws Exception {
        if (token != null) {
            OP();
            listaComandos();
            El();
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
