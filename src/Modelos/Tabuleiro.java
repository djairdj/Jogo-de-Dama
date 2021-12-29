package Modelos;

public class Tabuleiro {
    private final char[][] tabuleiro;

    private final char pedraBranca, pedraPreta;

    public Tabuleiro(char pecaBranca, char pecaPreta) {
        this.tabuleiro = new char[8][8];
        this.pedraBranca = pecaBranca;
        this.pedraPreta = pecaPreta;
        this.zeraTabuleiro();
    }

    public void zeraTabuleiro() {
        int l, c, linhasPreenchidas = 3;
        for (l = 0; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro[l].length; c++) {
                tabuleiro[l][c] = ' ';
            }
        }
        // Organizando as peças superiores
        for (l = 0; l < linhasPreenchidas; l++) {
            for (c = 0; c < tabuleiro[l].length; c++) {
                if (l % 2 != c % 2) {
                    tabuleiro[l][c] = pedraPreta;
                }
            }
        }
        // Organizando as peças inferiores
        for (l = tabuleiro.length - linhasPreenchidas; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro[l].length; c++) {
                if (l % 2 != c % 2) {
                    tabuleiro[l][c] = pedraBranca;
                }
            }
        }
    }

    public char[][] getTabuleiro() {
        return tabuleiro;
    }

    private String tabuleiro() {
        StringBuilder ss = new StringBuilder(" ");
        int l, c, largura = 4 * this.tabuleiro[0].length;
        String linha = "", espaco = "   ";
        while (largura-- > 0) {
            linha += "-";
        }
        linha += "\n";

        for (int index = 0; index < tabuleiro[0].length; index++) {
            ss.append(index + espaco);
        }
        ss.append("\n" + linha);
        for (l = 0; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro[l].length; c++) {
                ss.append(" " + tabuleiro[l][c] + " |");
                if (c == tabuleiro[l].length - 1) ss.append("  " + l);
            }
            ss.append("\n" + linha);
        }
        return ss.toString();
    }

    public String toString() {
        return tabuleiro();
    }
}