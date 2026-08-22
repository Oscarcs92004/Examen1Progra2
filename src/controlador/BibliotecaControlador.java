package controlador;

import excepciones.BibliotecaException;
import modelo.Audiovisual;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;
import modelo.Prestamo;
import modelo.Usuario;
import modelo.UsuarioEstandar;
import modelo.UsuarioPremium;
import servicio.Biblioteca;

import java.util.List;
import java.util.Map;

public class BibliotecaControlador {

    private final Biblioteca biblioteca;

    public BibliotecaControlador(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void altaLibro(String titulo, String codigo, int diasBase, NivelComplejidad nivel,
                          String rutaImagen, String autor, int numeroPaginas, String isbn) {
        biblioteca.registrarMaterial(
                new Libro(titulo, codigo, diasBase, nivel, rutaImagen, autor, numeroPaginas, isbn));
    }

    public void altaRevista(String titulo, String codigo, int diasBase, NivelComplejidad nivel,
                            String rutaImagen, int numeroEdicion, Periodicidad periodicidad) {
        biblioteca.registrarMaterial(
                new Revista(titulo, codigo, diasBase, nivel, rutaImagen, numeroEdicion, periodicidad));
    }

    public void altaAudiovisual(String titulo, String codigo, int diasBase, NivelComplejidad nivel,
                                String rutaImagen, int duracionMinutos, FormatoAV formato) {
        biblioteca.registrarMaterial(
                new Audiovisual(titulo, codigo, diasBase, nivel, rutaImagen, duracionMinutos, formato));
    }

    public List<Material> listarMateriales() {
        return biblioteca.getMaterialesPorTitulo();
    }

    public List<Material> listarMaterialesPorComplejidad() {
        return biblioteca.getMaterialesPorComplejidad();
    }

    public Material buscarExacto(String tituloOCodigo) {
        return biblioteca.buscarExacto(tituloOCodigo);
    }

    public List<Material> buscarPorTituloParcial(String fragmento) {
        return biblioteca.buscarPorTituloParcial(fragmento);
    }

    public List<Material> buscarPorNivel(NivelComplejidad nivel) {
        return biblioteca.buscarPorNivel(nivel);
    }

    public <T extends Material> List<T> filtrarPorTipo(Class<T> tipo) {
        return biblioteca.filtrarPorTipo(tipo);
    }

    public int contarDisponibles() {
        return biblioteca.contarDisponibles();
    }

    public String describir(Material material) {
        return material.getDescripcion();
    }

    public int diasDePrestamo(Material material) {
        return material.calcularDiasPrestamo();
    }

    public void altaUsuario(String id, String nombre, boolean premium) {
        biblioteca.registrarUsuario(premium ? new UsuarioPremium(id, nombre) : new UsuarioEstandar(id, nombre));
    }

    public List<Usuario> listarUsuarios() {
        return biblioteca.getUsuarios();
    }

    public void prestar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        biblioteca.prestar(idUsuario, codigoMaterial);
    }

    public void devolver(String idUsuario, String codigoMaterial) throws BibliotecaException {
        biblioteca.devolver(idUsuario, codigoMaterial);
    }

    public void reservar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        biblioteca.reservar(idUsuario, codigoMaterial);
    }

    public void cancelarReserva(String idUsuario, String codigoMaterial) throws BibliotecaException {
        biblioteca.cancelarReserva(idUsuario, codigoMaterial);
    }

    public List<Prestamo> listarPrestamosActivos() {
        return biblioteca.getPrestamosActivos();
    }

    public int diasPenalizacionAcumulados(String idUsuario) throws BibliotecaException {
        return biblioteca.diasPenalizacionAcumulados(idUsuario);
    }

    public boolean estaPenalizado(String idUsuario) throws BibliotecaException {
        return biblioteca.estaPenalizado(idUsuario);
    }

    public Map<Material, Integer> materialesMasSolicitados() {
        return biblioteca.getMasSolicitados();
    }
}
