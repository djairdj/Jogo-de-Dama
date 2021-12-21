package Modelos;

public enum Pecas {
    PecaBranca('○'), PecaPreta('●'), DamaBranca('░'), DamaPreta('▇');

    public final char peca;
    Pecas(char caractere){
        this.peca = caractere;
    }
    @Override
    public String toString() {
        return String.valueOf(peca);
    }
}