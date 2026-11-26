package ui;

import jogo.Configuracao;
import jogo.Jogo;
import modelo.Clube;
import modelo.Jogador;

import java.util.ArrayList;

import static ui.Formatador.dinheiro;

/**
 * Menus do jogo: configuração (cadastros, configurações, dados de teste) e partida.
 */
public class Menu {

    private static final String NOME_EQUIPE = "Equipe Individual - Matheus Schuh";
    private static final int MAX_CLUBES = 40;
    private static final int MIN_CLUBES = 10;
    private static final int MAX_JOGADORES = 6;
    private static final int MIN_JOGADORES = 2;

    private final Entrada entrada = new Entrada();
    private final Configuracao config = new Configuracao();
    private final ArrayList<Clube> clubes = new ArrayList<>();
    private final ArrayList<Jogador> jogadores = new ArrayList<>();

    public void iniciar() {
        System.out.println("==============================================");
        System.out.println("               FUTEBOL MANAGER");
        System.out.println("   Equipe: " + NOME_EQUIPE);
        System.out.println("==============================================");

        while (true) {
            System.out.println();
            System.out.println("--- MENU PRINCIPAL (configuração) ---");
            System.out.println("1 - Gerenciar clubes");
            System.out.println("2 - Gerenciar jogadores");
            System.out.println("3 - Configurações da partida");
            System.out.println("4 - Carregar dados de teste (clubes e jogadores)");
            System.out.println("5 - Iniciar partida");
            System.out.println("0 - Sair");
            int opcao = entrada.lerInt("Opção: ", 0, 5);

            switch (opcao) {
                case 1 -> menuClubes();
                case 2 -> menuJogadores();
                case 3 -> menuConfiguracoes();
                case 4 -> carregarDadosDeTeste();
                case 5 -> {
                    if (iniciarPartida()) {
                        return;   // depois do relatório final, o programa termina
                    }
                }
                case 0 -> {
                    System.out.println("Até a próxima!");
                    return;
                }
            }
        }
    }

    // ===================== CLUBES (CRUD) =====================

    private void menuClubes() {
        while (true) {
            System.out.println();
            System.out.println("--- CLUBES (" + clubes.size() + "/" + MAX_CLUBES + ") ---");
            System.out.println("1 - Cadastrar | 2 - Listar | 3 - Atualizar | 4 - Remover | 0 - Voltar");
            int opcao = entrada.lerInt("Opção: ", 0, 4);

            switch (opcao) {
                case 1 -> cadastrarClube();
                case 2 -> listarClubes();
                case 3 -> atualizarClube();
                case 4 -> removerClube();
                case 0 -> { return; }
            }
        }
    }

    private void cadastrarClube() {
        if (clubes.size() >= MAX_CLUBES) {
            System.out.println("Limite de " + MAX_CLUBES + " clubes atingido.");
            return;
        }
        String nome = lerNomeClubeUnico(null);
        double valor = entrada.lerDoublePositivo("Valor de aquisição: ");
        double taxa = entrada.lerDoublePositivo("Taxa de mando base: ");
        clubes.add(new Clube(nome, valor, taxa));
        System.out.println("Clube " + nome + " cadastrado.");
    }

    private void listarClubes() {
        if (clubes.isEmpty()) {
            System.out.println("Nenhum clube cadastrado.");
            return;
        }
        System.out.println();
        System.out.println("--- LISTA DE CLUBES ---");
        System.out.printf("%-4s %-22s %-22s %-20s%n", "Nº", "Nome", "Valor de aquisição", "Taxa de mando base");
        for (int i = 0; i < clubes.size(); i++) {
            Clube clube = clubes.get(i);
            System.out.printf("%-4d %-22s %-22s %-20s%n", i + 1, clube.getNome(),
                    dinheiro(clube.getValorAquisicao()), dinheiro(clube.getTaxaMandoBase()));
        }
    }

    private void atualizarClube() {
        if (clubes.isEmpty()) {
            System.out.println("Nenhum clube cadastrado.");
            return;
        }
        listarClubes();
        int numero = entrada.lerInt("Número do clube a atualizar (0 para cancelar): ", 0, clubes.size());
        if (numero == 0) return;

        Clube clube = clubes.get(numero - 1);
        clube.setNome(lerNomeClubeUnico(clube));
        clube.setValorAquisicao(entrada.lerDoublePositivo("Novo valor de aquisição: "));
        clube.setTaxaMandoBase(entrada.lerDoublePositivo("Nova taxa de mando base: "));
        System.out.println("Clube atualizado.");
    }

