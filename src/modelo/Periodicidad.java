package modelo;

public enum Periodicidad {
    SEMANAL(7),
    MENSUAL(30),
    ANUAL(365);

    private final int diasVigencia;

    Periodicidad(int diasVigencia) {
        this.diasVigencia = diasVigencia;
    }

    public int getDiasVigencia() {
        return diasVigencia;
    }
}
