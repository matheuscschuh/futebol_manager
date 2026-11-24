public class Pilha<T>{

    private No<T> inicio;
    private int tamanho;

    public Pilha() {
        this.inicio = null;
        this.tamanho = 0;
    }

    public void empilhar(T e){
        No<T> novoNo = new No<>(e);

        novoNo.proximo = inicio;
        inicio = novoNo;

        tamanho++;
    }
    public No<T> espiar() {

        if (tamanho == 0){
            System.out.println("Pilha vazia.");
            return null;
        }

        return inicio;
    }
    public No<T> desempilhar(){
        if(tamanho == 0){
            System.out.println("Pilha vazia");
            return null;
        }

        No<T> removido = inicio;
        inicio = inicio.proximo;
        tamanho--;
        removido.proximo = null;
        return removido;


    }

    public int tamanho() {
        return this.tamanho;
    }

    public void exibir() {
        if (inicio == null) {
            System.out.println("A pilha está vazia");
            return;
        }
        No<T> atual = inicio;

        while (atual != null) {
            System.out.print(atual.elemento + " ");
            atual = atual.proximo;
        }
        System.out.println();
    }

    public boolean estaVazia() {
        return inicio == null;
    }

}
