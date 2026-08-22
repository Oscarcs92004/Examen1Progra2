package modelo;

public class Audiovisual extends Material {

    private final int duracionMinutos;
    private final FormatoAV formato;

    public Audiovisual(String titulo, String codigo, int diasBasePrestamo, NivelComplejidad nivel,
                       String rutaImagen, int duracionMinutos, FormatoAV formato) {
        super(titulo, codigo, diasBasePrestamo, nivel, rutaImagen);
        this.duracionMinutos = duracionMinutos;
        this.formato = formato;
    }

    @Override
    public String getDescripcion() {
        return "Material audiovisual en " + formato.getEtiqueta() + " \"" + getTitulo() + "\", "
                + duracionMinutos + " minutos de duración. Complejidad " + getNivel() + ": "
                + getNivel().getDescripcion() + ".";
    }

    @Override
    public int calcularDiasPrestamo() {
        int dias = getDiasBasePrestamo() / 2 + getNivel().getDiasExtra();
        if (duracionMinutos > 120) {
            dias += 1;
        }
        return dias;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public FormatoAV getFormato() {
        return formato;
    }
}
