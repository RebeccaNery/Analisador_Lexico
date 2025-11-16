package mini_compiler;

import exceptions.SyntacticException;
import lexical.Scanner;
import lexical.Token;
import syntactic.Parser;

/*
GRUPO:
* Anita Alves Donato
* Ana Beatriz Cavalcanti
* Ruan Vitor Silva
* Rebecca Nery
*/

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner("programa.mc");

        try {
            Parser parser = new Parser(sc);
            parser.programa();
            System.out.println("Compilation successful");
        } catch (SyntacticException e) {
            System.out.println("Syntactic error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
