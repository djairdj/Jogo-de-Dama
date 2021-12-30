package Teste;

import Modelos.Jogo;

public class TesteJogo {
    public static void main(String[] args) {
        Jogo h = new Jogo();
        h.jogar();
        System.out.println(h);
    }
}
/*
1
Bill
Bob
5 6
4 7
2 5
3 6
4 7
2 5
2 7
3 6
2 5
4 7
1 6
2 5
4 7
3 6
2 5
3 4
3 6
2 7
0 5
1 6
2 7
0 5
1 4
2 5
0 5
4 1
 */