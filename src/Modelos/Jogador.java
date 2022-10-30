package Modelos;

public class Jogador {
    private final Peca[] peca;
    private final String nome;
    private int pontos;

    public Jogador(String name, Peca peca, Peca dama) {
        this.peca = new Peca[2];
        this.peca[0] = peca;
        this.peca[1] = dama;
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

    public Peca[] getPecas() {
        return peca;
    }

    public Peca peca() {
        return this.getPecas()[0];
    }

    public Peca dama() {
        return this.getPecas()[1];
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Jogador {" + this.nome;
        ret += ", peça = " + peca[0];
        ret += ", dama = " + peca[1];
        ret += ", pontos = " + pontos +
                '}';
        return ret;
    }
}