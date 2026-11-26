package jogo;

import modelo.Clube;
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

    public void pararEmClube(Jogador jogador) {
        Clube clube = jogador.getPosicaoAtual().getDado().getClube();
        Jogador dono = clube.getDono();

        if (dono == null) {
            double saldoAntes = jogador.getSaldo();

            boolean querComprar = true;
            if (jogador.getSaldo() >= clube.getValorAquisicao() && querComprar) {
                jogador.debitar(clube.getValorAquisicao());
                jogador.adicionarClube(clube);
                clube.setDono(jogador);
                System.out.println(">> " + jogador.getNome() + " comprou o " + clube.getNome()
                        + " por R$ " + clube.getValorAquisicao()
                        + " | Saldo: R$ " + saldoAntes + " → R$ " + jogador.getSaldo());
            } else {
                clube.registrarVisita();
                System.out.println(">> " + jogador.getNome() + " não comprou o " + clube.getNome()
                        + ". Multiplicador agora é " + clube.getMultiplicador());
            }

        } else if (dono == jogador) {
            // DELE MESMO
            // só imprime uma mensagem
            System.out.println(">> " + clube.getNome() + " já pertence a " + jogador.getNome() + ". Nada acontece.");

        } else {
            double taxa = clube.calcularTaxa();
            double saldoVisitanteAntes = jogador.getSaldo();
            double saldoDonoAntes = dono.getSaldo();

            cobrar(jogador, dono, taxa);
            dono.registrarTaxaRecebida(taxa);


            System.out.println(">> " + jogador.getNome() + " parou no " + clube.getNome() + ", que pertence a " + dono.getNome());
            System.out.println("   Taxa: R$ " + clube.getTaxaMandoBase() + " × " + clube.getMultiplicador()
                    + " = R$ " + taxa);
            System.out.println("   " + jogador.getNome() + ": R$ " + saldoVisitanteAntes + " → R$ " + jogador.getSaldo());
            System.out.println("   " + dono.getNome() + ": R$ " + saldoDonoAntes + " → R$ " + dono.getSaldo());

            clube.registrarVisita();
            System.out.println("   Multiplicador do " + clube.getNome() + " agora é " + clube.getMultiplicador());
        }


    }


    private void cobrar(Jogador devedor, Jogador credor, double valor) {
        devedor.debitar(valor);
        if (credor != null) {
            credor.receber(valor);
        }
    }
}