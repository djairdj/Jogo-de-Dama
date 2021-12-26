package Teste;

import Modelos.Jogador;
import Modelos.Peca;

import java.util.Arrays;
import java.util.Random;

public class TesteJogador {
    public static void main(String[] args) {
        var pedra1 = Peca.values()[new Random().nextInt(Peca.values().length)];
        var pedra2 = Peca.values()[new Random().nextInt(Peca.values().length)];
        Jogador j = new Jogador("Kelvin", pedra1, pedra2);
        System.out.println(Arrays.toString(j.getPecas()));
    }
}