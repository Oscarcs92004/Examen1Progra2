package controlador;

import excepciones.BibliotecaException;
import modelo.Audiovisual;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;
import servicio.Biblioteca;

import java.util.List;

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

    // Firmas acordadas para que Óscar arme la GUI ya. El cuerpo llega cuando Alex suba
    // modelo.Usuario, sus dos perfiles y modelo.Prestamo. Ninguna cambia de firma.

    public void altaUsuario(String id, String nombre, boolean premium) {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public List<String> listarUsuarios() {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public void prestar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario y modelo.Prestamo (Alex).");
    }

    public void devolver(String idUsuario, String codigoMaterial) throws BibliotecaException {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario y modelo.Prestamo (Alex).");
    }

    public void reservar(String idUsuario, String codigoMaterial) throws BibliotecaException {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public void cancelarReserva(String idUsuario, String codigoMaterial) throws BibliotecaException {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public List<String> listarPrestamosActivos() {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Prestamo (Alex).");
    }

    public int diasPenalizacionAcumulados(String idUsuario) {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public boolean estaPenalizado(String idUsuario) {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Usuario (Alex).");
    }

    public List<String> materialesMasSolicitados() {
        throw new UnsupportedOperationException("Pendiente: requiere modelo.Prestamo (Alex).");
    }
}
