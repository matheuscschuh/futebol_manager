package jogo;

import modelo.Jogador;
import modelo.TipoCasa;

public class Jogo {

    private double patrocinio = 100000;   // depois vem das configurações

    public void moverParaFrente(Jogador jogador, int passos) {
        for (int i = 0; i < passos; i++) {

            jogador.avancarUmaCasa();


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

            if (jogador.getPosicaoAtual().getDado().getTipo() == TipoCasa.INICIO) {
                jogador.adicionarVoltaPendente();
            }


            jogador.recuarUmaCasa();
        }
    }
}