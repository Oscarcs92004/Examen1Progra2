package modelo;

import java.util.Calendar;

public class Prestamo {

    private Calendar fechaPrestamo;
    private Calendar fechaDevolucionPrevista;
    private Calendar fechaDevolucionReal;

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

    public boolean estaVencido() {
        return fechaDevolucionReal == null && getDiasRetraso() > 0;
    }
}
