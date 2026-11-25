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
}
