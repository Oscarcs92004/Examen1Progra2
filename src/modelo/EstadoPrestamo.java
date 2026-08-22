package modelo;

public enum EstadoPrestamo {
    DISPONIBLE("Disponible"),
    PRESTADO("Prestado"),
    RESERVADO("Reservado");

    private final String etiqueta;

    EstadoPrestamo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
