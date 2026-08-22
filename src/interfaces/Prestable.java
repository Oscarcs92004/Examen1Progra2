package interfaces;

import excepciones.MaterialNoDisponibleException;

public interface Prestable {

    void prestar(String idUsuario) throws MaterialNoDisponibleException;

    void devolver();

    boolean estaDisponible();
}
