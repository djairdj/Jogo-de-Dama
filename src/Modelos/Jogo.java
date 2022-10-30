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
    private boolean deveCapturar = false, obrigatorioCapturar = false;
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
                System.out.printf("Vez de %s, as peças podem ser: %s.%n", jogadores[vez].getNome(), Arrays.toString(jogadores[vez].getPecas()));
                defineOrigem(jogadores[vez]);
                deveCapturar = false;
                System.out.print("\n" + tabuleiro);
                defineDestino(jogadores[vez]);
                if (deveCapturar) {
                    capturarGeral();
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
            System.out.print("\n" + tabuleiro);
            String player1 = this.jogadores[0].getNome();
            String player2 = this.jogadores[1].getNome();
            String vencedor = this.jogadores[0].getPontos() > this.jogadores[1].getPontos() ? player1 : player2;
            System.out.println(vencedor + " Ganhou!");
            sc.nextLine();
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
        boolean equivalente, pecaLivre = false;
        String pergunta;
        do {
            pergunta = String.format("%s, escolha uma linha de origem: ", j.getNome());
            this.lOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s, agora escolha uma coluna de origem: ", j.getNome());
            this.cOrigem = validaPonto(pergunta, this.tabuleiro.getTabuleiro()[lOrigem].length);

            boolean pecaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[0].peca;
            boolean damaOk = tabuleiro.getTabuleiro()[lOrigem][cOrigem] == j.getPecas()[1].peca;
            equivalente = pecaOk || damaOk;
            pecaLivre = this.pecaLivre();
        } while (!equivalente || !pecaLivre);
    }

    private void defineDestino(Jogador j) {
        String pergunta;
        boolean vago, eh_diagonal;
        String prefixo = "";
        prefixo = String.format("%s, sua linha de origem é %d e a coluna de origem é %d.\n", j.getNome(), lOrigem, cOrigem);
        do {
            pergunta = String.format("%s, escolha uma linha de destino: ", j.getNome());
            this.lDestino = validaPonto(prefixo + pergunta, this.tabuleiro.getTabuleiro().length);

            pergunta = String.format("%s, escolha uma coluna de destino: ", j.getNome());
            this.cDestino = validaPonto(prefixo + pergunta, this.tabuleiro.getTabuleiro()[lDestino].length);

            vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
            eh_diagonal = eh_diagonal();

        } while (!(vago && eh_diagonal && /*movimentoValido()*/checkCasa()));
    }

    /*
        private boolean ehPossivelCapturar() {
            boolean yes = true;
            var tab = this.tabuleiro.getTabuleiro();
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab[i].length; j++) {
                    if (tab[i][j])
                }
            }

            return yes;
        }
    */

    /*
        private boolean movimentoValido() {
            if (andaUmaCasaDiagonal()) {
                return true; // Anda uma casa na diagonal
            } else {
                return checkDiagonal();
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

        private boolean movimentoValidoDama() {
            char peca = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
            if (peca != this.pecaPreta && peca != this.pecaBranca) {
                // Confirmo que é dama
                boolean eh_diagonal = eh_diagonal();
                boolean vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
                if (vago && eh_diagonal) {
                    return checkDiagonal();
                }
            }
            return false;
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

    private boolean movimentoSimples() {
        char peca = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        if (lDestino == lOrigem + 1 && peca == this.pecaBranca) {
            return false;
        }
        if (lDestino == lOrigem - 1 && peca == this.pecaPreta) {
            return false;
        }
        return true;
    }

    */
    private boolean checkDiagonal() {
        if (tabuleiro.getTabuleiro()[lDestino][cDestino] != ' ') {
            return false;
        }
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        char pOponente = this.pecaPreta;
        char dOponente = this.damaPreta;

        if (pecaEscolhida == this.damaPreta || pecaEscolhida == this.pecaPreta) {
            pOponente = this.pecaBranca;
            dOponente = this.damaBranca;
        }
        int menor = lOrigem, maior = lDestino, coluna = cOrigem + 1, passo = 1; // Descendo para direita
        if (lDestino < lOrigem) { // Subindo
            maior = lOrigem;
            menor = lDestino;
            coluna = (cDestino > cOrigem) ? cDestino - 1 : cDestino + 1;
            passo = (cDestino > cOrigem) ? -1 : 1;
        }
        if (cDestino < cOrigem) { // Esquerda
            passo = (lDestino < lOrigem) ? 1 : -1;
            coluna = (lDestino < lOrigem) ? cDestino + 1 : cOrigem - 1;
        }
        var tab = this.tabuleiro.getTabuleiro();
        int nPecas = 0, space = 0;
        char p;

        for (int l = menor + 1; l < maior; l++, coluna += passo) {
            p = tab[l][coluna];
            if (p != pOponente && p != dOponente && p != ' ') {
                return false;
            }
            if (p == ' ') {
                space++;
            }
            if (p == pOponente || p == dOponente) {
                nPecas++;
            }
        }
        boolean pSimples = (pecaEscolhida == this.pecaPreta || pecaEscolhida == this.pecaBranca);
        if ((space > 0 && (pSimples)) || (nPecas > 1)) {
            return false;
        } else {
            if ((nPecas == 1 && (space == 0 || !pSimples))) {
                deveCapturar = true;
            }
            return true;
        }
    }

    private void capturarGeral() {
        // Acredito que dá para usar a mesma lógica do laço para captura de peças
        // A diferença aparente é que a dama pode se mover mais que duas casas
        var tab = this.tabuleiro.getTabuleiro();
        char pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem], pOponente, dOponente, p;
        int menor = lOrigem, maior = lDestino, coluna = cOrigem + 1, passo = 1; // Descendo para direita
        if (lDestino < lOrigem) { // Subindo
            maior = lOrigem;
            menor = lDestino;
            coluna = (cDestino > cOrigem) ? cDestino - 1 : cDestino + 1;
            passo = (cDestino > cOrigem) ? -1 : 1;
        }
        if (cDestino < cOrigem) { // Esquerda
            passo = (lDestino < lOrigem) ? 1 : -1;
            coluna = (lDestino < lOrigem) ? cDestino + 1 : cOrigem - 1;
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
                    this.jogadores[1].pontua();
                } else {
                    nPecasPretas--;
                    this.jogadores[0].pontua();
                }
                movePeca();
                tab[l][coluna] = ' ';
            }
        }
        if (!captura) {
            System.out.println("Erro na lógica de análise das diagonais.");
        }
    }

    private boolean checkCasa() {
        var pecaEscolhida = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        boolean vago = tabuleiro.getTabuleiro()[lDestino][cDestino] == ' ';
        if (vago) {
            if (pecaEscolhida == this.damaPreta || pecaEscolhida == this.damaBranca) {
                if (lDestino == lOrigem + 1 && (cDestino == cOrigem + 1 || cDestino == cOrigem - 1)) {
                    return true;
                } else if (lDestino == lOrigem - 1 && (cDestino == cOrigem + 1 || cDestino == cOrigem - 1)) {
                    return true;
                }
                return checkDiagonal();
            } else if ((lDestino == lOrigem + 1 || lDestino == lOrigem - 1) && (cDestino == cOrigem - 1 || cDestino == cOrigem + 1)) {
                if (lDestino == lOrigem + 1 && pecaEscolhida == this.pecaBranca) {
                    return false;
                }
                if (lDestino == lOrigem - 1 && pecaEscolhida == this.pecaPreta) {
                    return false;
                }
                return true;
            } else {
                return checkDiagonal();
            }
        }
        return false;
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

    private void movePeca() {
        tabuleiro.getTabuleiro()[lDestino][cDestino] = tabuleiro.getTabuleiro()[lOrigem][cOrigem];
        tabuleiro.getTabuleiro()[lOrigem][cOrigem] = ' ';
    }

    private boolean pecaLivre() {
        char pecaOrigem = this.tabuleiro.getTabuleiro()[this.lOrigem][this.cOrigem];
        boolean livre1 = false, livre2 = false, livre3 = false, livre4 = false;
        if (pecaOrigem == this.pecaPreta) {
            this.lDestino = lOrigem + 1; // Desce uma linha
            if (cOrigem == 0) { // Analisar só pra direita
                cDestino = cOrigem + 1;
                livre1 = checkCasa();
                if (this.lDestino + 1 < tabuleiro.getTabuleiro().length) {
                    lDestino++;
                    cDestino++;
                    livre2 = checkCasa();
                }
                return livre1 || livre2;
            } else if (cOrigem == tabuleiro.getTabuleiro().length - 1) {
                //Analisar só pra esquerda
                cDestino = cOrigem - 1;
                livre1 = checkCasa();
                if (this.lDestino + 1 < tabuleiro.getTabuleiro().length) {
                    lDestino++;
                    cDestino--;
                    livre2 = checkCasa();
                }
                return livre1 || livre2;
            } else { // Ambas as direções devem ser checadas
                cDestino = cOrigem + 1; // Para direita
                livre1 = checkCasa();
                cDestino = cOrigem - 1; // Para esquerda
                livre2 = checkCasa();
                if (this.lDestino + 1 < tabuleiro.getTabuleiro().length) {
                    lDestino++;
                    if (cOrigem + 2 < tabuleiro.getTabuleiro().length) {
                        cDestino = cOrigem + 2;
                        livre3 = checkCasa();
                    }
                    if (cOrigem - 2 >= 0) {
                        cDestino = cOrigem - 2;
                        livre4 = checkCasa();
                    }
                }
                if (!(livre1 || livre2 || livre3 || livre4)) {
                    if (lOrigem - 2 >= 0) {
                        lDestino = lOrigem - 2;
                        if (cOrigem + 2 < tabuleiro.getTabuleiro()[lDestino].length) {
                            cDestino = cOrigem + 2;
                            livre3 = checkCasa();
                        }
                        if (cOrigem - 2 >= 0) {
                            cDestino = cOrigem - 2;
                            livre4 = checkCasa();
                        }
                    }
                }
                return livre1 || livre2 || livre3 || livre4;
            }
        } else if (pecaOrigem == this.pecaBranca) { // Usando Peças Brancas
            this.lDestino = lOrigem - 1; // Desce uma linha
            if (cOrigem == 0) { // Analisar só pra direita
                cDestino = cOrigem + 1;
                livre1 = checkCasa();
                if (this.lDestino - 1 >= 0) {
                    lDestino--;
                    cDestino++;
                    livre2 = checkCasa();
                }
                return livre1 || livre2;
            } else if (cOrigem == tabuleiro.getTabuleiro().length - 1) {
                // Analisar só pra esquerda
                this.cDestino = this.cOrigem - 1;
                livre1 = checkCasa();
                if (this.lDestino - 1 >= 0) {
                    lDestino--;
                    cDestino--;
                    livre2 = checkCasa();
                }
                return livre1 || livre2;
            } else { // Ambas as direções devem ser checadas
                this.cDestino = this.cOrigem + 1; // Para direita
                livre1 = checkCasa();
                cDestino = cOrigem - 1; // Para esquerda
                livre2 = checkCasa();
                if (this.lDestino - 1 >= 0) {
                    lDestino--;
                    if (cOrigem + 2 < tabuleiro.getTabuleiro()[lDestino].length) {
                        cDestino = cOrigem + 2;
                        livre3 = checkCasa();
                    }
                    if (cOrigem - 2 >= 0) {
                        cDestino = cOrigem - 2;
                        livre4 = checkCasa();
                    }
                }
                if (!(livre1 || livre2 || livre3 || livre4)) {
                    if (lOrigem + 2 < tabuleiro.getTabuleiro().length) {
                        lDestino = lOrigem + 2;
                        if (cOrigem + 2 < tabuleiro.getTabuleiro()[lDestino].length) {
                            cDestino = cOrigem + 2;
                            livre3 = checkCasa();
                        }
                        if (cOrigem - 2 >= 0) {
                            cDestino = cOrigem - 2;
                            livre4 = checkCasa();
                        }
                    }
                }
                return livre1 || livre2 || livre3 || livre4;
            }
        } else { // Aqui são damas
            boolean diagRightUp = false, diagRightDown = false, diagLeftUp = false, diagLeftDown = false;
            char[][] tab = this.tabuleiro.getTabuleiro();
            int lastLinha = tab.length - 1, lastColuna = tab[0].length - 1;

            this.lDestino = this.lOrigem;
            this.cDestino = this.cOrigem;
            while (this.cDestino <= lastColuna && this.lDestino <= lastLinha) { // Analisar down lado direito
                diagRightDown = checkCasa();
                if (diagRightDown) break;
                this.lDestino++;
                this.cDestino++;
            }

            this.lDestino = this.lOrigem;
            this.cDestino = this.cOrigem;
            while (cDestino >= 0 && lDestino <= lastLinha) { // Analisar down lado esquerdo
                diagLeftDown = checkCasa();
                if (diagLeftDown) break;
                this.lDestino++;
                this.cDestino--;
            }

            this.lDestino = this.lOrigem;
            this.cDestino = this.cOrigem;
            while (cDestino <= lastColuna && lDestino >= 0) {// Analisar up lado direito
                diagRightUp = checkCasa();
                if (diagRightUp) break;
                this.lDestino--;
                this.cDestino++;
            }

            this.lDestino = this.lOrigem;
            this.cDestino = this.cOrigem;
            while (cDestino >= 0 && lDestino >= 0) { // Analisar up esquerdo
                diagLeftUp = checkCasa();
                if (diagLeftUp) break;
                this.lDestino--;
                this.cDestino--;
            }

            return diagRightDown || diagLeftDown || diagRightUp || diagLeftUp;
        }

    }

    private boolean eh_diagonal() {
        int diflinhas = Math.abs(lOrigem - lDestino);
        int difColunas = Math.abs(cOrigem - cDestino);
        return (diflinhas == difColunas);
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