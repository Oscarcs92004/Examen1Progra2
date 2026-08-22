package excepciones;

public class MaterialNoDisponibleException extends BibliotecaException {

    public MaterialNoDisponibleException(String titulo, String estado) {
        super("El material \"" + titulo + "\" no se puede prestar porque está " + estado + ".");
    }
}
