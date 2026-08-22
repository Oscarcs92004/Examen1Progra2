package modelo;

public enum NivelComplejidad {
    BAJO(1, 0, "Lectura general, sin conocimientos previos"),
    MEDIO(2, 3, "Requiere conocimientos previos del área"),
    ALTO(3, 7, "Material especializado de consulta restringida");

    private final int orden;
    private final int diasExtra;
    private final String descripcion;

    NivelComplejidad(int orden, int diasExtra, String descripcion) {
        this.orden = orden;
        this.diasExtra = diasExtra;
        this.descripcion = descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public int getDiasExtra() {
        return diasExtra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean requiereAutorizacion() {
        return this == ALTO;
    }
}
