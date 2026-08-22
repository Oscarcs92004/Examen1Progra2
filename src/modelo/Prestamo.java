package modelo;

import java.util.Calendar;

public class Prestamo {

    private final Usuario usuario;
    private final Material material;
    private Calendar fechaPrestamo;
    private Calendar fechaDevolucionPrevista;
    private Calendar fechaDevolucionReal;

    public Prestamo(Usuario usuario, Material material, Calendar fechaPrestamo, int diasPrestamo) {
        this.usuario = usuario;
        this.material = material;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = (Calendar) fechaPrestamo.clone();
        this.fechaDevolucionPrevista.add(Calendar.DATE, diasPrestamo);
    }

    public int getDiasRetraso() {
        if (fechaDevolucionPrevista == null) {
            return 0;
        }
        Calendar referencia = Calendar.getInstance();
        if (fechaDevolucionReal != null) {
            referencia = fechaDevolucionReal;
        }
        Calendar fecha = (Calendar) fechaDevolucionPrevista.clone();
        int dias = 0;
        while (fecha.before(referencia)) {
            fecha.add(Calendar.DATE, 1);
            dias++;
        }
        return dias;
    }

    public boolean estaVencido() {
        return fechaDevolucionReal == null && getDiasRetraso() > 0;
    }

    public boolean estaActivo() {
        return fechaDevolucionReal == null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Material getMaterial() {
        return material;
    }

    public Calendar getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Calendar fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Calendar getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(Calendar fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public Calendar getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Calendar fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }
}
