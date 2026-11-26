package jogo;

import modelo.Jogador;
import modelo.TipoCasa;

public class Jogo {

    private double patrocinio = 100000;   // depois vem das configurações

    public void moverParaFrente(Jogador jogador, int passos) {
        for (int i = 0; i < passos; i++) {
            // 1. dá um passo para frente
            jogador.avancarUmaCasa();

            // 2. se a casa onde ele chegou é o INÍCIO:
            //      - se ele tem volta pendente → quita uma, e imprime "reposição de volta" (SEM patrocínio)
            //      - senão → recebe o patrocínio, completa uma temporada e imprime uma mensagem
            if (jogador.getPosicaoAtual().getDado().getTipo() == TipoCasa.INICIO) {
                if (jogador.getVoltasPendentes() > 0) {
                    jogador.quitarVoltaPendente();
                    System.out.println(">> " + jogador.getNome() + " passou pelo Início, " +
                            "mas tinha uma volta pendente (reposição de volta). Sem patrocínio.");
                } else {
                    double saldoAntes = jogador.getSaldo();
                    jogador.receber(patrocinio);
                    jogador.completarTemporada();
                    System.out.println(">> Temporada concluída! " + jogador.getNome() + " recebeu R$ " + patrocinio
                            + " de patrocínio | Saldo: R$ " + saldoAntes + " → R$ " + jogador.getSaldo());
                }
            }
        }
    }

    public void moverParaTras(Jogador jogador, int passos) {
        for (int i = 0; i < passos; i++) {
            // 1. ANTES de recuar: se ele está NO Início, vai sair dele para trás
            //    → adiciona uma volta pendente
            if (jogador.getPosicaoAtual().getDado().getTipo() == TipoCasa.INICIO) {
                jogador.adicionarVoltaPendente();
            }

            // 2. dá um passo para trás
            jogador.recuarUmaCasa();
        }
    }
}