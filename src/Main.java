import controlador.BibliotecaControlador;
import excepciones.BibliotecaException;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;
import servicio.Biblioteca;

import java.util.List;

// Provisional: cuando la GUI esté lista, este main solo cargará los datos y abrirá VentanaPrincipal.
public class Main {

    public static void main(String[] args) throws BibliotecaException {
        Biblioteca biblioteca = new Biblioteca();
        BibliotecaControlador controlador = new BibliotecaControlador(biblioteca);

        cargarDatosIniciales(controlador);

        mostrarCatalogoPolimorfico(controlador);
        mostrarBusquedas(controlador);
        mostrarFiltroGenerico(controlador);
        mostrarOrdenaciones(controlador);
        mostrarFlujoDeReservas(controlador);
    }

    public static void cargarDatosIniciales(BibliotecaControlador controlador) {
        controlador.altaLibro("Cien años de soledad", "L001", 14, NivelComplejidad.BAJO,
                "imagenes/cien_anos.jpg", "Gabriel García Márquez", 471, "978-0307474728");
        controlador.altaLibro("El principito", "L002", 14, NivelComplejidad.BAJO,
                "imagenes/el_principito.jpg", "Antoine de Saint-Exupéry", 96, "978-0156012195");
        controlador.altaLibro("Introducción a los algoritmos", "L003", 21, NivelComplejidad.ALTO,
                "imagenes/algoritmos.jpg", "Thomas H. Cormen", 1312, "978-0262046305");

        controlador.altaRevista("National Geographic", "R001", 10, NivelComplejidad.MEDIO,
                "imagenes/national_geographic.jpg", 245, Periodicidad.MENSUAL);
        controlador.altaRevista("Nature", "R002", 14, NivelComplejidad.ALTO,
                "imagenes/nature.jpg", 8123, Periodicidad.SEMANAL);
        controlador.altaRevista("Anuario Estadístico", "R003", 15, NivelComplejidad.MEDIO,
                "imagenes/anuario.jpg", 12, Periodicidad.ANUAL);

        controlador.altaAudiovisual("Interstellar", "A001", 14, NivelComplejidad.MEDIO,
                "imagenes/interstellar.jpg", 169, FormatoAV.BLURAY);
        controlador.altaAudiovisual("El Padrino", "A002", 10, NivelComplejidad.BAJO,
                "imagenes/el_padrino.jpg", 175, FormatoAV.DVD);
        controlador.altaAudiovisual("Documental: Física Cuántica", "A003", 12, NivelComplejidad.ALTO,
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

    private static void mostrarBusquedas(BibliotecaControlador controlador) {
        System.out.println();
        System.out.println("=== BÚSQUEDAS RECURSIVAS ===");
        System.out.println("Exacta por código \"R002\": " + controlador.buscarExacto("R002"));
        System.out.println("Exacta por título \"El principito\": " + controlador.buscarExacto("El principito"));
        System.out.println("Exacta inexistente \"XXX\": " + controlador.buscarExacto("XXX"));
        System.out.println("Parcial \"el\": " + controlador.buscarPorTituloParcial("el"));
        System.out.println("Nivel ALTO: " + controlador.buscarPorNivel(NivelComplejidad.ALTO));
    }

    private static void mostrarFiltroGenerico(BibliotecaControlador controlador) {
        System.out.println();
        System.out.println("=== FILTRO GENÉRICO POR TIPO ===");

        List<Libro> libros = controlador.filtrarPorTipo(Libro.class);
        for (Libro libro : libros) {
            System.out.println("Libro de " + libro.getAutor() + ": " + libro.getTitulo());
        }

        List<Revista> revistas = controlador.filtrarPorTipo(Revista.class);
        for (Revista revista : revistas) {
            System.out.println("Revista " + revista.getPeriodicidad() + ": " + revista.getTitulo());
        }
    }

    private static void mostrarOrdenaciones(BibliotecaControlador controlador) {
        System.out.println();
        System.out.println("=== ORDEN NATURAL (título) ===");
        System.out.println(controlador.listarMateriales());

        System.out.println();
        System.out.println("=== ORDEN POR COMPLEJIDAD ===");
        for (Material material : controlador.listarMaterialesPorComplejidad()) {
            System.out.println(material.getNivel() + " -> " + material);
        }
    }

    private static void mostrarFlujoDeReservas(BibliotecaControlador controlador) throws BibliotecaException {
        Material material = controlador.buscarExacto("R001");

        System.out.println();
        System.out.println("=== FLUJO DE RESERVAS sobre " + material + " ===");

        material.prestar("U001");
        System.out.println("U001 lo presta -> " + material.getEstado().getEtiqueta()
                + " | disponibles: " + controlador.contarDisponibles());

        material.reservar("U002");
        material.reservar("U003");
        System.out.println("Cola de reservas: " + material.getColaReservas());

        material.devolver();
        System.out.println("U001 lo devuelve -> " + material.getEstado().getEtiqueta()
                + " para " + material.getReservadoPara() + " | cola: " + material.getColaReservas());

        try {
            material.prestar("U003");
        } catch (BibliotecaException e) {
            System.out.println("U003 intenta prestarlo -> " + e.getMessage());
        }

        material.prestar("U002");
        System.out.println("U002 lo presta -> " + material.getEstado().getEtiqueta());
    }
}
