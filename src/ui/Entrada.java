package ui;

import java.util.Scanner;

/**
 * Leitura do teclado COM VALIDAÇÃO (requisito 3.4 do enunciado):
 * texto no lugar de número ou opção inválida nunca encerra o programa.
 * Sempre lê a linha inteira e tenta converter; se não der, pede de novo.
 */
public class Entrada {

    private final Scanner scanner = new Scanner(System.in);

    // Lê um inteiro entre min e max (inclusive)
    public int lerInt(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor inválido. Digite um número entre " + min + " e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    // Lê um número decimal positivo (aceita vírgula ou ponto)
    public double lerDoublePositivo(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim().replace(".", "").replace(",", ".");
            try {
                double valor = Double.parseDouble(linha);
                if (valor > 0) {
                    return valor;
                }
                System.out.println("O valor deve ser positivo.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número (ex.: 500000 ou 500.000,00).");
            }
        }
    }

    // Lê um long qualquer (usado para a semente)
    public long lerLong(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                return Long.parseLong(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    // Lê um texto que não pode ficar vazio
    public String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                return linha;
            }
            System.out.println("O texto não pode ficar vazio.");
        }
    }

    // Lê s/n
    public boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem + " (s/n): ");
            String linha = scanner.nextLine().trim().toLowerCase();
            if (linha.equals("s")) return true;
            if (linha.equals("n")) return false;
            System.out.println("Responda com s ou n.");
        }
    }

    // Só espera o Enter (para dar tempo de ler/tirar print)
    public void aguardarEnter() {
        System.out.print("(Enter para continuar)");
        scanner.nextLine();
    }
}
