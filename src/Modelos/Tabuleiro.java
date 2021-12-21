package Modelos;

public class Tabuleiro {
    private final char[][] tabuleiro;

    private final char pedraBranca, pedraPreta;

    public Tabuleiro(int linhas, int colunas, char pecaBranca, char pecaPreta) {
        this.tabuleiro = new char[linhas][colunas];
        this.pedraBranca = pecaBranca;
        this.pedraPreta = pecaPreta;
    }

    public void zeraTabuleiro() {
        int l, c;
        for (l = 0; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro.length; c++) {
                tabuleiro[l][c] = ' ';
            }
        }
        // Organizando as peças superiores
        for (l = 0; l < 3; l++) {
            for (c = 0; c < tabuleiro.length; c++) {
                if (l % 2 != c % 2) {
                    tabuleiro[l][c] = pedraPreta;
                }
            }
        }
        // Organizando as peças inferiores
        for (l = 5; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro.length; c++) {
                if (l % 2 != c % 2) {
                    tabuleiro[l][c] = pedraBranca;
                }
            }
        }
    }

    private String tabuleiro() {
        StringBuilder ss = new StringBuilder(" ");
        int l, c;
        String linha = "-----------------------\n", escaco = "   ";

        for (int index = 1; index <= tabuleiro.length; index++) {
            ss.append(index + escaco);
        }
        ss.append("\n" + linha);
        for (l = 0; l < tabuleiro.length; l++) {
            for (c = 0; c < tabuleiro.length; c++) {
                ss.append(" " + tabuleiro[l][c] + " |");
                if (c == tabuleiro.length - 1) ss.append(" " + (l + 1));
            }
            ss.append("\n" + linha + "\n");
        }
        return ss.toString();
    }

    public String toString() {
        return tabuleiro();
    }
}