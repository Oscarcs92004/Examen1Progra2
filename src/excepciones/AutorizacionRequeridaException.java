package excepciones;

public class AutorizacionRequeridaException extends BibliotecaException {

    public AutorizacionRequeridaException(String nombreUsuario, String titulo, String nivel) {
        super("El usuario " + nombreUsuario + " no tiene autorización para prestar \"" + titulo
                + "\": es un material de complejidad " + nivel + " y se requiere perfil premium.");
    }
}