    private void removerClube() {
        if (clubes.isEmpty()) {
            System.out.println("Nenhum clube cadastrado.");
            return;
        }
        listarClubes();
        int numero = entrada.lerInt("Número do clube a remover (0 para cancelar): ", 0, clubes.size());
        if (numero == 0) return;

        Clube removido = clubes.remove(numero - 1);
        System.out.println("Clube " + removido.getNome() + " removido.");
    }

    // Lê um nome que ainda não existe ("atual" é o próprio clube, ao atualizar)
    private String lerNomeClubeUnico(Clube atual) {
        while (true) {
            String nome = entrada.lerTexto("Nome do clube: ");
            boolean repetido = false;
            for (Clube clube : clubes) {
                if (clube != atual && clube.getNome().equalsIgnoreCase(nome)) {
                    repetido = true;
                }
            }
            if (!repetido) return nome;
            System.out.println("Já existe um clube com esse nome.");
        }
    }

    // ===================== JOGADORES (CRUD) =====================

    private void menuJogadores() {
        while (true) {
            System.out.println();
            System.out.println("--- JOGADORES (" + jogadores.size() + "/" + MAX_JOGADORES + ") ---");
            System.out.println("1 - Cadastrar | 2 - Listar | 3 - Atualizar | 4 - Remover | 0 - Voltar");
            int opcao = entrada.lerInt("Opção: ", 0, 4);

            switch (opcao) {
                case 1 -> cadastrarJogador();
                case 2 -> listarJogadores();
                case 3 -> atualizarJogador();
                case 4 -> removerJogador();
                case 0 -> { return; }
            }
        }
    }

    private void cadastrarJogador() {
        if (jogadores.size() >= MAX_JOGADORES) {
            System.out.println("Limite de " + MAX_JOGADORES + " jogadores atingido.");
            return;
        }
        String nome = lerNomeJogadorUnico(null);
        jogadores.add(new Jogador(nome));
        System.out.println("Jogador " + nome + " cadastrado (ordem de turno: " + jogadores.size() + "º).");
    }

    private void listarJogadores() {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        System.out.println();
        System.out.println("--- LISTA DE JOGADORES (na ordem dos turnos) ---");
        for (int i = 0; i < jogadores.size(); i++) {
            System.out.println((i + 1) + " - " + jogadores.get(i).getNome());
        }
        System.out.println("Saldo inicial de cada um: " + dinheiro(config.getSaldoInicial()));
    }

    private void atualizarJogador() {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        listarJogadores();
        int numero = entrada.lerInt("Número do jogador a atualizar (0 para cancelar): ", 0, jogadores.size());
        if (numero == 0) return;

        Jogador jogador = jogadores.get(numero - 1);
        jogador.setNome(lerNomeJogadorUnico(jogador));
        System.out.println("Jogador atualizado.");
    }

    private void removerJogador() {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        listarJogadores();
        int numero = entrada.lerInt("Número do jogador a remover (0 para cancelar): ", 0, jogadores.size());
        if (numero == 0) return;

        Jogador removido = jogadores.remove(numero - 1);
        System.out.println("Jogador " + removido.getNome() + " removido.");
    }

    private String lerNomeJogadorUnico(Jogador atual) {
        while (true) {
            String nome = entrada.lerTexto("Nome do jogador: ");
            boolean repetido = false;
            for (Jogador jogador : jogadores) {
                if (jogador != atual && jogador.getNome().equalsIgnoreCase(nome)) {
                    repetido = true;
                }
            }
            if (!repetido) return nome;
            System.out.println("Já existe um jogador com esse nome.");
        }
    }

    // ===================== CONFIGURAÇÕES =====================

    private void menuConfiguracoes() {
        while (true) {
            System.out.println();
            System.out.println("--- CONFIGURAÇÕES DA PARTIDA ---");
            System.out.println("1 - Saldo inicial dos jogadores: " + dinheiro(config.getSaldoInicial()));
            System.out.println("2 - Patrocínio por volta completa: " + dinheiro(config.getPatrocinio()));
            System.out.println("3 - Número máximo de rodadas: " + config.getMaxRodadas());
            System.out.println("4 - Capacidade do histórico: " + config.getCapacidadeHistorico());
            System.out.println("5 - Semente do gerador aleatório: " + config.getSemente());
            System.out.println("6 - Modo automático (sistema decide as compras): " + ligado(config.isModoAutomatico()));
            System.out.println("7 - Modo de teste (dados e carta do topo manuais): " + ligado(config.isModoTeste()));
            System.out.println("8 - Casas que a carta \"Volte\" faz recuar: " + config.getCasasCartaVolta());
            System.out.println("0 - Voltar");
            int opcao = entrada.lerInt("Opção: ", 0, 8);

            switch (opcao) {
                case 1 -> config.setSaldoInicial(entrada.lerDoublePositivo("Novo saldo inicial: "));
                case 2 -> config.setPatrocinio(entrada.lerDoublePositivo("Novo patrocínio: "));
                case 3 -> config.setMaxRodadas(entrada.lerInt("Novo máximo de rodadas (1-1000): ", 1, 1000));
                case 4 -> config.setCapacidadeHistorico(entrada.lerInt("Nova capacidade do histórico (1-100): ", 1, 100));
                case 5 -> config.setSemente(entrada.lerLong("Nova semente: "));
                case 6 -> config.setModoAutomatico(!config.isModoAutomatico());
                case 7 -> config.setModoTeste(!config.isModoTeste());
                case 8 -> config.setCasasCartaVolta(entrada.lerInt("Casas da carta Volte (1-17): ", 1, 17));
                case 0 -> { return; }
            }
        }
    }

