package Modelos;

import java.util.Scanner;

public class Jogo {
    private static Scanner sc = new Scanner(System.in);
    private Tabuleiro tabuleiro;
    private Jogador[] jogadores;
    private int nJogadas;
    private int nPecasBrancas;
    private int nPecasPretas;

    public Jogo() {
        this.jogadores = new Jogador[2];
        this.tabuleiro = new Tabuleiro(Peca.PecaBranca.peca, Peca.PecaPreta.peca);
        this.nJogadas = 0;
        this.nPecasBrancas = 12;
        this.nPecasPretas = 12;
    }

    private void setJogadores() {  // Pronto
        String name1, name2;
        System.out.print("Informe o nome do primeiro jogador: ");
        name1 = sc.nextLine();
        name1 = name1.substring(0, 1).toUpperCase() + name1.substring(1);
        System.out.print("Informe o nome do segundo jogador: ");
        name2 = sc.nextLine();
        name2 = name2.substring(0, 1).toUpperCase() + name2.substring(1);
        int sorteio = (int) Math.round(Math.random()) % 2;
        jogadores[sorteio] = new Jogador(name1, Peca.values()[sorteio], Peca.values()[sorteio + 2]);
        jogadores[(sorteio + 1) % 2] = new Jogador(name2, Peca.values()[(sorteio + 1) % 2], Peca.values()[(sorteio + 1) % 2 + 2]);
    }

    public String toString() {
        String result = "";
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[0].getNome(), jogadores[0].getPontos());
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[1].getNome(), jogadores[1].getPontos());
        return result;
    }
}