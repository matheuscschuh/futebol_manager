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
        Carta[] vetor = new Carta[cartas.size()];
        for (int i = 0; i < vetor.length; i++) {
            vetor[i] = cartas.get(i);
        }
        embaralharEEmpilhar(vetor);
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
        if (baralho.estaVazia()) {
            reabastecer();
        }
        return baralho.desempilhar();
    }

    // Depois de aplicada, a carta vai para o descarte
    public void descartar(Carta carta) {
        // empilha no descarte
        descarte.empilhar(carta);
    }


    private void reabastecer() {
        System.out.println("   ** Baralho vazio! O descarte (" + descarte.tamanho() + " cartas) foi embaralhado e empilhado de novo.");
        Carta[] cartas = new Carta[descarte.tamanho()];
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = descarte.desempilhar();
        }
        embaralharEEmpilhar(cartas);
    }

    private void embaralharEEmpilhar(Carta[] cartas) {
        embaralhar(cartas);
        for (int i = 0; i < cartas.length; i++) {
            baralho.empilhar(cartas[i]);
        }
    }

    /**
     * As 12 cartas do jogo (item 2.5 do enunciado): 6 de ganho/avanço e 6 de perda/retrocesso.
     * Equipe de 1 integrante: a carta 9 ("Vá para a Série B") foi trocada por outra carta de perda.
     */
    public static ArrayList<Carta> criarCartasPadrao() {
        ArrayList<Carta> cartas = new ArrayList<>();

        // Ganho / avanço
        cartas.add(new Carta("Receba R$ 50.000 do banco por nova negociação de patrocínio", TipoEfeito.RECEBER_BANCO, 50000));
        cartas.add(new Carta("Avance 3 casas após uma vitória importante", TipoEfeito.AVANCAR, 3));
        cartas.add(new Carta("Avance até o Início e receba o patrocínio", TipoEfeito.IR_PARA_INICIO, 0));
        cartas.add(new Carta("Todos os outros jogadores não eliminados pagam R$ 10.000 a você", TipoEfeito.RECEBER_TODOS, 10000));
        cartas.add(new Carta("Receba R$ 75.000 de bônus por conquistar um título", TipoEfeito.RECEBER_BANCO, 75000));
        cartas.add(new Carta("Receba R$ 30.000 por direitos de transmissão", TipoEfeito.RECEBER_BANCO, 30000));

        // Perda / retrocesso
        cartas.add(new Carta("Pague R$ 30.000 ao banco por multa da federação", TipoEfeito.PAGAR_BANCO, 30000));
        cartas.add(new Carta("Pague R$ 10.000 a cada um dos outros jogadores não eliminados", TipoEfeito.PAGAR_TODOS, 10000));
        cartas.add(new Carta("Pague R$ 20.000 pelo tratamento de um jogador lesionado", TipoEfeito.PAGAR_BANCO, 20000)); // substitui a carta 9
        cartas.add(new Carta("Volte 3 casas após uma derrota inesperada", TipoEfeito.RECUAR, 3));
        cartas.add(new Carta("Pague R$ 40.000 por uma contratação malsucedida", TipoEfeito.PAGAR_BANCO, 40000));
        cartas.add(new Carta("Perca 5% do seu patrimônio por problemas financeiros", TipoEfeito.PERDER_PORCENTAGEM, 5));

        return cartas;
    }
}