package jogo;

import modelo.Carta;
import modelo.Clube;
import modelo.Jogador;
import modelo.TipoCasa;

import java.util.ArrayList;

public class Jogo {

    private double patrocinio = 100000;
    private ArrayList<Jogador> ordemEliminacao = new ArrayList<>();
    private ArrayList<Jogador> jogadores;
    private Baralho baralho;

    // TEMPORÁRIO: quando fizermos o "Iniciar partida", o Jogo vai montar tudo sozinho
    public Jogo(ArrayList<Jogador> jogadores, Baralho baralho) {
        this.jogadores = jogadores;
        this.baralho = baralho;
    }

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

            double pago = cobrar(jogador, dono, taxa);
            dono.registrarTaxaRecebida(pago);


            System.out.println(">> " + jogador.getNome() + " parou no " + clube.getNome() + ", que pertence a " + dono.getNome());
            System.out.println("   Taxa: R$ " + clube.getTaxaMandoBase() + " × " + clube.getMultiplicador()
                    + " = R$ " + taxa);
            System.out.println("   " + jogador.getNome() + ": R$ " + saldoVisitanteAntes + " → R$ " + jogador.getSaldo());
            System.out.println("   " + dono.getNome() + ": R$ " + saldoDonoAntes + " → R$ " + dono.getSaldo());

