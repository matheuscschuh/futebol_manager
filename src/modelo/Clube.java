package modelo;

public class Clube {
    private String nome;
    private double valorAquisicao;
    private double taxaMandoBase;
    private Jogador dono;
    private double multiplicador;

    public Clube(String nome, double valorAquisicao, double taxaMandoBase) {
        this.nome = nome;
        this.valorAquisicao = valorAquisicao;
        this.taxaMandoBase = taxaMandoBase;
        this.dono = null;
        this.multiplicador = 1.0;
    }

    public void setValorAquisicao(double valorAquisicao) {
        this.valorAquisicao = valorAquisicao;
    }

    public void setDono(Jogador dono) {
        this.dono = dono;
    }

    public void setTaxaMandoBase(double taxaMandoBase) {
        this.taxaMandoBase = taxaMandoBase;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public double getValorAquisicao() {
        return valorAquisicao;
    }

    public double getTaxaMandoBase() {
        return taxaMandoBase;
    }

    public Jogador getDono() {
        return dono;
    }

    public double getMultiplicador() {
        return multiplicador;
    }

    public void registrarVisita() {
        if (multiplicador >= 2.0) {
            return;
        }

        multiplicador += 0.1;
        multiplicador = Math.round(multiplicador * 10) / 10.0;
    }

    public double calcularTaxa() {
        double taxaCobrada = taxaMandoBase * multiplicador;
        taxaCobrada = Math.round(taxaCobrada * 100) / 100.0;

        return taxaCobrada;
    }

    public void voltarAoBanco() {
        multiplicador = 1.0;
        dono = null;
    }
}
