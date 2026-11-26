package jogo;

import estruturas.Pilha;
import modelo.Carta;
import modelo.TipoEfeito;

import java.util.ArrayList;
import java.util.Random;

public class Baralho {

    private Pilha<Carta> baralho;
    private Pilha<Carta> descarte;
    private Random random;

    public Baralho(ArrayList<Carta> cartas, Random random) {
        this.baralho = new Pilha<>();
        this.descarte = new Pilha<>();
        this.random = random;

        // 1. passa as cartas para um vetor (Carta[])
        // 2. embaralha o vetor
        // 3. empilha cada carta do vetor no baralho
    }

    // Fisher-Yates: embaralha o vetor no lugar
    private void embaralhar(Carta[] cartas) {
        for (int i = cartas.length - 1; i >= 1 ; i--) {

            int j = random.nextInt(i + 1);
            Carta temp = cartas[i];
            cartas[i] = cartas[j];
            cartas[j] = temp;
        }
    }

    // Tira a carta do topo. Se o baralho estiver vazio, reabastece antes.
    public Carta comprar() {
        // se o baralho está vazio → reabastecer()
        // devolve a carta desempilhada do baralho
    }

    // Depois de aplicada, a carta vai para o descarte
    public void descartar(Carta carta) {
        // empilha no descarte
        descarte.empilhar(carta);
    }


    private void reabastecer() {
        Carta[] cartas = new Carta[descarte.tamanho()];
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = descarte.desempilhar();
        }
        embaralharEEmpilhar(cartas);
    }

    private void embaralharEEmpilhar(Carta[] cartas) {
        embaralhar(cartas);
        for (int i = 0; i < cartas.length - 1; i++) {
            baralho.empilhar(cartas[i]);
        }
    }
}