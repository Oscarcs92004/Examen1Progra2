package excepciones;

public class LimitePrestamosException extends BibliotecaException {

    public LimitePrestamosException(String nombreUsuario, int limite) {
        super("El usuario " + nombreUsuario + " ya alcanzó su límite de " + limite
                + " préstamos simultáneos. Debe devolver algún material antes de pedir otro.");
    }
}
