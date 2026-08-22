package vista;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public final class UtilImagen {

    public static final int ANCHO = 120;
    public static final int ALTO = 160;
    private static final String RUTA_SIN_PORTADA = "imagenes/sinPortada.jpg";
    private static ImageIcon sinPortada;

    private UtilImagen() {
    }

    public static ImageIcon cargarPortada(String rutaImagen) {
        ImageIcon icono = cargarDesdeArchivo(rutaImagen);
        return icono != null ? icono : obtenerSinPortada();
    }

    private static ImageIcon obtenerSinPortada() {
        if (sinPortada == null) {
            ImageIcon icono = cargarDesdeArchivo(RUTA_SIN_PORTADA);
            sinPortada = icono != null ? icono : new ImageIcon(new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_ARGB));
        }
        return sinPortada;
    }

    private static ImageIcon cargarDesdeArchivo(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            return null;
        }
        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            return null;
        }
        ImageIcon icono = new ImageIcon(ruta);
        if (icono.getIconWidth() <= 0 || icono.getIconHeight() <= 0) {
            return null;
        }
        Image escalada = icono.getImage().getScaledInstance(ANCHO, ALTO, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
}
