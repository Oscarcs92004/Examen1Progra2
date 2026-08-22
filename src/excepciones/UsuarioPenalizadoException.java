package excepciones;

import java.time.LocalDate;

public class UsuarioPenalizadoException extends BibliotecaException {
    public UsuarioPenalizadoException(String nombreUsuario, LocalDate penalizadoHasta) {
        super("El usuario " + nombreUsuario + " tiene una penalización vigente hasta "
                + penalizadoHasta + " por una devolución tardía y no puede solicitar nuevos préstamos.");
    }
}
