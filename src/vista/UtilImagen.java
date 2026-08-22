package vista;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public final class UtilImagen {

    public static final int ANCHO = 120;
    public static final int ALTO = 160;
    private static final String RUTA_SIN_PORTADA = "imagenes/sinPortada.jpg";
    private static final File RAIZ_PROYECTO = localizarRaizProyecto();
    private static ImageIcon sinPortada;

    private UtilImagen() {
    }

    // Las rutas del modelo son relativas al proyecto, pero el directorio de trabajo
    // cambia segun se ejecute desde la raiz, desde src/ o desde el IDE.
    private static File localizarRaizProyecto() {
        File directorio = new File(System.getProperty("user.dir")).getAbsoluteFile();
        while (directorio != null) {
            if (new File(directorio, "imagenes").isDirectory()) {
                return directorio;
            }
            directorio = directorio.getParentFile();
        }
        return new File(System.getProperty("user.dir")).getAbsoluteFile();
    }

    private static File resolver(String ruta) {
        File archivo = new File(ruta);
        if (archivo.isAbsolute() || archivo.isFile()) {
            return archivo;
        }
        return new File(RAIZ_PROYECTO, ruta);
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
        File archivo = resolver(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            return null;
        }
        ImageIcon icono = new ImageIcon(archivo.getAbsolutePath());
        if (icono.getIconWidth() <= 0 || icono.getIconHeight() <= 0) {
            return null;
        }
        Image escalada = icono.getImage().getScaledInstance(ANCHO, ALTO, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
}
