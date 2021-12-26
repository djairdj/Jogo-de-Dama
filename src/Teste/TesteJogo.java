package Teste;

import Modelos.Jogo;

public class TesteJogo {
    public static void main(String[] args) {
        System.out.println("Usando método estático.");
        Jogo.novoJogo();
        System.out.println("Usando método do objeto.");
        Jogo h = new Jogo();
        h.jogar();
    }
}