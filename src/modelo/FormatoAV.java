package modelo;

public enum FormatoAV {
    DVD("DVD"),
    BLURAY("Blu-ray");

    private final String etiqueta;

    FormatoAV(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
