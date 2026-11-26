package jogo;

import estruturas.Fila;
import estruturas.ListaDuplaCircular;
import modelo.*;
import ui.Entrada;

import java.util.ArrayList;
import java.util.Random;

import static ui.Formatador.dinheiro;

public class Jogo {

    private final Configuracao config;
    private final Entrada entrada;
    private final ArrayList<Jogador> jogadores;
    private final ArrayList<Carta> cartasDoJogo;
    private final ListaDuplaCircular<Casa> tabuleiro;
    private final Baralho baralho;
    private final Random random;
    private final Fila<EntradaHistorico> historico;
    private final ArrayList<Jogador> ordemEliminacao = new ArrayList<>();

    private double patrocinio;
    private int maxRodadas;
    private int rodadaAtual = 1;
    private int indiceProximoJogador = 0;   // de quem é a vez (posição no ArrayList de jogadores)
    private String efeitoTurno = "";        // resumo do que aconteceu no turno (vai para o histórico)

    // Estatística do relatório final: clube com a maior taxa de mando cobrada na partida
    private Clube clubeMaiorTaxa = null;
    private double maiorTaxaCobrada = 0;

    /**
     * Monta a partida: Random com a semente, tabuleiro, baralho, histórico,
     * e coloca todos os jogadores no Início com o saldo inicial configurado.
     */
    public Jogo(ArrayList<Jogador> jogadores, ArrayList<Clube> clubes, Configuracao config, Entrada entrada) {
        this.jogadores = jogadores;
        this.config = config;
        this.entrada = entrada;
        this.patrocinio = config.getPatrocinio();
        this.maxRodadas = config.getMaxRodadas();

        this.random = new Random(config.getSemente());
        this.tabuleiro = Tabuleiro.montar(clubes);
        this.cartasDoJogo = Baralho.criarCartasPadrao(config.getCasasCartaVolta());
        this.baralho = new Baralho(cartasDoJogo, random);
        this.historico = new Fila<>(config.getCapacidadeHistorico());

        for (Clube clube : clubes) {
            clube.voltarAoBanco();   // todo clube começa sem dono e com multiplicador 1,0
        }
        for (Jogador jogador : jogadores) {
            jogador.prepararParaPartida(config.getSaldoInicial(), tabuleiro.getInicio());
        }
    }

    public void exibirTabuleiro() {
        System.out.println("--- TABULEIRO (" + tabuleiro.tamanho() + " casas) ---");
        tabuleiro.exibir();
    }

    // ===================== TURNO E RODADA =====================

    // Joga o turno de quem é a vez (pulando eliminados)
    public void jogarProximoTurno() {
        if (jogoTerminou()) {
            return;
        }
        while (jogadores.get(indiceProximoJogador).isEliminado()) {
            avancarVez();
        }
        Jogador jogador = jogadores.get(indiceProximoJogador);

        int dado1;
        int dado2;
        if (config.isModoTeste()) {
            System.out.println("[MODO DE TESTE] Vez de " + jogador.getNome());
            dado1 = entrada.lerInt("Valor do dado 1 (1-6): ", 1, 6);
            dado2 = entrada.lerInt("Valor do dado 2 (1-6): ", 1, 6);
        } else {
            dado1 = lancarDado();
            dado2 = lancarDado();
        }

        jogarTurno(jogador, dado1, dado2);
        avancarVez();
    }

    // Passa a vez para o próximo jogador; quando todos jogaram, começa uma nova rodada
    private void avancarVez() {
        indiceProximoJogador++;
        if (indiceProximoJogador == jogadores.size()) {
            indiceProximoJogador = 0;
            rodadaAtual++;
        }
    }

