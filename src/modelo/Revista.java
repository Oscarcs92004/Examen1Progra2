package modelo;

public class Revista extends Material {

    private final int numeroEdicion;
    private final Periodicidad periodicidad;

    public Revista(String titulo, String codigo, int diasBasePrestamo, NivelComplejidad nivel,
                   String rutaImagen, int numeroEdicion, Periodicidad periodicidad) {
        super(titulo, codigo, diasBasePrestamo, nivel, rutaImagen);
        this.numeroEdicion = numeroEdicion;
        this.periodicidad = periodicidad;
    }

    @Override
    public String getDescripcion() {
        return "Revista \"" + getTitulo() + "\", edición número " + numeroEdicion
                + " de publicación " + periodicidad.name().toLowerCase()
                + ". Complejidad " + getNivel() + ": " + getNivel().getDescripcion() + ".";
    }

    @Override
    public int calcularDiasPrestamo() {
        return Math.min(getDiasBasePrestamo(), periodicidad.getDiasVigencia()) + getNivel().getDiasExtra();
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }
}
