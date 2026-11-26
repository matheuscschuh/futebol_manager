package modelo;

public class EntradaHistorico {
    private int numeroRodada;
    private String jogador;
    private int dado1;
    private int dado2;
    private String casaDestino;
    private String efeitoAplicado;

    public EntradaHistorico(int numeroRodada, String jogador, int dado1, int dado2, String casaDestino, String efeitoAplicado) {
        this.numeroRodada = numeroRodada;
        this.jogador = jogador;
        this.dado1 = dado1;
        this.dado2 = dado2;
        this.casaDestino = casaDestino;
        this.efeitoAplicado = efeitoAplicado;
    }

    @Override
    public String toString() {
        return "Rodada " + numeroRodada + " | " +
                jogador + " | Dados: " +
                dado1 + " + " +
                dado2 + " = " + (dado1 + dado2) + " | Casa: " +
                casaDestino + " | " + efeitoAplicado;
    }
}
