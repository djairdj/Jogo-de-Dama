package Modelos;

import java.util.Arrays;
import java.util.Scanner;

public class Jogo {
    private static Scanner sc = new Scanner(System.in);
    private Tabuleiro tabuleiro;
    private Jogador[] jogadores;
    private int nJogadas;
    private int nPecasBrancas;
    private int nPecasPretas;
    private int lOrigem, cOrigem, lDestino, cDestino;
    private boolean deveCapturar = false;
    private char pecaBranca, damaBranca, pecaPreta, damaPreta;

    public void defineDadosIniciais() {
        this.jogadores = new Jogador[2];
        this.tabuleiro = new Tabuleiro(Peca.PecaBranca.peca, Peca.PecaPreta.peca);
        this.nJogadas = 0;
        this.nPecasBrancas = 12;
        this.nPecasPretas = 12;
        setJogadores();
        this.pecaBranca = this.jogadores[0].peca().peca;
        this.damaBranca = this.jogadores[0].dama().peca;
        this.pecaPreta = this.jogadores[1].peca().peca;
        this.damaPreta = this.jogadores[1].dama().peca;
    }

    public void jogar() {
        System.out.println("Bem vindo ao jogo de dama.");
        System.out.print("Caso queira iniciar, digite 1: ");
        String escolha = sc.nextLine();
        while (escolha.equals("1")) {
            // A variável nJogada já vale 0 (inicialmente) e cada mudança de jogador ela deve ser incrementada.
            int vez;
            defineDadosIniciais();
            while (nPecasBrancas != 0 && nPecasPretas != 0) {
                vez = nJogadas % 2;
                System.out.print("\n" + tabuleiro);
                System.out.printf("Quem joga agora é %s, as peças podem ser: %s.%n", jogadores[vez].getNome(), Arrays.toString(jogadores[vez].getPecas()));
                defineOrigem(jogadores[vez]);
                System.out.print("\n" + tabuleiro);
                defineDestino(jogadores[vez]);
                if (deveCapturar) {
                    capturarPeca();
                    viraDama();
                    deveCapturar = false;
//                    while (eh_possivelCapturar(jogadores[nJogadas])) {
//                        defineDestino(jogadores[nJogadas]);
//                    }
                } else {
                    movePeca();
                    viraDama();
                }
                nJogadas++;
            }
            System.out.print("Digite 1 para iniciar um novo jogo: ");
            escolha = sc.nextLine();
            if (!escolha.equals("1")) {
                System.out.println("Até a próxima.");
            }
        }
    }

    private void capturarPeca() {
        char peca = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        if (peca == this.damaBranca || peca == this.damaPreta) {
            capturarGeral();
        } else {
            int l = lOrigem - 1, c = cOrigem + 1; // Subindo a direita
            if (lOrigem < lDestino) { // Está descendo
                l = lOrigem + 1;
            }
            if (cOrigem > cDestino) { // Está indo à esquerda
                c = cOrigem - 1;
            }
            char oponente = tabuleiro.getTabuleiro()[l][c];
            if (peca == this.pecaBranca) {
                if (oponente == this.damaPreta || oponente == this.pecaPreta) {
                    movePeca();
                    tabuleiro.getTabuleiro()[l][c] = ' ';
                    this.nPecasPretas--;
                } else {
                    System.out.println("Não é possível fazer a captura.");
                }
            } else if (peca == this.pecaPreta) {
                if (oponente == this.pecaBranca || oponente == this.damaBranca) {
                    movePeca();
                    tabuleiro.getTabuleiro()[l][c] = ' ';
                    this.nPecasBrancas--;
                } else {
                    System.out.println("Não é possível fazer a captura.");
                }
            }
        }
    }

