package jogo;

/**
 * Configurações da partida (item 3.3 do enunciado), todas alteráveis pelo menu.
 */
public class Configuracao {

    private double saldoInicial = 1000000;
    private double patrocinio = 100000;
    private int maxRodadas = 60;
    private int capacidadeHistorico = 10;
    private long semente = 42;              // semente do gerador de números aleatórios
    private boolean modoAutomatico = false; // o sistema decide compras sozinho
    private boolean modoTeste = false;      // dados e carta do topo informados manualmente
    private int casasCartaVolta = 3;        // quantas casas a carta "Volte" faz recuar

    public double getSaldoInicial() { return saldoInicial; }
    public double getPatrocinio() { return patrocinio; }
    public int getMaxRodadas() { return maxRodadas; }
    public int getCapacidadeHistorico() { return capacidadeHistorico; }
    public long getSemente() { return semente; }
    public boolean isModoAutomatico() { return modoAutomatico; }
    public boolean isModoTeste() { return modoTeste; }
    public int getCasasCartaVolta() { return casasCartaVolta; }

    public void setSaldoInicial(double saldoInicial) { this.saldoInicial = saldoInicial; }
    public void setPatrocinio(double patrocinio) { this.patrocinio = patrocinio; }
    public void setMaxRodadas(int maxRodadas) { this.maxRodadas = maxRodadas; }
    public void setCapacidadeHistorico(int capacidadeHistorico) { this.capacidadeHistorico = capacidadeHistorico; }
    public void setSemente(long semente) { this.semente = semente; }
    public void setModoAutomatico(boolean modoAutomatico) { this.modoAutomatico = modoAutomatico; }
    public void setModoTeste(boolean modoTeste) { this.modoTeste = modoTeste; }
    public void setCasasCartaVolta(int casasCartaVolta) { this.casasCartaVolta = casasCartaVolta; }
}
