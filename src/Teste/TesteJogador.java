package Teste;

import Modelos.Jogador;
import Modelos.Peca;

import java.util.Random;

public class TesteJogador {
    public static void main(String[] args) {
        var pedra = Peca.values()[new Random().nextInt(Peca.values().length)];
        Jogador j = new Jogador("Kelvin", pedra);
        System.out.println(j.getPeca());
    }
}