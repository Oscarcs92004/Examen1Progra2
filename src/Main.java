import controlador.BibliotecaControlador;
import modelo.FormatoAV;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import servicio.Biblioteca;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        BibliotecaControlador controlador = new BibliotecaControlador(biblioteca);

        cargarDatosIniciales(controlador);
        cargarUsuariosIniciales(controlador);
        mostrarCatalogoPolimorfico(controlador);

        VentanaPrincipal.iniciar(controlador);
    }

    public static void cargarUsuariosIniciales(BibliotecaControlador controlador) {
        controlador.agregarUsuario("U001", "Alex Enamorado", false);
        controlador.agregarUsuario("U002", "Marcelo García", true);
        controlador.agregarUsuario("U003", "Óscar Canahuati", false);
    }

    public static void cargarDatosIniciales(BibliotecaControlador controlador) {
        controlador.agregarLibro("Cien años de soledad", "L001", 14, NivelComplejidad.BAJO,
                "imagenes/cien_anos.jpg", "Gabriel García Márquez", 471, "978-0307474728");
        controlador.agregarLibro("El principito", "L002", 14, NivelComplejidad.BAJO,
                "imagenes/el_principito.jpg", "Antoine de Saint-Exupéry", 96, "978-0156012195");
        controlador.agregarLibro("Introducción a los algoritmos", "L003", 21, NivelComplejidad.ALTO,
                "imagenes/algoritmos.jpg", "Thomas H. Cormen", 1312, "978-0262046305");

        controlador.agregarRevista("National Geographic", "R001", 10, NivelComplejidad.MEDIO,
                "imagenes/national_geographic.jpg", 245, Periodicidad.MENSUAL);
        controlador.agregarRevista("Nature", "R002", 14, NivelComplejidad.ALTO,
                "imagenes/nature.jpg", 8123, Periodicidad.SEMANAL);
        controlador.agregarRevista("Anuario Estadístico", "R003", 15, NivelComplejidad.MEDIO,
                "imagenes/anuario.jpg", 12, Periodicidad.ANUAL);

        controlador.agregarAudiovisual("Interstellar", "A001", 14, NivelComplejidad.MEDIO,
                "imagenes/interstellar.jpg", 169, FormatoAV.BLURAY);
        controlador.agregarAudiovisual("El Padrino", "A002", 10, NivelComplejidad.BAJO,
                "imagenes/el_padrino.jpg", 175, FormatoAV.DVD);
        controlador.agregarAudiovisual("Documental: Física Cuántica", "A003", 12, NivelComplejidad.ALTO,
                "ruta/inexistente.jpg", 55, FormatoAV.BLURAY);
    }

    private static void mostrarCatalogoPolimorfico(BibliotecaControlador controlador) {
        System.out.println("=== CATÁLOGO (recorrido polimórfico) ===");
        for (Material material : controlador.listarMateriales()) {
            System.out.println(controlador.describir(material));
            System.out.println("   Días de préstamo: " + controlador.diasDePrestamo(material)
                    + " | Estado: " + material.getEstado().getEtiqueta()
                    + " | Autorización especial: " + (material.getNivel().requiereAutorizacion() ? "sí" : "no"));
        }
        System.out.println("Disponibles ahora mismo: " + controlador.contarDisponibles());
    }




}
