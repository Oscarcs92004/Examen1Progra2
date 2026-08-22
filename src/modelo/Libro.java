package modelo;

public class Libro extends Material {

    private final String autor;
    private final int numeroPaginas;
    private final String isbn;

    public Libro(String titulo, String codigo, int diasBasePrestamo, NivelComplejidad nivel,
                 String rutaImagen, String autor, int numeroPaginas, String isbn) {
        super(titulo, codigo, diasBasePrestamo, nivel, rutaImagen);
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
    }

    @Override
    public String getDescripcion() {
        return "Libro \"" + getTitulo() + "\" de " + autor + ", " + numeroPaginas
                + " páginas (ISBN " + isbn + "). Complejidad " + getNivel() + ": "
                + getNivel().getDescripcion() + ".";
    }

    @Override
    public int calcularDiasPrestamo() {
        int dias = getDiasBasePrestamo() + getNivel().getDiasExtra();
        if (numeroPaginas > 400) {
            dias += 5;
        }
        return dias;
    }

    public String getAutor() {
        return autor;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getIsbn() {
        return isbn;
    }
}
