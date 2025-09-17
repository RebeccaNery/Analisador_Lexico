## 🔍 Analisador Léxico em Java
Um analisador léxico simples e funcional para uma mini-linguagem, desenvolvido como projeto para a disciplina de Compiladores.

## 📜 Sobre o Projeto
Este projeto implementa a primeira fase de um compilador: a Análise Léxica. O programa lê um arquivo de código-fonte de uma linguagem hipotética e o converte em uma sequência de tokens, que podem ser utilizados pela próxima fase, a análise sintática.

O analisador foi construído em Java puro, utilizando o conceito de uma Máquina de Estados Finitos para reconhecer os diferentes padrões (lexemas) da linguagem.

## ✨ Funcionalidades
O analisador é capaz de reconhecer:

* ✅ Palavras Reservadas: if, else, int, float, print

* ✅ Identificadores: Nomes de variáveis como soma, valor, x1

* ✅ Números: Inteiros (10) e decimais (3.14)

* ✅ Operadores:

    * Matemáticos (+, -, *, /)

    * Relacionais (>, <, ==, !=, >=, <=)

    * Atribuição (=)

* ✅ Símbolos: Parênteses ((, ))

* ✅ Comentários: Ignora comentários de uma linha (// ...)

* ✅ Espaços em Branco: Ignora espaços, tabulações e quebras de linha.

## 🚀 Como Executar
Pré-requisitos
* Java Development Kit (JDK) - Versão 11 ou superior.

### Passos
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    cd seu-repositorio
    ```

2.  **Crie um arquivo de teste:**
    Crie um arquivo chamado `programa.txt` na raiz do projeto com o código que você deseja analisar. Por exemplo:
    ```java
    // Exemplo de código
    int a = 10
    float b = 20.5
    if (a > b) {
        print a
    }
    ```

3.  **Compile o projeto:**
    (Assumindo que seus arquivos `.java` estão em uma pasta `src`)
    ```bash
    javac -d out src/**/*.java
    ```

4.  **Execute o analisador:**
    (Assumindo que sua classe principal é `mini_compiler.Main`)
    ```bash
    java -cp out mini_compiler.Main programa.txt
    ```
    O programa irá imprimir a sequência de tokens encontrados no arquivo.

---


## 💻 Tecnologias Utilizadas
* Java

Feito com ❤️ por [Rebecca Nery].
