package Modelos;

public class Jogador {

    private final Peca peca;
    private final String nome;
    private int pontos;

    public Jogador(String name, Peca peca) {
        this.peca = peca;
        this.nome = name;
        this.pontos = 0;
    }

    public int getPontos() {
        return this.pontos;
    }

    public void pontua() {
        this.pontos++;
    }

    public String getNome() {
        return this.nome;
    }

    public Peca getPeca() {
        return peca;
    }

}