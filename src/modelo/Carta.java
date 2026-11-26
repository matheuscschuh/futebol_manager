package modelo;

public class Carta {
    private final String descricao;
    private final TipoEfeito efeito;
    private final double valor;

    public Carta(String descricao, TipoEfeito efeito, double valor) {
        this.descricao = descricao;
        this.efeito = efeito;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoEfeito getEfeito() {
        return efeito;
    }

    public double getValor() {
        return valor;
    }
}
