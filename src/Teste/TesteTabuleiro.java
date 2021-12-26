package Teste;

import Modelos.Peca;
import Modelos.Tabuleiro;

public class TesteTabuleiro {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(Peca.DamaBranca.peca, Peca.DamaPreta.peca);
        System.out.print(tabuleiro);
    }
}