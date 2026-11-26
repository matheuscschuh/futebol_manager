package jogo;

import estruturas.ListaDuplaCircular;
import modelo.Casa;
import modelo.Clube;
import modelo.TipoCasa;

import java.util.ArrayList;

public class Tabuleiro {


    private static final TipoCasa[] ESPECIAIS = {
            TipoCasa.INICIO,
            TipoCasa.SORTE_REVES,
            TipoCasa.FOLHA_SALARIAL,
            TipoCasa.PREMIACAO
    };

    public static ListaDuplaCircular<Casa> montar(ArrayList<Clube> clubes) {
        int n = clubes.size();
        int e = ESPECIAIS.length;
        int t = n + e;

        Casa[] posicoes = new Casa[t];


        for (int k = 0; k < e; k++) {
            int posicao = (int) Math.round((double) k * t / e);
            posicoes[posicao] = new Casa(ESPECIAIS[k]);
        }


        int proximoClube = 0;
        for (int i = 0; i < t; i++) {
            if (posicoes[i] == null) {
                posicoes[i] = new Casa(clubes.get(proximoClube));
                proximoClube++;
            }
        }


        ListaDuplaCircular<Casa> tabuleiro = new ListaDuplaCircular<>();
        for (Casa casa : posicoes) {
            tabuleiro.adicionarNoFim(casa);
        }

        return tabuleiro;
    }
}