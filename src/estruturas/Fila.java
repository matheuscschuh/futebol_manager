package estruturas;

public class Fila<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;
    private int capacidade;

    public Fila(int capacidade) {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;

        if (capacidade <= 0) {
            this.capacidade = 10;
        } else {
            this.capacidade = capacidade;
        }

    }

    public void enfileirar(T e) {
        No<T> novoNo = new No<>(e);

        if (tamanho == capacidade){
            desenfileirar();
        }

        if (inicio == null) {
            inicio = novoNo;
            fim = novoNo;
        } else {
            fim.proximo = novoNo;
            fim = novoNo;
        }

        tamanho++;
    }

    public T espiar() {
        if (tamanho == 0) {
            System.out.println("estruturas.Fila vazia.");
            return null;
        }

        return inicio.dado;
    }

    public T desenfileirar() {
        if (inicio == null) {
            System.out.println("estruturas.Fila vazia");
            return null;
        }

        No<T> removido = inicio;
        inicio = inicio.proximo;

        tamanho--;

        if (inicio == null) {
            fim = null;
        }

        removido.proximo = null;

        return removido.dado;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    public int tamanho() {
        return this.tamanho;
    }

    public void exibir() {
        if (inicio == null) {
            System.out.println("A fila está vazia");
            return;
        }

        No<T> atual = inicio;

        while (atual != null) {
            System.out.print(atual.dado + " ");
            atual = atual.proximo;
        }

        System.out.println();
    }
}