package servicio;

import excepciones.AutorizacionRequeridaException;
import excepciones.BibliotecaException;
import excepciones.LimitePrestamosException;
import interfaces.Prestable;
import modelo.ComparadorPorComplejidad;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Prestamo;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca {

    private static final int DIAS_PENALIZACION_POR_DIA_RETRASO = 2;

    private final List<Material> materiales = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Prestamo> historial = new ArrayList<>();

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

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuario(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                return usuario;
            }
        }
        return null;
    }

    public void prestar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        Usuario usuario = exigirUsuario(idUsuario);
        Material material = exigirMaterial(codigoMaterial);

        if (usuario.estaPenalizado()) {
            throw new BibliotecaException("El usuario " + usuario.getNombre()
                    + " tiene una penalización vigente y no puede solicitar préstamos.");
        }
        if (usuario.getPrestamosActivos().size() >= usuario.getLimitePrestamos()) {
            throw new LimitePrestamosException(usuario.getNombre(), usuario.getLimitePrestamos());
        }
        if (!usuario.puedeAcceder(material.getNivel())) {
            throw new AutorizacionRequeridaException(usuario.getNombre(), material.getTitulo(),
                    material.getNivel().name());
        }

        material.prestar(usuario.getId());

        Prestamo prestamo = new Prestamo(usuario, material, Calendar.getInstance(),
                material.calcularDiasPrestamo());
        usuario.getPrestamosActivos().add(material);
        usuario.getHistorial().add(prestamo);
        historial.add(prestamo);
    }

    public void devolver(String idUsuario, String codigoMaterial) throws BibliotecaException {
        Usuario usuario = exigirUsuario(idUsuario);
        Material material = exigirMaterial(codigoMaterial);

        Prestamo prestamo = buscarPrestamoActivo(usuario, material);
        if (prestamo == null) {
            throw new BibliotecaException("El usuario " + usuario.getNombre()
                    + " no tiene prestado el material \"" + material.getTitulo() + "\".");
        }

        prestamo.setFechaDevolucionReal(Calendar.getInstance());
        int diasRetraso = prestamo.getDiasRetraso();
        if (diasRetraso > 0) {
            Calendar fin = Calendar.getInstance();
            fin.add(Calendar.DATE, diasRetraso * DIAS_PENALIZACION_POR_DIA_RETRASO);
            usuario.setPenalizadoHasta(fin);
        }

        usuario.getPrestamosActivos().remove(material);
        material.devolver();
    }

    public void reservar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        Usuario usuario = exigirUsuario(idUsuario);
        Material material = exigirMaterial(codigoMaterial);

        if (!usuario.puedeReservar()) {
            throw new BibliotecaException("El perfil de " + usuario.getNombre()
                    + " no permite reservar materiales. Solo los usuarios premium pueden hacerlo.");
        }
        if (material.estaDisponible()) {
            throw new BibliotecaException("El material \"" + material.getTitulo()
                    + "\" está disponible: puede prestarse directamente sin reservar.");
        }
        material.reservar(usuario.getId());
    }

    public void cancelarReserva(String idUsuario, String codigoMaterial) throws BibliotecaException {
        Usuario usuario = exigirUsuario(idUsuario);
        exigirMaterial(codigoMaterial).cancelarReserva(usuario.getId());
    }

    public List<Prestamo> getPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo prestamo : historial) {
            if (prestamo.estaActivo()) {
                activos.add(prestamo);
            }
        }
        return activos;
    }

    public List<Prestamo> getHistorial() {
        return new ArrayList<>(historial);
    }

    public int diasPenalizacionAcumulados(String idUsuario) throws BibliotecaException {
        return exigirUsuario(idUsuario).sumarDiasPenalizacion(0);
    }

    public boolean estaPenalizado(String idUsuario) throws BibliotecaException {
        return exigirUsuario(idUsuario).estaPenalizado();
    }

    public Map<Material, Integer> getMasSolicitados() {
        Map<Material, Integer> conteo = new LinkedHashMap<>();
        for (Prestamo prestamo : historial) {
            Material material = prestamo.getMaterial();
            conteo.put(material, conteo.getOrDefault(material, 0) + 1);
        }
        List<Map.Entry<Material, Integer>> entradas = new ArrayList<>(conteo.entrySet());
        entradas.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        Map<Material, Integer> ordenado = new LinkedHashMap<>();
        for (Map.Entry<Material, Integer> entrada : entradas) {
            ordenado.put(entrada.getKey(), entrada.getValue());
        }
        return ordenado;
    }

    private Prestamo buscarPrestamoActivo(Usuario usuario, Material material) {
        for (Prestamo prestamo : historial) {
            if (prestamo.estaActivo() && prestamo.getUsuario() == usuario
                    && prestamo.getMaterial() == material) {
                return prestamo;
            }
        }
        return null;
    }

    private Usuario exigirUsuario(String id) throws BibliotecaException {
        Usuario usuario = buscarUsuario(id);
        if (usuario == null) {
            throw new BibliotecaException("No existe ningún usuario con el identificador " + id + ".");
        }
        return usuario;
    }

    private Material exigirMaterial(String codigo) throws BibliotecaException {
        Material material = buscarExacto(codigo);
        if (material == null) {
            throw new BibliotecaException("No existe ningún material con el código " + codigo + ".");
        }
        return material;
    }
}
