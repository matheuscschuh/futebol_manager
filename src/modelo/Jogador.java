package modelo;

import estruturas.No;

import java.util.ArrayList;

public class Jogador {
    private String nome;
    private double saldo;
    private No<Casa> posicaoAtual;
    private ArrayList<Clube> clubesAdquiridos;
    private int temporadasCompletas;
    private boolean eliminado;
    private int voltasPendentes;
    private double totalRecebidoTaxas;

    public Jogador(String nome) {
        this.nome = nome;
        this.saldo = 0;
        this.posicaoAtual = null;
        this.clubesAdquiridos = new ArrayList<>();
        this.temporadasCompletas = 0;
        this.eliminado = false;
        this.voltasPendentes = 0;
        this.totalRecebidoTaxas = 0;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public No<Casa> getPosicaoAtual() {
        return posicaoAtual;
    }

    public ArrayList<Clube> getClubesAdquiridos() {
        return clubesAdquiridos;
    }

    public int getTemporadasCompletas() {
        return temporadasCompletas;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public int getVoltasPendentes() {
        return voltasPendentes;
    }

    public double getTotalRecebidoTaxas() {
        return totalRecebidoTaxas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void prepararParaPartida(double saldoInicial, No<Casa> inicio) {
        saldo = saldoInicial;
        posicaoAtual = inicio;
    }

    public void receber(double valor) {
        saldo += valor;
    }
    public void debitar(double valor) {
        saldo -= valor;
    }
    public void adicionarClube(Clube c) {
        clubesAdquiridos.add(c);
    }
    public void removerClube(Clube c) {
        clubesAdquiridos.remove(c);
    }

    public double calcularPatrimonio() {
        double patrimonioTotal = saldo;
        for (Clube clube : clubesAdquiridos) {
            patrimonioTotal += clube.getValorAquisicao();
        }

        return patrimonioTotal;
    }

    public void completarTemporada() {
        temporadasCompletas += 1;
    }
    public void adicionarVoltaPendente() {
        voltasPendentes += 1;
    }
    public void quitarVoltaPendente() {
        if (voltasPendentes == 0) {
            return;
        }
        voltasPendentes -= 1;
    }
    public void registrarTaxaRecebida(double valor) {
        totalRecebidoTaxas += valor;
    }
    public void eliminar() {
        eliminado = true;
        saldo = 0;
        clubesAdquiridos.clear();
    }

    public void avancarUmaCasa() {
        posicaoAtual = posicaoAtual.getProximo();
    }
    public void recuarUmaCasa() {
        posicaoAtual = posicaoAtual.getAnterior();
    }
}
