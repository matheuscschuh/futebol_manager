package modelo;

public class Casa {
    private TipoCasa tipo;
    private Clube clube;

    public Casa(TipoCasa tipo) {
        this.tipo = tipo;
    }
    public Casa(Clube clube) {
        this.clube = clube;
        tipo = TipoCasa.CLUBE;
    }

    public TipoCasa getTipo() {
        return tipo;
    }

    public Clube getClube() {
        return clube;
    }

    @Override
    public String toString() {
        if (tipo == TipoCasa.CLUBE) {
            return clube.getNome();
        } else {
            return tipo.toString();
        }
    }
}