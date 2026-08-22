package modelo;

import excepciones.MaterialNoDisponibleException;
import interfaces.Prestable;
import interfaces.Reservable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Material implements Prestable, Reservable, Comparable<Material> {

    private final String titulo;
    private final String codigo;
    private final int diasBasePrestamo;
    private final NivelComplejidad nivel;
    private final String rutaImagen;

    private EstadoPrestamo estado;
    private String reservadoPara;

    // La cola guarda ids de usuario y no objetos Usuario para que modelo.Material
    // no dependa de modelo.Usuario. Biblioteca resuelve el id contra su lista de usuarios.
    private final Queue<String> colaReservas = new LinkedList<>();

    protected Material(String titulo, String codigo, int diasBasePrestamo,
                       NivelComplejidad nivel, String rutaImagen) {
        this.titulo = titulo;
        this.codigo = codigo;
        this.diasBasePrestamo = diasBasePrestamo;
        this.nivel = nivel;
        this.rutaImagen = rutaImagen;
        this.estado = EstadoPrestamo.DISPONIBLE;
    }

    public abstract String getDescripcion();

    public abstract int calcularDiasPrestamo();

    @Override
    public void prestar(String idUsuario) throws MaterialNoDisponibleException {
        if (estado == EstadoPrestamo.PRESTADO) {
            throw new MaterialNoDisponibleException(titulo, estado.getEtiqueta().toLowerCase());
        }
        if (estado == EstadoPrestamo.RESERVADO && !idUsuario.equals(reservadoPara)) {
            throw new MaterialNoDisponibleException(titulo, "reservado para el usuario " + reservadoPara);
        }
        estado = EstadoPrestamo.PRESTADO;
        reservadoPara = null;
    }

    @Override
    public void devolver() {
        if (colaReservas.isEmpty()) {
            estado = EstadoPrestamo.DISPONIBLE;
            reservadoPara = null;
        } else {
            estado = EstadoPrestamo.RESERVADO;
            reservadoPara = colaReservas.poll();
        }
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoPrestamo.DISPONIBLE;
    }

    @Override
    public void reservar(String idUsuario) {
        if (!colaReservas.contains(idUsuario)) {
            colaReservas.add(idUsuario);
        }
    }

    @Override
    public void cancelarReserva(String idUsuario) {
        colaReservas.remove(idUsuario);
        if (idUsuario.equals(reservadoPara)) {
            devolver();
        }
    }

    @Override
    public boolean tieneReservas() {
        return !colaReservas.isEmpty();
    }

    @Override
    public int compareTo(Material otro) {
        return titulo.compareToIgnoreCase(otro.titulo);
    }

    @Override
    public String toString() {
        return titulo + " [" + codigo + "]";
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getDiasBasePrestamo() {
        return diasBasePrestamo;
    }

    public NivelComplejidad getNivel() {
        return nivel;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public String getReservadoPara() {
        return reservadoPara;
    }

    public List<String> getColaReservas() {
        return new ArrayList<>(colaReservas);
    }
}
