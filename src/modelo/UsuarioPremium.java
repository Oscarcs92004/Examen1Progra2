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

    @Override
    public boolean puedeReservar() {
        return true;
    }

    @Override
    public boolean puedeAcceder(NivelComplejidad nivel) {
        return true;
    }
}
