package jogo;

import estruturas.ListaDuplaCircular;
import modelo.Casa;
import modelo.Clube;
import modelo.TipoCasa;

import java.util.ArrayList;

/**
 * Monta o tabuleiro (Lista Duplamente Ligada Circular de casas).
 * Regra do enunciado (item 2.2):
 *   T = N + E  (N = clubes, E = casas especiais)
 *   A k-ésima casa especial fica na posição round(k * T / E).
 *   As posições que sobram recebem os clubes, na ordem de cadastro.
 *
 * O vetor é usado SÓ durante a montagem, para calcular as posições.
 * Depois disso, o jogo anda apenas pelos nós (proximo/anterior), sem índices.
 */
public class Tabuleiro {

    // Equipe de 1 integrante: sem Série B e sem Leilão (4 casas especiais, nesta ordem)
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

        // 1) Coloca as casas especiais nas posições calculadas pela fórmula
        for (int k = 0; k < e; k++) {
            int posicao = (int) Math.round((double) k * t / e);
            posicoes[posicao] = new Casa(ESPECIAIS[k]);
        }

        // 2) Preenche as posições vazias com os clubes, na ordem de cadastro
        int proximoClube = 0;
        for (int i = 0; i < t; i++) {
            if (posicoes[i] == null) {
                posicoes[i] = new Casa(clubes.get(proximoClube));
                proximoClube++;
            }
        }

        // 3) Passa tudo para a lista circular, na ordem
        ListaDuplaCircular<Casa> tabuleiro = new ListaDuplaCircular<>();
        for (Casa casa : posicoes) {
            tabuleiro.adicionarNoFim(casa);
        }

        return tabuleiro;
    }
}