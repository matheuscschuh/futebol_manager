public class Fila<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void enfileirar(T e) {
        No<T> novoNo = new No<>(e);

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
            System.out.println("Fila vazia.");
            return null;
        }

        return inicio.elemento;
    }

    public No<T> desenfileirar() {
        if (inicio == null) {
            System.out.println("Fila vazia");
            return null;
        }

        No<T> removido = inicio;
        inicio = inicio.proximo;

        tamanho--;

        if (inicio == null) {
            fim = null;
        }

        removido.proximo = null;

        return removido;
    }

    public No<T> primeiro() {
        if (inicio == null) {
            System.out.println("Fila vazia");
            return null;
        }

        return inicio;
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
            System.out.print(atual.elemento + " ");
            atual = atual.proximo;
        }

        System.out.println();
    }
}