    // Turno: dados → anda nó a nó → efeito da casa → histórico
    private void jogarTurno(Jogador jogador, int dado1, int dado2) {
        double saldoAntes = jogador.getSaldo();
        efeitoTurno = "";

        System.out.println();
        System.out.println("=== Rodada " + rodadaAtual + " | Vez de " + jogador.getNome() + " ===");
        System.out.println("Posição atual: " + jogador.getPosicaoAtual().getDado() + " | Saldo: " + dinheiro(saldoAntes));
        System.out.println("Dados: " + dado1 + " + " + dado2 + " = " + (dado1 + dado2));

        moverParaFrente(jogador, dado1 + dado2);

        String casaDestino = jogador.getPosicaoAtual().getDado().toString();
        System.out.println("Casa de destino: " + casaDestino);

        aplicarEfeitoDaCasa(jogador, true);

        if (efeitoTurno.isEmpty()) {
            efeitoTurno = "Nenhum efeito";
        }
        historico.enfileirar(new EntradaHistorico(rodadaAtual, jogador.getNome(), dado1, dado2, casaDestino, efeitoTurno));

        if (!jogador.isEliminado()) {
            System.out.println("Saldo de " + jogador.getNome() + ": " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
        }
    }

    // Modo automático: joga até o fim da partida sem precisar digitar cada ação
    public void simularAteOFim() {
        while (!jogoTerminou()) {
            jogarProximoTurno();
        }
    }

    // O jogo termina ao passar do máximo de rodadas ou quando sobra um só jogador
    public boolean jogoTerminou() {
        return rodadaAtual > maxRodadas || contarJogadoresAtivos() <= 1;
    }

    private int contarJogadoresAtivos() {
        int ativos = 0;
        for (Jogador jogador : jogadores) {
            if (!jogador.isEliminado()) {
                ativos++;
            }
        }
        return ativos;
    }

    private int lancarDado() {
        int min = 1;
        int max = 6;

        return random.nextInt(min, max + 1);
    }

    // Junta um efeito ao resumo do turno (um turno pode ter mais de um: passou pelo Início E comprou um clube)
    private void registrarEfeito(String efeito) {
        if (efeitoTurno.isEmpty()) {
            efeitoTurno = efeito;
        } else {
            efeitoTurno += "; " + efeito;
        }
    }

    public void exibirHistorico() {
        System.out.println("--- HISTÓRICO (últimas " + historico.tamanho() + " entradas, capacidade "
                + config.getCapacidadeHistorico() + ") ---");
        historico.exibir();
    }

    public void exibirEstado() {
        System.out.println("--- ESTADO DO JOGO (rodada " + Math.min(rodadaAtual, maxRodadas) + " de " + maxRodadas + ") ---");
        for (Jogador jogador : jogadores) {
            if (jogador.isEliminado()) {
                System.out.println(jogador.getNome() + " | ELIMINADO");
                continue;
            }
            System.out.println(jogador.getNome()
                    + " | Saldo: " + dinheiro(jogador.getSaldo())
                    + " | Patrimônio: " + dinheiro(jogador.calcularPatrimonio())
                    + " | Casa: " + jogador.getPosicaoAtual().getDado()
                    + " | Temporadas: " + jogador.getTemporadasCompletas()
                    + " | Voltas pendentes: " + jogador.getVoltasPendentes());
            System.out.print("   Clubes (" + jogador.getClubesAdquiridos().size() + "): ");
            for (Clube clube : jogador.getClubesAdquiridos()) {
                System.out.print(clube.getNome() + " (x" + clube.getMultiplicador() + ")  ");
            }
            System.out.println();
        }
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    // ===================== MOVIMENTO =====================

    public void moverParaFrente(Jogador jogador, int passos) {
        for (int i = 0; i < passos; i++) {

            jogador.avancarUmaCasa();

            if (jogador.getPosicaoAtual().getDado().getTipo() == TipoCasa.INICIO) {
                if (jogador.getVoltasPendentes() > 0) {
                    jogador.quitarVoltaPendente();
                    System.out.println(">> " + jogador.getNome() + " passou pelo Início, " +
                            "mas tinha uma volta pendente (reposição de volta). Sem patrocínio.");
                    registrarEfeito("Reposição de volta (sem patrocínio)");
                } else {
                    double saldoAntes = jogador.getSaldo();
                    jogador.receber(patrocinio);
                    jogador.completarTemporada();
                    System.out.println(">> Temporada concluída! " + jogador.getNome() + " recebeu " + dinheiro(patrocinio)
                            + " de patrocínio | Saldo: " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
                    registrarEfeito("Patrocínio de " + dinheiro(patrocinio));
                }
            }
        }
    }

    public void moverParaTras(Jogador jogador, int passos) {
        for (int i = 0; i < passos; i++) {

            if (jogador.getPosicaoAtual().getDado().getTipo() == TipoCasa.INICIO) {
                jogador.adicionarVoltaPendente();
                System.out.println(">> " + jogador.getNome() + " saiu do Início andando para trás. "
                        + "Volta pendente registrada (não receberá patrocínio na próxima passagem).");
            }

            jogador.recuarUmaCasa();
        }
    }

    // ===================== CASAS =====================

    public void pararEmClube(Jogador jogador) {
        Clube clube = jogador.getPosicaoAtual().getDado().getClube();
        Jogador dono = clube.getDono();

        if (dono == null) {
            double saldoAntes = jogador.getSaldo();

            if (jogador.getSaldo() >= clube.getValorAquisicao() && decidirCompra(jogador, clube)) {
                jogador.debitar(clube.getValorAquisicao());
                jogador.adicionarClube(clube);
                clube.setDono(jogador);
                System.out.println(">> " + jogador.getNome() + " comprou o " + clube.getNome()
                        + " por " + dinheiro(clube.getValorAquisicao())
                        + " | Saldo: " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
                registrarEfeito("Comprou o " + clube.getNome() + " por " + dinheiro(clube.getValorAquisicao()));
            } else {
                clube.registrarVisita();
                System.out.println(">> " + jogador.getNome() + " não comprou o " + clube.getNome()
                        + ". Multiplicador agora é " + clube.getMultiplicador());
                registrarEfeito("Não comprou o " + clube.getNome());
            }

        } else if (dono == jogador) {
            System.out.println(">> " + clube.getNome() + " já pertence a " + jogador.getNome() + ". Nada acontece.");
            registrarEfeito("Clube próprio");

        } else {
            double taxa = clube.calcularTaxa();
            double saldoVisitanteAntes = jogador.getSaldo();
            double saldoDonoAntes = dono.getSaldo();

            if (taxa > maiorTaxaCobrada) {
                maiorTaxaCobrada = taxa;
                clubeMaiorTaxa = clube;
            }

            System.out.println(">> " + jogador.getNome() + " parou no " + clube.getNome() + ", que pertence a " + dono.getNome());
            System.out.println("   Taxa: " + dinheiro(clube.getTaxaMandoBase()) + " × " + clube.getMultiplicador()
                    + " = " + dinheiro(taxa));

            double pago = cobrar(jogador, dono, taxa);
            dono.registrarTaxaRecebida(pago);

            System.out.println("   " + jogador.getNome() + ": " + dinheiro(saldoVisitanteAntes) + " → " + dinheiro(jogador.getSaldo()));
            System.out.println("   " + dono.getNome() + ": " + dinheiro(saldoDonoAntes) + " → " + dinheiro(dono.getSaldo()));

            clube.registrarVisita();
            System.out.println("   Multiplicador do " + clube.getNome() + " agora é " + clube.getMultiplicador());
            registrarEfeito("Pagou " + dinheiro(pago) + " de taxa de mando a " + dono.getNome());
        }
    }

    /**
     * Decide se o jogador compra o clube.
     * Modo automático: compra se o saldo depois da compra ficar acima de 10% do saldo inicial
     * (sugestão do enunciado). Modo manual: pergunta no teclado.
     */
    private boolean decidirCompra(Jogador jogador, Clube clube) {
        if (config.isModoAutomatico()) {
            return jogador.getSaldo() - clube.getValorAquisicao() > config.getSaldoInicial() * 0.10;
        }
        return entrada.lerSimNao(jogador.getNome() + ", deseja comprar o " + clube.getNome()
                + " por " + dinheiro(clube.getValorAquisicao()) + "? Seu saldo: " + dinheiro(jogador.getSaldo()));
    }

    public void pararEmFolhaSalarial(Jogador jogador) {
        if (!jogador.getClubesAdquiridos().isEmpty()) {
            double saldoAntes = jogador.getSaldo();
            int quantidade = jogador.getClubesAdquiridos().size();
            double total = 0;
            for (Clube time : jogador.getClubesAdquiridos()) {
                total += time.getValorAquisicao() * 0.01;
            }
            cobrar(jogador, null, total);
            System.out.println(">> " + jogador.getNome() + " pagou " + dinheiro(total) + " de folha salarial ("
                    + quantidade + " clubes) | Saldo: " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
            registrarEfeito("Pagou " + dinheiro(total) + " de folha salarial");
        } else {
            System.out.println(">> " + jogador.getNome() + " não possui clubes e não paga folha salarial.");
            registrarEfeito("Folha salarial: sem clubes, nada a pagar");
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
            System.out.println(">> " + jogador.getNome() + " recebeu " + dinheiro(total) + " de premiação ("
                    + jogador.getClubesAdquiridos().size() + " clubes) | Saldo: " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
            registrarEfeito("Recebeu " + dinheiro(total) + " de premiação");
        } else {
            System.out.println(">> " + jogador.getNome() + " não possui clubes e não recebe premiação.");
            registrarEfeito("Premiação: sem clubes, nada a receber");
        }
    }

    // ===================== SORTE / REVÉS =====================

    public void pararEmSorteReves(Jogador jogador) {
        if (config.isModoTeste()) {
            escolherCartaDoTopo();
        }

        Carta carta = baralho.comprar();
        System.out.println(">> " + jogador.getNome() + " tirou a carta do topo da pilha de Sorte/Revés:");
        System.out.println("   \"" + carta.getDescricao() + "\"");
        registrarEfeito("Carta: " + carta.getDescricao());

        aplicarCarta(jogador, carta);

        baralho.descartar(carta);   // depois de aplicada, vai para a pilha de descarte
        System.out.println("   (a carta foi para a pilha de descarte)");
    }

    // MODO DE TESTE: escolher qual carta vai estar no topo da pilha
    private void escolherCartaDoTopo() {
        System.out.println("[MODO DE TESTE] Escolha a carta do topo da pilha:");
        System.out.println("0 - Não escolher (usar a carta que já está no topo)");
        for (int i = 0; i < cartasDoJogo.size(); i++) {
            System.out.println((i + 1) + " - " + cartasDoJogo.get(i).getDescricao());
        }
        int opcao = entrada.lerInt("Carta: ", 0, cartasDoJogo.size());
        if (opcao > 0) {
            baralho.trazerParaTopo(cartasDoJogo.get(opcao - 1));
        }
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
                System.out.println("   5% de " + dinheiro(jogador.calcularPatrimonio()) + " = " + dinheiro(perda));
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
            System.out.println("   Saldo de " + jogador.getNome() + ": " + dinheiro(saldoAntes) + " → " + dinheiro(jogador.getSaldo()));
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

    // ===================== PAGAMENTOS =====================

    /**
     * Cobrança padrão do jogo (item 2.4 do enunciado).
     * devedor paga "valor" ao credor (credor == null significa o BANCO).
     *
     * 1) Se o saldo cobre, debita normalmente.
     * 2) Se não cobre: VENDA FORÇADA. Vende clubes ao banco, do menor para o maior
     *    valor original, por 50% do valor, até cobrir a dívida.
     * 3) Se nem vendendo tudo cobre: FALÊNCIA. O credor recebe tudo o que o
     *    devedor tinha, e o devedor é eliminado.
     *
     * Retorna quanto foi pago de verdade (na falência, pode ser menos que "valor").
     */
    private double cobrar(Jogador devedor, Jogador credor, double valor) {

        // 2) Venda forçada, se precisar
        if (devedor.getSaldo() < valor && !devedor.getClubesAdquiridos().isEmpty()) {
            System.out.println("   !! " + devedor.getNome() + " não tem saldo para pagar " + dinheiro(valor)
                    + " (saldo: " + dinheiro(devedor.getSaldo()) + "). Venda forçada:");

            while (devedor.getSaldo() < valor && !devedor.getClubesAdquiridos().isEmpty()) {
                Clube maisBarato = encontrarClubeMaisBarato(devedor);
                double valorVenda = maisBarato.getValorAquisicao() * 0.5;

                devedor.removerClube(maisBarato);
                maisBarato.voltarAoBanco();
                devedor.receber(valorVenda);

                System.out.println("      - Vendeu " + maisBarato.getNome() + " ao banco por " + dinheiro(valorVenda)
                        + " (50% de " + dinheiro(maisBarato.getValorAquisicao()) + "). O clube voltou ao banco.");
            }
            registrarEfeito(devedor.getNome() + " fez venda forçada");
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
        System.out.println("   XX " + devedor.getNome() + " FALIU! Não conseguiu pagar " + dinheiro(valor)
                + ". " + quemRecebe + " recebeu tudo o que restava (" + dinheiro(tudoQueTinha) + ").");
        System.out.println("   XX " + devedor.getNome() + " foi eliminado. Patrimônio final: R$ 0,00. Seus clubes voltaram ao banco.");
        registrarEfeito(devedor.getNome() + " FALIU e foi eliminado");

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

    // ===================== RELATÓRIO FINAL =====================

    public void exibirRelatorioFinal() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("               RELATÓRIO FINAL");
        System.out.println("==============================================");
        System.out.println("Rodadas jogadas: " + Math.min(rodadaAtual - 1, maxRodadas)
                + (contarJogadoresAtivos() <= 1 ? " (restou um único jogador)" : " (limite de rodadas)"));

        // 1. Classificação: ativos ordenados por patrimônio + eliminados na ordem inversa de eliminação
        ArrayList<Jogador> ativos = new ArrayList<>();
        for (Jogador jogador : jogadores) {
            if (!jogador.isEliminado()) {
                ativos.add(jogador);
            }
        }
        ArrayList<Jogador> classificacao = Ranking.ordenarPorPatrimonio(ativos);
        for (int i = ordemEliminacao.size() - 1; i >= 0; i--) {
            classificacao.add(ordemEliminacao.get(i));
        }

        System.out.println();
        System.out.println("--- CLASSIFICAÇÃO (por patrimônio total) ---");
        for (int i = 0; i < classificacao.size(); i++) {
            Jogador jogador = classificacao.get(i);
            System.out.println((i + 1) + "º " + jogador.getNome()
                    + " | Patrimônio: " + dinheiro(jogador.calcularPatrimonio())
                    + " | Saldo: " + dinheiro(jogador.getSaldo())
                    + (jogador.isEliminado() ? " | ELIMINADO" : ""));
        }

        // 2 e 3. Temporadas e quantidade de clubes de cada jogador
        System.out.println();
        System.out.println("--- ESTATÍSTICAS DOS JOGADORES ---");
        for (Jogador jogador : jogadores) {
            System.out.println(jogador.getNome()
                    + " | Temporadas completas: " + jogador.getTemporadasCompletas()
                    + " | Clubes: " + jogador.getClubesAdquiridos().size()
                    + " | Recebido em taxas de mando: " + dinheiro(jogador.getTotalRecebidoTaxas()));
        }

        // 4. Clube com a maior taxa de mando cobrada na partida
        System.out.println();
        if (clubeMaiorTaxa != null) {
            System.out.println("Clube com a maior taxa de mando cobrada: " + clubeMaiorTaxa.getNome()
                    + " (" + dinheiro(maiorTaxaCobrada) + ")");
        } else {
            System.out.println("Nenhuma taxa de mando foi cobrada na partida.");
        }

        // 5. Jogador que mais arrecadou em taxas de mando
        Jogador maiorArrecadador = null;
        for (Jogador jogador : jogadores) {
            if (maiorArrecadador == null || jogador.getTotalRecebidoTaxas() > maiorArrecadador.getTotalRecebidoTaxas()) {
                maiorArrecadador = jogador;
            }
        }
        System.out.println("Jogador que mais arrecadou em taxas de mando: " + maiorArrecadador.getNome()
                + " (" + dinheiro(maiorArrecadador.getTotalRecebidoTaxas()) + ")");

        // 7. Histórico das últimas N rodadas
        System.out.println();
        exibirHistorico();

        // 8. Campeão: primeiro da classificação
        System.out.println();
        System.out.println("##############################################");
        System.out.println("   CAMPEÃO: " + classificacao.get(0).getNome()
                + " com " + dinheiro(classificacao.get(0).calcularPatrimonio()) + " de patrimônio!");
        System.out.println("##############################################");
    }
}
