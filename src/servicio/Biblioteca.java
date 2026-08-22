package servicio;

import interfaces.Prestable;
import modelo.ComparadorPorComplejidad;
import modelo.Material;
import modelo.NivelComplejidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Biblioteca {

    private final List<Material> materiales = new ArrayList<>();

    public void registrarMaterial(Material material) {
        materiales.add(material);
    }

    public List<Material> getMateriales() {
        return new ArrayList<>(materiales);
    }

    public Material buscarExacto(String tituloOCodigo) {
        return buscarExacto(materiales, tituloOCodigo, 0);
    }

    private Material buscarExacto(List<Material> lista, String tituloOCodigo, int indice) {
        if (indice == lista.size()) {
            return null;
        }
        Material actual = lista.get(indice);
        if (actual.getTitulo().equalsIgnoreCase(tituloOCodigo)
                || actual.getCodigo().equalsIgnoreCase(tituloOCodigo)) {
            return actual;
        }
        return buscarExacto(lista, tituloOCodigo, indice + 1);
    }

    public List<Material> buscarPorNivel(NivelComplejidad nivel) {
        return buscarPorNivel(materiales, nivel, 0, new ArrayList<>());
    }

    private List<Material> buscarPorNivel(List<Material> lista, NivelComplejidad nivel,
                                          int indice, List<Material> acumulado) {
        if (indice == lista.size()) {
            return acumulado;
        }
        Material actual = lista.get(indice);
        if (actual.getNivel() == nivel) {
            acumulado.add(actual);
        }
        return buscarPorNivel(lista, nivel, indice + 1, acumulado);
    }

    public List<Material> buscarPorTituloParcial(String fragmento) {
        return buscarPorTituloParcial(materiales, fragmento.toLowerCase(), 0, new ArrayList<>());
    }

    private List<Material> buscarPorTituloParcial(List<Material> lista, String fragmento,
                                                  int indice, List<Material> acumulado) {
        if (indice == lista.size()) {
            return acumulado;
        }
        Material actual = lista.get(indice);
        if (actual.getTitulo().toLowerCase().contains(fragmento)) {
            acumulado.add(actual);
        }
        return buscarPorTituloParcial(lista, fragmento, indice + 1, acumulado);
    }

    public <T extends Material> List<T> filtrarPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Material material : materiales) {
            if (tipo.isInstance(material)) {
                resultado.add(tipo.cast(material));
            }
        }
        return resultado;
    }

    public List<Material> getMaterialesPorTitulo() {
        List<Material> ordenados = getMateriales();
        Collections.sort(ordenados);
        return ordenados;
    }

    public List<Material> getMaterialesPorComplejidad() {
        List<Material> ordenados = getMateriales();
        ordenados.sort(new ComparadorPorComplejidad());
        return ordenados;
    }

    public List<Prestable> getInventarioPrestable() {
        return new ArrayList<>(materiales);
    }

    public int contarDisponibles() {
        int disponibles = 0;
        for (Prestable prestable : getInventarioPrestable()) {
            if (prestable.estaDisponible()) {
                disponibles++;
            }
        }
        return disponibles;
    }
}
