package Teste;

import Modelos.Pecas;

public class TesteEnum {
    public static void main(String[] args) {
        System.out.println(Pecas.PecaPreta.peca);
        System.out.println(Pecas.PecaBranca.name());
        System.out.println(Pecas.DamaPreta);
        System.out.println(Pecas.DamaBranca);
    }
}