            clube.registrarVisita();
            System.out.println("   Multiplicador do " + clube.getNome() + " agora é " + clube.getMultiplicador());
        }


    }

    public void pararEmFolhaSalarial(Jogador jogador) {
        if (!jogador.getClubesAdquiridos().isEmpty()) {
            double saldoAntes = jogador.getSaldo();
            double total = 0;
            for (Clube time : jogador.getClubesAdquiridos()) {
                total += time.getValorAquisicao() * 0.01;
            }
            cobrar(jogador, null, total);
            System.out.println(">> " + jogador.getNome() + " pagou R$ " + total + " de folha salarial ("
                    + jogador.getClubesAdquiridos().size() + " clubes) | Saldo: R$ " + saldoAntes + " → R$ " + jogador.getSaldo());
        } else {
            System.out.println(">> " + jogador.getNome() + " não possui clubes e não paga folha salarial.");
        }
    }
    public void pararEmPremiacao(Jogador jogador) {
        if (!jogador.getClubesAdquiridos().isEmpty()) {
            double saldoAntes = jogador.getSaldo();
            double total = 0;
            for (Clube time : jogador.getClubesAdquiridos()) {
                total += time.getValorAquisicao() * 0.02;
            }
            jogador.receber(total);
            System.out.println(">> " + jogador.getNome() + " recebeu R$ " + total + " de premiação ("
                    + jogador.getClubesAdquiridos().size() + " clubes) | Saldo: R$ " + saldoAntes + " → R$ " + jogador.getSaldo());
        } else {
            System.out.println(">> " + jogador.getNome() + " não possui clubes e não recebe premiação.");
        }
    }

    // ===================== SORTE / REVÉS =====================

    public void pararEmSorteReves(Jogador jogador) {
        Carta carta = baralho.comprar();
        System.out.println(">> " + jogador.getNome() + " tirou uma carta de Sorte/Revés:");
        System.out.println("   \"" + carta.getDescricao() + "\"");

        aplicarCarta(jogador, carta);

        baralho.descartar(carta);   // depois de aplicada, vai para a pilha de descarte
    }

    private void aplicarCarta(Jogador jogador, Carta carta) {
        double saldoAntes = jogador.getSaldo();

        switch (carta.getEfeito()) {
            case RECEBER_BANCO -> jogador.receber(carta.getValor());

            case PAGAR_BANCO -> cobrar(jogador, null, carta.getValor());

            case RECEBER_TODOS -> {
                for (Jogador outro : jogadores) {
                    if (outro != jogador && !outro.isEliminado()) {
                        cobrar(outro, jogador, carta.getValor());
                    }
                }
            }

            case PAGAR_TODOS -> {
                for (Jogador outro : jogadores) {
                    if (jogador.isEliminado()) break;   // faliu no meio: para de pagar
                    if (outro != jogador && !outro.isEliminado()) {
                        cobrar(jogador, outro, carta.getValor());
                    }
                }
            }

            case PERDER_PORCENTAGEM -> {
                double perda = jogador.calcularPatrimonio() * carta.getValor() / 100;
                perda = Math.round(perda * 100) / 100.0;
                cobrar(jogador, null, perda);
            }

            case AVANCAR -> {
                moverParaFrente(jogador, (int) carta.getValor());
                System.out.println("   " + jogador.getNome() + " avançou até " + jogador.getPosicaoAtual().getDado());
                aplicarEfeitoDaCasa(jogador, false);
            }

            case RECUAR -> {
                moverParaTras(jogador, (int) carta.getValor());
                System.out.println("   " + jogador.getNome() + " voltou até " + jogador.getPosicaoAtual().getDado());
                aplicarEfeitoDaCasa(jogador, false);
            }

            case IR_PARA_INICIO -> {
                // anda nó a nó até chegar no Início (as regras do Início valem normalmente)
                do {
                    moverParaFrente(jogador, 1);
                } while (jogador.getPosicaoAtual().getDado().getTipo() != TipoCasa.INICIO);
            }
        }

        if (!jogador.isEliminado()) {
            System.out.println("   Saldo de " + jogador.getNome() + ": R$ " + saldoAntes + " → R$ " + jogador.getSaldo());
        }
    }

    // ===================== EFEITO DA CASA =====================

    /**
     * Aplica o efeito da casa onde o jogador está.
     * podeTirarCarta = false quando o jogador chegou aqui por causa de uma carta
     * (o enunciado diz que, nesse caso, Sorte/Revés não sorteia uma nova carta).
     */
    public void aplicarEfeitoDaCasa(Jogador jogador, boolean podeTirarCarta) {
        switch (jogador.getPosicaoAtual().getDado().getTipo()) {
            case INICIO -> { }   // o patrocínio já é tratado durante o movimento
            case CLUBE -> pararEmClube(jogador);
            case FOLHA_SALARIAL -> pararEmFolhaSalarial(jogador);
            case PREMIACAO -> pararEmPremiacao(jogador);
            case SORTE_REVES -> {
                if (podeTirarCarta) {
                    pararEmSorteReves(jogador);
                } else {
                    System.out.println("   Caiu em Sorte/Revés por causa de uma carta: nenhuma nova carta é sorteada.");
                }
            }
        }
    }

    private double cobrar(Jogador devedor, Jogador credor, double valor) {

        // 2) Venda forçada, se precisar
        if (devedor.getSaldo() < valor && !devedor.getClubesAdquiridos().isEmpty()) {
            System.out.println("   !! " + devedor.getNome() + " não tem saldo para pagar R$ " + valor
                    + " (saldo: R$ " + devedor.getSaldo() + "). Venda forçada:");

            while (devedor.getSaldo() < valor && !devedor.getClubesAdquiridos().isEmpty()) {
                Clube maisBarato = encontrarClubeMaisBarato(devedor);
                double valorVenda = maisBarato.getValorAquisicao() * 0.5;

                devedor.removerClube(maisBarato);
                maisBarato.voltarAoBanco();
                devedor.receber(valorVenda);

                System.out.println("      - Vendeu " + maisBarato.getNome() + " ao banco por R$ " + valorVenda
                        + " (50% de R$ " + maisBarato.getValorAquisicao() + "). O clube voltou ao banco.");
            }
        }

        // 1) Saldo cobre (de primeira, ou depois da venda forçada)
        if (devedor.getSaldo() >= valor) {
            devedor.debitar(valor);
            if (credor != null) {
                credor.receber(valor);
            }
            return valor;
        }

        // 3) Falência: nem vendendo tudo deu
        double tudoQueTinha = devedor.getSaldo();
        if (credor != null) {
            credor.receber(tudoQueTinha);
        }
        devedor.eliminar();
        ordemEliminacao.add(devedor);

        String quemRecebe = (credor != null) ? credor.getNome() : "o banco";
        System.out.println("   XX " + devedor.getNome() + " FALIU! Não conseguiu pagar R$ " + valor
                + ". " + quemRecebe + " recebeu tudo o que restava (R$ " + tudoQueTinha + ").");
        System.out.println("   XX " + devedor.getNome() + " foi eliminado. Patrimônio final: R$ 0,00. Seus clubes voltaram ao banco.");

        return tudoQueTinha;
    }

    // Percorre os clubes do jogador e devolve o de menor valor original
    private Clube encontrarClubeMaisBarato(Jogador jogador) {
        Clube maisBarato = null;
        for (Clube clube : jogador.getClubesAdquiridos()) {
            if (maisBarato == null || clube.getValorAquisicao() < maisBarato.getValorAquisicao()) {
                maisBarato = clube;
            }
        }
        return maisBarato;
    }
}