package estruturas;

public class ListaDuplaCircular<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public ListaDuplaCircular() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void adicionarNoFim(T dado) {
        No<T> novo = new No<>(dado);

        if (estaVazia()) {

            inicio = novo;
            fim = novo;
            novo.proximo = novo;
            novo.anterior = novo;
        } else {

            fim.proximo = novo;
            novo.anterior = fim;


            novo.proximo = inicio;
            inicio.anterior = novo;

            fim = novo;
        }

        tamanho++;
    }


    public No<T> getInicio() {
        return inicio;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public void exibir() {
        if (estaVazia()) {
            System.out.println("Tabuleiro vazio");
            return;
        }

        No<T> atual = inicio;
        int posicao = 0;


        do {
            System.out.println("[" + posicao + "] " + atual.dado);
            atual = atual.proximo;
            posicao++;
        } while (atual != inicio);


        System.out.println("↻ Última casa (" + fim.dado + ") -> próximo: " + fim.proximo.dado);
        System.out.println("↺ Primeira casa (" + inicio.dado + ") -> anterior: " + inicio.anterior.dado);
    }
}