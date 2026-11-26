package ui;

import java.util.Locale;

/**
 * Formata valores em dinheiro no padrão brasileiro: R$ 1.000.000,00
 */
public class Formatador {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    public static String dinheiro(double valor) {
        return String.format(BRASIL, "R$ %,.2f", valor);
    }
}
