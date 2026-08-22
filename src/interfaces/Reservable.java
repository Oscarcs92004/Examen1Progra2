package interfaces;

public interface Reservable {

    void reservar(String idUsuario);

    void cancelarReserva(String idUsuario);

    boolean tieneReservas();
}
