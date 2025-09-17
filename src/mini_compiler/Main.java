package mini_compiler;

import lexical.Scanner;
import lexical.Token;

/*
GRUPO:
* Anita Alves Donato
* Ana Beatriz
* Ruan Vitor
* Rebecca Nery
*/

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner("programa.mc");
        Token tk;
        do {
            tk = sc.nextToken();
            System.out.println(tk);
        } while (tk != null);
    }

}
