package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Usuario {

    private final String id;
    private final String nombre;

    private final ArrayList<Material> prestamosActivos = new ArrayList<>();
    private final ArrayList<Material> historial = new ArrayList<>();
    private LocalDate penalizadoHasta;

    protected Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public abstract int getLimitePrestamos();

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Material> getPrestamosActivos() {
        return prestamosActivos;
    }

    public ArrayList<Material> getHistorial() {
        return historial;
    }

    public LocalDate getPenalizadoHasta() {
        return penalizadoHasta;
    }

    public void setPenalizadoHasta(LocalDate penalizadoHasta) {
        this.penalizadoHasta = penalizadoHasta;
    }

    public boolean estaPenalizado() {
        return penalizadoHasta != null && LocalDate.now().isBefore(penalizadoHasta);
    }

    @Override
    public String toString() {
        return nombre + " [" + id + "]";
    }
}
