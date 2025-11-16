package util;

public enum TokenType {
    IDENTIFIER, NUMBER, REL_OPERATOR, MATH_OPERATOR, ASSIGNMENT, LEFT_PARENTHESIS, RIGHT_PARENTHESIS, RESERVED_WORD, RESERVED_WORD_FN, RESERVED_WORD_MAIN,
    RESERVED_WORD_LET, RESERVED_WORD_MUT, RESERVED_WORD_READ, RESERVED_WORD_PRINT, RESERVED_WORD_IF, RESERVED_WORD_WHILE, RESERVED_WORD_INT, RESERVED_WORD_FLOAT, STRING, SEMICOLON, TWOPOINTS, LEFT_BRACE, RIGHT_BRACE, EXCLAMATION;
}

/*CRIAR IDENTIFICADORES
 * palavra reservada --> fn ✅
 * palavra reservada --> main ✅
 * palavra reservada --> let ✅
 * palavra reservada --> mut ✅
 * palavra reservada --> read ✅
 * parêntesis --> separar direito e esquerdo ✅
 * ponto e vírgula ✅
 * dois pontos ✅
 * chaves  --> bloco ✅
 *
 *
 *
 * */