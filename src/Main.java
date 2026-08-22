import excepciones.BibliotecaException;
import modelo.Audiovisual;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Provisional: cuando la GUI esté lista, este main solo cargará los datos y abrirá VentanaPrincipal.
public class Main {

    public static void main(String[] args) throws BibliotecaException {
        List<Material> catalogo = crearCatalogo();

        mostrarCatalogoPolimorfico(catalogo);
        mostrarOrdenNatural(catalogo);
        mostrarFlujoDeReservas(catalogo.get(3));
    }

    public static List<Material> crearCatalogo() {
        List<Material> catalogo = new ArrayList<>();

        catalogo.add(new Libro("Cien años de soledad", "L001", 14, NivelComplejidad.BAJO,
                "imagenes/cien_anos.jpg", "Gabriel García Márquez", 471, "978-0307474728"));
        catalogo.add(new Libro("El principito", "L002", 14, NivelComplejidad.BAJO,
                "imagenes/el_principito.jpg", "Antoine de Saint-Exupéry", 96, "978-0156012195"));
        catalogo.add(new Libro("Introducción a los algoritmos", "L003", 21, NivelComplejidad.ALTO,
                "imagenes/algoritmos.jpg", "Thomas H. Cormen", 1312, "978-0262046305"));
        catalogo.add(new Revista("National Geographic", "R001", 10, NivelComplejidad.MEDIO,
                "imagenes/national_geographic.jpg", 245, Periodicidad.MENSUAL));
        catalogo.add(new Revista("Nature", "R002", 14, NivelComplejidad.ALTO,
                "imagenes/nature.jpg", 8123, Periodicidad.SEMANAL));
        catalogo.add(new Revista("Anuario Estadístico", "R003", 30, NivelComplejidad.MEDIO,
                "imagenes/anuario.jpg", 12, Periodicidad.ANUAL));
        catalogo.add(new Audiovisual("Interstellar", "A001", 14, NivelComplejidad.MEDIO,
                "imagenes/interstellar.jpg", 169, FormatoAV.BLURAY));
        catalogo.add(new Audiovisual("El Padrino", "A002", 10, NivelComplejidad.BAJO,
                "imagenes/el_padrino.jpg", 175, FormatoAV.DVD));
        catalogo.add(new Audiovisual("Documental: Física Cuántica", "A003", 12, NivelComplejidad.ALTO,
                "ruta/inexistente.jpg", 55, FormatoAV.BLURAY));

        return catalogo;
    }

    private static void mostrarCatalogoPolimorfico(List<Material> catalogo) {
        System.out.println("=== CATÁLOGO (recorrido polimórfico) ===");
        for (Material material : catalogo) {
            System.out.println(material.getDescripcion());
            System.out.println("   Días de préstamo: " + material.calcularDiasPrestamo()
                    + " | Estado: " + material.getEstado().getEtiqueta()
                    + " | Autorización especial: " + (material.getNivel().requiereAutorizacion() ? "sí" : "no"));
        }
    }

    private static void mostrarOrdenNatural(List<Material> catalogo) {
        List<Material> ordenados = new ArrayList<>(catalogo);
        Collections.sort(ordenados);

        System.out.println();
        System.out.println("=== ORDEN NATURAL (por título) ===");
        for (Material material : ordenados) {
            System.out.println(material);
        }
    }

    private static void mostrarFlujoDeReservas(Material material) throws BibliotecaException {
        System.out.println();
        System.out.println("=== FLUJO DE RESERVAS sobre " + material + " ===");

        material.prestar("U001");
        System.out.println("U001 lo presta -> " + material.getEstado().getEtiqueta());

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
