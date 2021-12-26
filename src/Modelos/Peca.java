package Modelos;

public enum Peca {
    // Algumas opções para peças:
    // '●' '◇' '◆'
    PecaBranca('□'), PecaPreta('■'), DamaBranca('░'), DamaPreta('▇');

    public final char peca;

    Peca(char caractere) {
        this.peca = caractere;
    }

    @Override
    public String toString() {
        return String.valueOf(peca);
    }
}