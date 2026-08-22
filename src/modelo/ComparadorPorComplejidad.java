package modelo;

import java.util.Comparator;

public class ComparadorPorComplejidad implements Comparator<Material> {

    @Override
    public int compare(Material primero, Material segundo) {
        int porNivel = Integer.compare(primero.getNivel().getOrden(), segundo.getNivel().getOrden());
        if (porNivel != 0) {
            return porNivel;
        }
        return primero.compareTo(segundo);
    }
}
