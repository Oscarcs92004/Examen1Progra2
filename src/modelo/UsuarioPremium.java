package modelo;

public class UsuarioPremium extends Usuario {

    private static final int LIMITE_PRESTAMOS = 6;

    public UsuarioPremium(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int getLimitePrestamos() {
        return LIMITE_PRESTAMOS;
    }

    public boolean puedeReservar() {
        return true;
    }

    public boolean puedeAcceder(NivelComplejidad nivel) {
        return true;
    }
}
