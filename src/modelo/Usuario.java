package modelo;

import java.util.ArrayList;
import java.util.Calendar;

public abstract class Usuario {

    private final String id;
    private final String nombre;

    private final ArrayList<Material> prestamosActivos = new ArrayList<>();
    private final ArrayList<Prestamo> historial = new ArrayList<>();
    private Calendar penalizadoHasta;

    protected Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int sumarDiasPenalizacion(int indice) {
        if (indice >= historial.size()) {
            return 0;
        }
        return historial.get(indice).getDiasRetraso() + sumarDiasPenalizacion(indice + 1);
    }

    public abstract int getLimitePrestamos();

    public abstract boolean puedeReservar();

    public abstract boolean puedeAcceder(NivelComplejidad nivel);

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Material> getPrestamosActivos() {
        return prestamosActivos;
    }

    public ArrayList<Prestamo> getHistorial() {
        return historial;
    }


    public Calendar getPenalizadoHasta() {
        return penalizadoHasta;
    }

    public void setPenalizadoHasta(Calendar penalizadoHasta) {
        this.penalizadoHasta = penalizadoHasta;
    }

    public boolean estaPenalizado() {
        return penalizadoHasta != null && Calendar.getInstance().before(penalizadoHasta);
    }

    @Override
    public String toString() {
        return nombre + " [" + id + "]";
    }
}
