package vista;

import controlador.BibliotecaControlador;
import modelo.Usuario;
import modelo.UsuarioEstandar;
import modelo.UsuarioPremium;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

public class PanelUsuarios extends JPanel {

    private final BibliotecaControlador controlador;
    private final DefaultListModel<Usuario> modeloUsuarios = new DefaultListModel<>();
    private final JList<Usuario> listaUsuarios = new JList<>(modeloUsuarios);
    private final JTextArea areaDetalle = new JTextArea(10, 30);
    private final JTextField campoBusqueda = new JTextField(14);
    private final JComboBox<String> comboFiltroPerfil = new JComboBox<>(new String[]{"Todos", "Estándar", "Premium"});
    private final JTextField campoIdUsuario = new JTextField(8);
    private final JTextField campoNombreUsuario = new JTextField(14);
    private final JComboBox<String> comboPerfilAlta = new JComboBox<>(new String[]{"Estándar", "Premium"});

    public PanelUsuarios(BibliotecaControlador controlador) {
        super(new BorderLayout(8, 8));
        this.controlador = controlador;

        add(construirBarraBusqueda(), BorderLayout.NORTH);

        JSplitPane splitCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPanelLista(), construirPanelDetalle());
        splitCentral.setResizeWeight(0.45);
        add(splitCentral, BorderLayout.CENTER);

        add(construirPanelAlta(), BorderLayout.SOUTH);

        listaUsuarios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle(listaUsuarios.getSelectedValue());
            }
        });

        refrescar();
    }

    private JPanel construirBarraBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        JButton botonBuscar = new JButton("Buscar por ID/nombre");
        botonBuscar.addActionListener(e -> buscarPorTexto());

        JButton botonFiltrarPerfil = new JButton("Filtrar por perfil");
        botonFiltrarPerfil.addActionListener(e -> filtrarPorPerfil());

        JButton botonTodos = new JButton("Mostrar todos");
        botonTodos.addActionListener(e -> refrescar());

        panel.add(new JLabel("ID o nombre:"));
        panel.add(campoBusqueda);
        panel.add(botonBuscar);
        panel.add(new JLabel("Perfil:"));
        panel.add(comboFiltroPerfil);
        panel.add(botonFiltrarPerfil);
        panel.add(botonTodos);
        return panel;
    }

    private JPanel construirPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Usuarios"));
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setCellRenderer(new RenderizadorUsuario());
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Detalle"));
        areaDetalle.setEditable(false);
        areaDetalle.setLineWrap(true);
        areaDetalle.setWrapStyleWord(true);
        panel.add(new JScrollPane(areaDetalle), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelAlta() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Alta de usuario"));

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.add(new JLabel("ID:"));
        fila.add(campoIdUsuario);
        fila.add(new JLabel("Nombre:"));
        fila.add(campoNombreUsuario);
        fila.add(new JLabel("Perfil:"));
        fila.add(comboPerfilAlta);

        JButton botonAgregar = new JButton("Agregar usuario");
        botonAgregar.addActionListener(e -> agregarUsuario());
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filaBoton.add(botonAgregar);

        panel.add(fila);
        panel.add(filaBoton);
        return panel;
    }

    private void buscarPorTexto() {
        String clave = campoBusqueda.getText().trim();
        if (clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un ID o nombre para buscar.",
                    "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Usuario> resultados = controlador.obtenerUsuarios().stream()
                .filter(usuario -> usuario.getId().equalsIgnoreCase(clave)
                        || usuario.getNombre().toLowerCase().contains(clave.toLowerCase()))
                .toList();
        cargarLista(resultados);
    }

    private void filtrarPorPerfil() {
        String perfil = (String) comboFiltroPerfil.getSelectedItem();
        if ("Todos".equals(perfil)) {
            refrescar();
            return;
        }
        List<Usuario> resultados = controlador.obtenerUsuarios().stream()
                .filter(usuario -> perfilLegible(usuario).equals(perfil))
                .toList();
        cargarLista(resultados);
    }

    private void agregarUsuario() {
        String id = campoIdUsuario.getText().trim();
        String nombre = campoNombreUsuario.getText().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID y el nombre del usuario son obligatorios.",
                    "Campos obligatorios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Usuario usuario = "Premium".equals(comboPerfilAlta.getSelectedItem())
                ? new UsuarioPremium(id, nombre)
                : new UsuarioEstandar(id, nombre);

        controlador.agregarUsuario(usuario);
        campoIdUsuario.setText("");
        campoNombreUsuario.setText("");
        refrescar();
        JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.",
                "Alta de usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refrescar() {
        cargarLista(controlador.obtenerUsuarios());
    }

    private void cargarLista(List<Usuario> usuarios) {
        Usuario seleccionActual = listaUsuarios.getSelectedValue();
        modeloUsuarios.clear();
        for (Usuario usuario : usuarios) {
            modeloUsuarios.addElement(usuario);
        }
        if (seleccionActual != null && modeloUsuarios.contains(seleccionActual)) {
            listaUsuarios.setSelectedValue(seleccionActual, true);
        } else if (!modeloUsuarios.isEmpty()) {
            listaUsuarios.setSelectedIndex(0);
        } else {
            mostrarDetalle(null);
        }
    }

    private void mostrarDetalle(Usuario usuario) {
        if (usuario == null) {
            areaDetalle.setText("");
            return;
        }
        StringBuilder texto = new StringBuilder();
        texto.append(usuario).append("\n");
        texto.append("Perfil: ").append(perfilLegible(usuario)).append("\n");
        texto.append("Límite de préstamos simultáneos: ").append(usuario.getLimitePrestamos()).append("\n");
        texto.append("Préstamos activos: ").append(usuario.getPrestamosActivos().size())
                .append(" / ").append(usuario.getLimitePrestamos()).append("\n");
        texto.append("Préstamos en historial: ").append(usuario.getHistorial().size()).append("\n");

        if (usuario.estaPenalizado()) {
            texto.append("PENALIZADO hasta: ").append(usuario.getPenalizadoHasta().getTime()).append("\n");
        } else {
            texto.append("Sin penalización vigente.\n");
        }

        areaDetalle.setText(texto.toString());
        areaDetalle.setCaretPosition(0);
    }

    private String perfilLegible(Usuario usuario) {
        if (usuario instanceof UsuarioPremium) {
            return "Premium";
        } else if (usuario instanceof UsuarioEstandar) {
            return "Estándar";
        }
        return "Desconocido";
    }

    private final class RenderizadorUsuario extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            if (value instanceof Usuario usuario) {
                etiqueta.setText(usuario.getNombre() + "  [" + usuario.getId() + "]  ("
                        + perfilLegible(usuario) + ")" + (usuario.estaPenalizado() ? "  ⚠ penalizado" : ""));
                if (usuario.estaPenalizado() && !isSelected) {
                    etiqueta.setForeground(new Color(178, 34, 34));
                }
            }
            return etiqueta;
        }
    }
}