    private String ligado(boolean valor) {
        return valor ? "LIGADO" : "desligado";
    }

    // ===================== DADOS DE TESTE =====================

    // Clubes do Campeonato Brasileiro (tabela 7.1 do enunciado) + jogadores de teste
    private void carregarDadosDeTeste() {
        clubes.clear();
        clubes.add(new Clube("Santos", 200000, 20000));
        clubes.add(new Clube("Bahia", 280000, 28000));
        clubes.add(new Clube("Fortaleza", 310000, 31000));
        clubes.add(new Clube("Vasco da Gama", 350000, 35000));
        clubes.add(new Clube("Fluminense", 400000, 40000));
        clubes.add(new Clube("Botafogo", 450000, 45000));
        clubes.add(new Clube("Cruzeiro", 500000, 50000));
        clubes.add(new Clube("Atlético Mineiro", 550000, 55000));
        clubes.add(new Clube("Grêmio", 600000, 60000));
        clubes.add(new Clube("Internacional", 650000, 65000));
        clubes.add(new Clube("Corinthians", 850000, 85000));
        clubes.add(new Clube("São Paulo", 900000, 90000));
        clubes.add(new Clube("Palmeiras", 950000, 95000));
        clubes.add(new Clube("Flamengo", 1000000, 100000));

        jogadores.clear();
        jogadores.add(new Jogador("Matheus"));
        jogadores.add(new Jogador("Adversário 1"));
        jogadores.add(new Jogador("Adversário 2"));

        System.out.println("Dados de teste carregados: " + clubes.size() + " clubes e " + jogadores.size() + " jogadores.");
    }

    // ===================== PARTIDA =====================

    // Retorna true se a partida aconteceu (aí o programa termina depois do relatório)
    private boolean iniciarPartida() {
        if (clubes.size() < MIN_CLUBES) {
            System.out.println("São necessários pelo menos " + MIN_CLUBES + " clubes (há " + clubes.size() + ").");
            return false;
        }
        if (jogadores.size() < MIN_JOGADORES) {
            System.out.println("São necessários pelo menos " + MIN_JOGADORES + " jogadores (há " + jogadores.size() + ").");
            return false;
        }

        Jogo jogo = new Jogo(jogadores, clubes, config, entrada);
        System.out.println();
        System.out.println("Partida iniciada! Semente: " + config.getSemente()
                + " | Modo automático: " + ligado(config.isModoAutomatico())
                + " | Modo de teste: " + ligado(config.isModoTeste()));
        jogo.exibirTabuleiro();

        while (!jogo.jogoTerminou()) {
            System.out.println();
            System.out.println("--- MENU DA PARTIDA (rodada " + jogo.getRodadaAtual() + ") ---");
            System.out.println("1 - Jogar próximo turno");
            System.out.println("2 - Exibir histórico");
            System.out.println("3 - Exibir estado do jogo");
            System.out.println("4 - Simular até o fim (automático)");
            System.out.println("5 - Exibir tabuleiro");
            System.out.println("0 - Encerrar a partida");
            int opcao = entrada.lerInt("Opção: ", 0, 5);

            switch (opcao) {
                case 1 -> jogo.jogarProximoTurno();
                case 2 -> jogo.exibirHistorico();
                case 3 -> jogo.exibirEstado();
                case 4 -> {
                    config.setModoAutomatico(true);   // sem perguntas, para chegar ao fim
                    config.setModoTeste(false);
                    jogo.simularAteOFim();
                }
                case 5 -> jogo.exibirTabuleiro();
                case 0 -> {
                    jogo.exibirRelatorioFinal();
                    return true;
                }
            }
        }

        System.out.println();
        System.out.println("A partida terminou!");
        jogo.exibirRelatorioFinal();
        return true;
    }
}