    private void defineOrigem(Jogador j) {
        boolean equivalente;
        String pergunta;
        do {
            pergunta = String.format("%s, escolha uma linha de origem: ", j.getNome());
            this.lOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s, agora escolha uma coluna de origem: ", j.getNome());
            this.cOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro()[lOrigem].length);

            boolean pecaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[0].peca;
            boolean damaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[1].peca;
            equivalente = pecaOk || damaOk;
        } while (!equivalente);
        // Falta checar se a peça pode ser movida e se não puder, repetir a chamada desse método avisando o porquê.
//        while (pecaPresa()){
//            defineOrigem(j);
//        }
    }

    private void defineDestino(Jogador j) {
        String pergunta;
        boolean vago, movimentoOk, eh_diagonal;
        String prefixo = "";
        prefixo = String.format("%s, sua linha de origem é %d e a coluna de origem é %d.\n", j.getNome(), lOrigem, cOrigem);
        do {
            pergunta = String.format("%s, escolha uma linha de destino: ", j.getNome());
            this.lDestino = validaPonto(prefixo + pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s, escolha uma coluna de destino: ", j.getNome());
            this.cDestino = validaPonto(prefixo + pergunta, this.tabuleiro.getTabuleiro()[lDestino].length);

            vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
            eh_diagonal = (lOrigem + cOrigem) % 2 == (lDestino + cDestino) % 2;
            movimentoOk = movimentoValido();
        } while (!(vago && eh_diagonal && movimentoOk));
    }

    private void movePeca() {
        tabuleiro.getTabuleiro()[lDestino][cDestino] = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        tabuleiro.getTabuleiro()[lOrigem][cOrigem] = ' ';
    }

    private boolean movimentoValido() {
        if (andaUmaCasaDiagonal()) {
            return true; // Anda uma casa na diagonal
        } else {
            return possibleCapture2();
        }
    }

    private boolean possibleCapture() {
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        if ((pecaEscolhida == this.damaBranca || pecaEscolhida == this.damaPreta)) {
            return movimentoValidoDama();
        } else {
            boolean linhaSimples = lDestino == lOrigem + 2 || lDestino == lOrigem - 2;
            boolean colunaSimples = cDestino == cOrigem + 2 || cDestino == cOrigem - 2;
            if (linhaSimples && colunaSimples) { // Verificar captura simples
                // inicialmente vou assumir que a peça é branca...
                char oponentePeca = this.pecaPreta;
                char oponenteDama = this.damaPreta;
                if (pecaEscolhida == this.pecaPreta) {
                    oponentePeca = this.pecaBranca;
                    oponenteDama = this.damaBranca;
                }
                int vertical = lOrigem - 1, horizontal = cOrigem + 1; // Subindo para direita
                if (lDestino > lOrigem) {
                    vertical = lDestino - 1; // Descendo
                }
                if (cDestino < cOrigem) {
                    horizontal = cOrigem - 1; // Indo para esquerda
                }
                char oponente = tabuleiro.getTabuleiro()[vertical][horizontal];
                if (oponente == oponentePeca || oponente == oponenteDama) {
                    deveCapturar = true;
                    return true;
                } else {
                    return false;
                }
            }
            // Aqui a peça não é dama e está querendo fazer um movimento de dama. Proibido.
            return false;
        }
    }

    private boolean possibleCapture2() {
        return checkDiagonal();
        
    }

    private boolean movimentoValidoDama() {
        char peca = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        if (peca != this.pecaPreta && peca != this.pecaBranca) {
            // Confirmo que é dama
            boolean eh_diagonal = (lOrigem + cOrigem) % 2 == (lDestino + cDestino) % 2;
            boolean vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
            if (vago && eh_diagonal) {
                return checkDiagonal();
            }
        }
        return false;
    }

    /*
        private boolean checkDiagonalSupDireita() {
            boolean ret = false;
            char damaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
            char pOponente = this.pecaPreta;
            char dOponente = this.damaPreta;

            if (damaEscolhida == this.damaPreta) {
                pOponente = this.pecaBranca;
                dOponente = this.damaBranca;
            }
            var tab = tabuleiro.getTabuleiro();
            int nPecas = 0;
            char p;
            for (int l = lOrigem - 1, c = cOrigem + 1; l > lDestino; l--, c++) {
                p = tab[l][c];
                if (p != pOponente && p != dOponente && p != ' ') {
                    return false;
                }
                if (p == pOponente || p == dOponente) {
                    nPecas++;
                }
            }
            if (nPecas <= 1) {
                deveCapturar = true;
                return true;
            }
            return ret;
        }

        private boolean checkDiagonalSupEsquerda() {
            boolean ret = false;
            char damaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
            char pOponente = this.pecaPreta;
            char dOponente = this.damaPreta;

            if (damaEscolhida == this.damaPreta) {
                pOponente = this.pecaBranca;
                dOponente = this.damaBranca;
            }
            var tab = tabuleiro.getTabuleiro();
            int nPecas = 0;
            char p;
            for (int l = lOrigem - 1, c = cOrigem - 1; l > lDestino; l--, c--) {
                p = tab[l][c];
                if (p != pOponente && p != dOponente && p != ' ') {
                    return false;
                }
                if (p == pOponente || p == dOponente) {
                    nPecas++;
                }
            }
            if (nPecas <= 1) {
                deveCapturar = true;
                return true;
            }
            return ret;
        }

        private boolean checkDiagonalInfDireita() {
            boolean ret = false;
            char damaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
            char pOponente = this.pecaPreta;
            char dOponente = this.damaPreta;

            if (damaEscolhida == this.damaPreta) {
                pOponente = this.pecaBranca;
                dOponente = this.damaBranca;
            }
            var tab = tabuleiro.getTabuleiro();
            int nPecas = 0;
            char p;
            for (int l = lOrigem + 1, c = cOrigem + 1; l < lDestino; l--, c++) {
                p = tab[l][c];
                if (p != pOponente && p != dOponente && p != ' ') {
                    return false;
                }
                if (p == pOponente || p == dOponente) {
                    nPecas++;
                }
            }
            if (nPecas <= 1) {
                deveCapturar = true;
                return true;
            }
            return ret;
        }

        private boolean checkDiagonalInfEsquerda() {
            boolean ret = false;
            char damaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
            char pOponente = this.pecaPreta;
            char dOponente = this.damaPreta;

            if (damaEscolhida == this.damaPreta) {
                pOponente = this.pecaBranca;
                dOponente = this.damaBranca;
            }
            var tab = tabuleiro.getTabuleiro();
            int nPecas = 0;
            char p;
            for (int l = lOrigem + 1, c = cOrigem - 1; l < lDestino; l++, c--) {
                p = tab[l][c];
                if (p != pOponente && p != dOponente && p != ' ') {
                    return false;
                }
                if (p == pOponente || p == dOponente) {
                    nPecas++;
                }
            }
            if (nPecas <= 1) {
                deveCapturar = true;
                return true;
            }
            return ret;
        }
    */
    private boolean checkDiagonal() {
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
//        if (pecaEscolhida == this.damaBranca || pecaEscolhida == this.damaPreta) {
        char pOponente = this.pecaPreta;
        char dOponente = this.damaPreta;

        if (pecaEscolhida == this.damaPreta) {
            pOponente = this.pecaBranca;
            dOponente = this.damaBranca;
        }
        int menor = lOrigem, maior = lDestino, coluna = cOrigem + 1, passo = 1; // Descendo para direita
        if (lDestino < lOrigem) {
            menor = lDestino;
            maior = lOrigem;
            coluna = cDestino + 1;
        }
        if (cDestino < cOrigem) {
            passo = -1;
            coluna = cOrigem - 1;
        }
        var tab = this.tabuleiro.getTabuleiro();
        int nPecas = 0;
        char p;
        if (pecaEscolhida == this.pecaPreta || pecaEscolhida == this.pecaBranca) {
            maior = menor + 2;
        }

        for (int l = menor + 1; l < maior; l++, coluna += passo) {
            p = tab[l][coluna];
            if (p != pOponente && p != dOponente && p != ' ') {
                return false;
            }
            if (p == pOponente || p == dOponente) {
                nPecas++;
            }
        }
        if (nPecas == 1) {
            deveCapturar = true;
        }
        if (nPecas == 0 && (pecaEscolhida == this.pecaPreta || pecaEscolhida == this.pecaBranca)) {
            return false;
        } else {
            return true;
        }
    }

    private void capturarGeral() {
        // Acredito que dá para usar a mesma lógica do laço para captura de peças
        // A diferença aparente é que a dama pode se mover mais que duas casas
        var tab = this.tabuleiro.getTabuleiro();
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem], pOponente, dOponente, p;
        int menor = lOrigem, maior = lDestino, coluna = cOrigem + 1, passo = 1; // Descendo para direita
        if (lDestino < lOrigem) {
            menor = lDestino;
            maior = lOrigem;
            coluna = cDestino + 1;
        }
        if (cDestino < cOrigem) {
            passo = -1;
            coluna = cOrigem - 1;
        }

        pOponente = this.pecaPreta;
        dOponente = this.damaPreta;

        if (pecaEscolhida == this.damaPreta || pecaEscolhida == this.pecaPreta) {
            pOponente = this.pecaBranca;
            dOponente = this.damaBranca;
        }
        if (pecaEscolhida == this.pecaPreta || pecaEscolhida == this.pecaBranca) {
            maior = menor + 2;
        }

        boolean captura = true;
        for (int l = menor + 1; l < maior; l++, coluna += passo) {
            p = tab[l][coluna];
            if (p != pOponente && p != dOponente && p != ' ') {
                captura = false;
            }
            if (p == pOponente || p == dOponente) {
                if (p == this.pecaBranca || p == this.damaBranca) {
                    nPecasBrancas--;
                } else {
                    nPecasPretas--;
                }
                movePeca();
                tab[l][coluna] = ' ';
            }
        }
        if (!captura) {
            System.out.println("Erro na lógica de análise das diagonais.");
        }
    }

    private void viraDama() {
        char peca = tabuleiro.getTabuleiro()[lDestino][cDestino];

        if (lDestino == 0 && peca == this.pecaBranca) {
            tabuleiro.getTabuleiro()[lDestino][cDestino] = this.damaBranca;
        }
        if (lDestino == tabuleiro.getTabuleiro().length - 1 && peca == this.pecaPreta) {
            tabuleiro.getTabuleiro()[lDestino][cDestino] = this.damaPreta;
        }
    }

    private boolean andaUmaCasaDiagonal() {
        var pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        int nextLine, passo = 1;
        if (pecaEscolhida == this.damaPreta || pecaEscolhida == this.damaBranca) {
            if (lDestino == lOrigem + 1 && (cDestino == cOrigem + 1 || cDestino == cOrigem - 1)) {
                return true;
            } else if (lDestino == lOrigem - 1 && (cDestino == cOrigem + 1 || cDestino == cOrigem - 1)) {
                return true;
            }
            return false;
        } else if (pecaEscolhida == this.pecaBranca) {
            passo = -1;
        }
        nextLine = lOrigem + passo;

        return ((lDestino == nextLine) && (cDestino == cOrigem - passo || cDestino == cOrigem + passo));
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

    private int validaPonto(String mensagem, int limite) {
        int n = -1;
        if (limite >= 0) {
            while (n < 0 || n >= limite) {
                System.out.print(mensagem);
                n = sc.nextInt();
                if (n < 0 | n >= limite) {
                    System.out.println("Coordenada inválida, escolha de 0 até " + (limite - 1));
                }
            }
        }
        return n;
    }

    public String toString() {
        String result = "";
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[0].getNome(), jogadores[0].getPontos());
        result += String.format("Jogador %s marcou %d pontos.%n", jogadores[1].getNome(), jogadores[1].getPontos());
        return result;
    }
}