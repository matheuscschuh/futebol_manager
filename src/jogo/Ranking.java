package jogo;

import modelo.Jogador;

import java.util.ArrayList;

public class Ranking {

    public static ArrayList<Jogador> ordenarPorPatrimonio(ArrayList<Jogador> jogadores) {
        ArrayList<Jogador> ordenados = new ArrayList<>(jogadores);

        for (int i = 0; i < jogadores.size(); i++) {
            for (int j = 0; j < jogadores.size() - 1; j++) {
                Jogador atual = ordenados.get(j);
                Jogador proximo = ordenados.get(j + 1);

                double patrimonioAtual = atual.calcularPatrimonio();
                double patrimonioProximo = proximo.calcularPatrimonio();


                if (patrimonioProximo > patrimonioAtual
                        || (patrimonioProximo == patrimonioAtual && proximo.getSaldo() > atual.getSaldo())) {

                    ordenados.set(j, proximo);
                    ordenados.set(j + 1, atual);
                }
            }
        }

        return ordenados;
    }
}
