package Teste;

import Modelos.Jogador;
import Modelos.Peca;

public class TesteJogador {
    public static void main(String[] args) {
        int sorteio = (int) Math.round(Math.random()) % 2;
        Jogador j = new Jogador("Kelvin", Peca.values()[sorteio], Peca.values()[sorteio + 2]);
        System.out.println(j);
    }
}