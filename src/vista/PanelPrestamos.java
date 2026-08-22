package vista;

import controlador.BibliotecaControlador;
import excepciones.AutorizacionRequeridaException;
import excepciones.BibliotecaException;
import excepciones.LimitePrestamosException;
import excepciones.MaterialNoDisponibleException;
import excepciones.UsuarioPenalizadoException;
import modelo.Material;
import modelo.Prestamo;
import modelo.Usuario;
import modelo.UsuarioEstandar;
import modelo.UsuarioPremium;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class PanelPrestamos extends JPanel {

    private final BibliotecaControlador controlador;
    private final PanelMateriales panelMateriales;
    private final DefaultListModel<Usuario> modeloUsuarios = new DefaultListModel<>();
    private final JList<Usuario> listaUsuarios = new JList<>(modeloUsuarios);
    private final JComboBox<Material> comboMaterial = new JComboBox<>();
    private final JTextField campoIdUsuario = new JTextField(8);
    private final JTextField campoNombreUsuario = new JTextField(14);
    private final JComboBox<String> comboPerfil = new JComboBox<>(new String[]{"Estándar", "Premium"});
    private final JTextArea areaEstadoUsuario = new JTextArea(10, 30);

    public PanelPrestamos(BibliotecaControlador controlador, PanelMateriales panelMateriales) {
        super(new BorderLayout(8, 8));
        this.controlador = controlador;
        this.panelMateriales = panelMateriales;

        add(construirPanelUsuarios(), BorderLayout.WEST);
        add(construirPanelAcciones(), BorderLayout.CENTER);

        listaUsuarios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarEstadoUsuario(listaUsuarios.getSelectedValue());
            }
        });

        refrescar();
    }

    private JPanel construirPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Usuarios"));

        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

        JPanel formularioAlta = new JPanel();
        formularioAlta.setLayout(new BoxLayout(formularioAlta, BoxLayout.Y_AXIS));
        formularioAlta.setBorder(BorderFactory.createTitledBorder("Alta de usuario"));

        JPanel filaId = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaId.add(new JLabel("ID:"));
        filaId.add(campoIdUsuario);
        formularioAlta.add(filaId);

        JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaNombre.add(new JLabel("Nombre:"));
        filaNombre.add(campoNombreUsuario);
        formularioAlta.add(filaNombre);

        JPanel filaPerfil = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaPerfil.add(new JLabel("Perfil:"));
        filaPerfil.add(comboPerfil);
        formularioAlta.add(filaPerfil);

        JButton botonAgregarUsuario = new JButton("Agregar usuario");
        botonAgregarUsuario.addActionListener(e -> agregarUsuario());
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filaBoton.add(botonAgregarUsuario);
        formularioAlta.add(filaBoton);

        panel.add(formularioAlta, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Préstamos, devoluciones y reservas"));

        JPanel filaMaterial = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaMaterial.add(new JLabel("Material:"));
        comboMaterial.setPreferredSize(new java.awt.Dimension(320, comboMaterial.getPreferredSize().height));
        filaMaterial.add(comboMaterial);
        JButton botonActualizarMateriales = new JButton("Actualizar lista");
        botonActualizarMateriales.addActionListener(e -> refrescar());
        filaMaterial.add(botonActualizarMateriales);

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botonPrestar = new JButton("Prestar");
        botonPrestar.addActionListener(e -> prestar());
        JButton botonDevolver = new JButton("Devolver");
        botonDevolver.addActionListener(e -> devolver());
        JButton botonReservar = new JButton("Reservar");
        botonReservar.addActionListener(e -> reservar());
        JButton botonCancelarReserva = new JButton("Cancelar reserva");
        botonCancelarReserva.addActionListener(e -> cancelarReserva());
        JButton botonPenalizacion = new JButton("Consultar penalización");
        botonPenalizacion.addActionListener(e -> mostrarEstadoUsuario(listaUsuarios.getSelectedValue()));

        filaBotones.add(botonPrestar);
        filaBotones.add(botonDevolver);
        filaBotones.add(botonReservar);
        filaBotones.add(botonCancelarReserva);
        filaBotones.add(botonPenalizacion);

        areaEstadoUsuario.setEditable(false);
        areaEstadoUsuario.setLineWrap(true);
        areaEstadoUsuario.setWrapStyleWord(true);

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.add(filaMaterial);
        norte.add(filaBotones);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaEstadoUsuario), BorderLayout.CENTER);
        return panel;
    }

    private void agregarUsuario() {
        String id = campoIdUsuario.getText().trim();
        String nombre = campoNombreUsuario.getText().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID y el nombre del usuario son obligatorios.",
                    "Campos obligatorios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Usuario usuario = "Premium".equals(comboPerfil.getSelectedItem())
                ? new UsuarioPremium(id, nombre)
                : new UsuarioEstandar(id, nombre);

        controlador.altaUsuario(usuario);
        campoIdUsuario.setText("");
        campoNombreUsuario.setText("");
        refrescar();
        JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.",
                "Alta de usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    private void prestar() {
        Usuario usuario = usuarioSeleccionadoORequerido();
        Material material = materialSeleccionadoORequerido();
        if (usuario == null || material == null) {
            return;
        }
        try {
            controlador.prestar(usuario.getId(), material.getCodigo());
            actualizarTrasAccion(usuario, "\"" + material.getTitulo() + "\" prestado a " + usuario.getNombre() + ".");
        } catch (BibliotecaException ex) {
            mostrarError(ex);
        }
    }

    private void devolver() {
        Material material = materialSeleccionadoORequerido();
        if (material == null) {
            return;
        }
        try {
            controlador.devolver(material.getCodigo());
            actualizarTrasAccion(listaUsuarios.getSelectedValue(), "\"" + material.getTitulo() + "\" devuelto.");
        } catch (BibliotecaException ex) {
            mostrarError(ex);
        }
    }

    private void reservar() {
        Usuario usuario = usuarioSeleccionadoORequerido();
        Material material = materialSeleccionadoORequerido();
        if (usuario == null || material == null) {
            return;
        }
        try {
            controlador.reservar(usuario.getId(), material.getCodigo());
            actualizarTrasAccion(usuario, "Reserva registrada para " + usuario.getNombre()
                    + " sobre \"" + material.getTitulo() + "\".");
        } catch (BibliotecaException ex) {
            mostrarError(ex);
        }
    }

    private void cancelarReserva() {
        Usuario usuario = usuarioSeleccionadoORequerido();
        Material material = materialSeleccionadoORequerido();
        if (usuario == null || material == null) {
            return;
        }
        try {
            controlador.cancelarReserva(usuario.getId(), material.getCodigo());
            actualizarTrasAccion(usuario, "Reserva cancelada para " + usuario.getNombre()
                    + " sobre \"" + material.getTitulo() + "\".");
        } catch (BibliotecaException ex) {
            mostrarError(ex);
        }
    }

    private Usuario usuarioSeleccionadoORequerido() {
        Usuario usuario = listaUsuarios.getSelectedValue();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un usuario de la lista.",
                    "Falta seleccionar usuario", JOptionPane.WARNING_MESSAGE);
        }
        return usuario;
    }

    private Material materialSeleccionadoORequerido() {
        Material material = (Material) comboMaterial.getSelectedItem();
        if (material == null) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un material.",
                    "Falta seleccionar material", JOptionPane.WARNING_MESSAGE);
        }
        return material;
    }

    private void actualizarTrasAccion(Usuario usuario, String mensaje) {
        panelMateriales.refrescar();
        refrescarCombo();
        listaUsuarios.repaint();
        mostrarEstadoUsuario(usuario);
        JOptionPane.showMessageDialog(this, mensaje, "Operación realizada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(BibliotecaException ex) {
        if (ex instanceof MaterialNoDisponibleException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Material no disponible", JOptionPane.WARNING_MESSAGE);
        } else if (ex instanceof LimitePrestamosException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Límite de préstamos alcanzado", JOptionPane.WARNING_MESSAGE);
        } else if (ex instanceof AutorizacionRequeridaException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Autorización requerida", JOptionPane.ERROR_MESSAGE);
        } else if (ex instanceof UsuarioPenalizadoException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Usuario penalizado", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo completar la operación", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void refrescar() {
        Usuario seleccionActual = listaUsuarios.getSelectedValue();
        modeloUsuarios.clear();
        for (Usuario usuario : controlador.obtenerUsuarios()) {
            modeloUsuarios.addElement(usuario);
        }
        if (seleccionActual != null && modeloUsuarios.contains(seleccionActual)) {
            listaUsuarios.setSelectedValue(seleccionActual, true);
        } else if (!modeloUsuarios.isEmpty()) {
            listaUsuarios.setSelectedIndex(0);
        }
        refrescarCombo();
    }

    private void refrescarCombo() {
        Material seleccionActual = (Material) comboMaterial.getSelectedItem();
        comboMaterial.removeAllItems();
        for (Material material : controlador.obtenerCatalogo()) {
            comboMaterial.addItem(material);
        }
        if (seleccionActual != null) {
            comboMaterial.setSelectedItem(seleccionActual);
        }
    }

    private void mostrarEstadoUsuario(Usuario usuario) {
        if (usuario == null) {
            areaEstadoUsuario.setText("");
            return;
        }
        StringBuilder texto = new StringBuilder();
        texto.append(usuario).append("\n");
        texto.append("Límite de préstamos simultáneos: ").append(usuario.getLimitePrestamos()).append("\n");
        texto.append("Préstamos activos: ").append(usuario.getPrestamosActivos().size())
                .append(" / ").append(usuario.getLimitePrestamos()).append("\n");

        if (usuario.estaPenalizado()) {
            texto.append("PENALIZADO hasta: ").append(usuario.getPenalizadoHasta()).append("\n");
        } else {
            texto.append("Sin penalización vigente.\n");
        }

        try {
            int diasAcumulados = controlador.diasPenalizacionAcumulados(usuario.getId());
            texto.append("Días de penalización acumulados en su historial: ").append(diasAcumulados).append("\n");
        } catch (BibliotecaException ex) {
            texto.append("No se pudo calcular la penalización acumulada: ").append(ex.getMessage()).append("\n");
        }

        texto.append("\nPréstamos en curso:\n");
        List<Prestamo> activos = controlador.prestamosActivosDe(usuario.getId());
        if (activos.isEmpty()) {
            texto.append("  (ninguno)\n");
        } else {
            for (Prestamo prestamo : activos) {
                texto.append("  - ").append(prestamo).append("\n");
            }
        }

        areaEstadoUsuario.setText(texto.toString());
        areaEstadoUsuario.setCaretPosition(0);
    }
}
