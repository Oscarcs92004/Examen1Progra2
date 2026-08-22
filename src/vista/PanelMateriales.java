package vista;

import controlador.BibliotecaControlador;
import modelo.Audiovisual;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class PanelMateriales extends JPanel {
    private final BibliotecaControlador controlador;
    private final DefaultListModel<Material> modeloLista = new DefaultListModel<>();
    private final JList<Material> listaMateriales = new JList<>(modeloLista);
    private final JLabel etiquetaImagen = new JLabel();
    private final JTextArea areaDetalle = new JTextArea(6, 30);
    private final JLabel etiquetaNivel = new JLabel(" ", SwingConstants.CENTER);
    private final JTextField campoBusqueda = new JTextField(14);
    private final JComboBox<NivelComplejidad> comboNivelBusqueda = new JComboBox<>(NivelComplejidad.values());
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Libro", "Revista", "Audiovisual"});
    private final JTextField campoTitulo = new JTextField(16);
    private final JTextField campoCodigo = new JTextField(8);
    private final JTextField campoDiasBase = new JTextField(4);
    private final JComboBox<NivelComplejidad> comboNivelAlta = new JComboBox<>(NivelComplejidad.values());
    private final JTextField campoRutaImagen = new JTextField(14);
    private final JTextField campoAutor = new JTextField(14);
    private final JTextField campoPaginas = new JTextField(6);
    private final JTextField campoIsbn = new JTextField(12);
    private final JTextField campoEdicion = new JTextField(6);
    private final JComboBox<Periodicidad> comboPeriodicidad = new JComboBox<>(Periodicidad.values());
    private final JTextField campoDuracion = new JTextField(6);
    private final JComboBox<FormatoAV> comboFormato = new JComboBox<>(FormatoAV.values());
    private final JPanel panelCamposLibro = new JPanel(new GridLayout(0, 2, 4, 4));
    private final JPanel panelCamposRevista = new JPanel(new GridLayout(0, 2, 4, 4));
    private final JPanel panelCamposAv = new JPanel(new GridLayout(0, 2, 4, 4));
    private final JPanel panelCamposEspecificos = new JPanel(new BorderLayout());

    public PanelMateriales(BibliotecaControlador controlador) {
        super(new BorderLayout(8, 8));
        this.controlador = controlador;

        add(construirBarraBusqueda(), BorderLayout.NORTH);

        JSplitPane splitCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirPanelLista(), construirPanelDetalle());
        splitCentral.setResizeWeight(0.45);
        add(splitCentral, BorderLayout.CENTER);

        add(construirPanelAlta(), BorderLayout.SOUTH);

        listaMateriales.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle(listaMateriales.getSelectedValue());
            }
        });

        actualizarCamposEspecificos();
        comboTipo.addActionListener(e -> actualizarCamposEspecificos());

        refrescar();
    }

    private JPanel construirBarraBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        JButton botonExacta = new JButton("Buscar por título/código");
        botonExacta.addActionListener(e -> buscar());

        JButton botonPorNivel = new JButton("Ver por nivel");
        botonPorNivel.addActionListener(e -> buscarPorNivel());

        JButton botonTodos = new JButton("Mostrar todo el catálogo");
        botonTodos.addActionListener(e -> refrescar());

        JButton botonOrdenarComplejidad = new JButton("Ordenar por complejidad");
        botonOrdenarComplejidad.addActionListener(e -> ordenarPorComplejidad());

        panel.add(new JLabel("Título o código:"));
        panel.add(campoBusqueda);
        panel.add(botonExacta);
        panel.add(new JLabel("Nivel:"));
        panel.add(comboNivelBusqueda);
        panel.add(botonPorNivel);
        panel.add(botonOrdenarComplejidad);
        panel.add(botonTodos);
        return panel;
    }

    private JPanel construirPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Catálogo"));
        listaMateriales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMateriales.setCellRenderer(new RenderizadorMaterial());
        panel.add(new JScrollPane(listaMateriales), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Detalle"));

        etiquetaImagen.setPreferredSize(new Dimension(UtilImagen.ANCHO, UtilImagen.ALTO));
        etiquetaImagen.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaImagen.setIcon(UtilImagen.cargarPortada(null));

        etiquetaNivel.setOpaque(true);
        etiquetaNivel.setPreferredSize(new Dimension(UtilImagen.ANCHO, 22));
        etiquetaNivel.setFont(etiquetaNivel.getFont().deriveFont(Font.BOLD));

        JPanel panelImagen = new JPanel();
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.add(etiquetaImagen);
        panelImagen.add(etiquetaNivel);

        areaDetalle.setEditable(false);
        areaDetalle.setLineWrap(true);
        areaDetalle.setWrapStyleWord(true);

        panel.add(panelImagen, BorderLayout.WEST);
        panel.add(new JScrollPane(areaDetalle), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelAlta() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Alta de material", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel filaComunes1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaComunes1.add(new JLabel("Tipo:"));
        filaComunes1.add(comboTipo);
        filaComunes1.add(new JLabel("Título:"));
        filaComunes1.add(campoTitulo);
        filaComunes1.add(new JLabel("Código:"));
        filaComunes1.add(campoCodigo);

        JPanel filaComunes2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaComunes2.add(new JLabel("Días base de préstamo:"));
        filaComunes2.add(campoDiasBase);
        filaComunes2.add(new JLabel("Nivel de complejidad:"));
        filaComunes2.add(comboNivelAlta);
        filaComunes2.add(new JLabel("Imagen (portada):"));
        filaComunes2.add(campoRutaImagen);
        JButton botonExaminar = new JButton("Examinar...");
        botonExaminar.addActionListener(e -> elegirImagen());
        filaComunes2.add(botonExaminar);

        panelCamposLibro.add(new JLabel("Autor:"));
        panelCamposLibro.add(campoAutor);
        panelCamposLibro.add(new JLabel("Nº páginas:"));
        panelCamposLibro.add(campoPaginas);
        panelCamposLibro.add(new JLabel("ISBN:"));
        panelCamposLibro.add(campoIsbn);

        panelCamposRevista.add(new JLabel("Nº edición:"));
        panelCamposRevista.add(campoEdicion);
        panelCamposRevista.add(new JLabel("Periodicidad:"));
        panelCamposRevista.add(comboPeriodicidad);

        panelCamposAv.add(new JLabel("Duración (min):"));
        panelCamposAv.add(campoDuracion);
        panelCamposAv.add(new JLabel("Formato:"));
        panelCamposAv.add(comboFormato);

        JButton botonAgregar = new JButton("Agregar material");
        botonAgregar.addActionListener(e -> agregarMaterial());

        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filaBoton.add(botonAgregar);

        panel.add(filaComunes1);
        panel.add(filaComunes2);
        panel.add(panelCamposEspecificos);
        panel.add(filaBoton);
        return panel;
    }

    private void actualizarCamposEspecificos() {
        panelCamposEspecificos.removeAll();
        String tipo = (String) comboTipo.getSelectedItem();
        if ("Libro".equals(tipo)) {
            panelCamposEspecificos.add(panelCamposLibro, BorderLayout.CENTER);
        } else if ("Revista".equals(tipo)) {
            panelCamposEspecificos.add(panelCamposRevista, BorderLayout.CENTER);
        } else {
            panelCamposEspecificos.add(panelCamposAv, BorderLayout.CENTER);
        }
        panelCamposEspecificos.revalidate();
        panelCamposEspecificos.repaint();
    }

    private void elegirImagen() {
        JFileChooser selector = new JFileChooser();
        int resultado = selector.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            campoRutaImagen.setText(selector.getSelectedFile().getPath());
        }
    }

    private void buscar() {
        String clave = campoBusqueda.getText().trim();
        if (clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un título o código para buscar.",
                    "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Material> resultados = controlador.buscar(clave);
        if (resultados.isEmpty()) {
            modeloLista.clear();
            JOptionPane.showMessageDialog(this, "No se encontró ningún material con \"" + clave + "\".",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        cargarLista(resultados);
        listaMateriales.setSelectedIndex(0);
    }

    private void buscarPorNivel() {
        NivelComplejidad nivel = (NivelComplejidad) comboNivelBusqueda.getSelectedItem();
        List<Material> resultados = controlador.buscarPorNivel(nivel);
        cargarLista(resultados);
    }

    private void ordenarPorComplejidad() {
        List<Material> actuales = List.copyOf(java.util.Collections.list(modeloLista.elements()));
        List<Material> copia = new java.util.ArrayList<>(actuales);
        copia.sort(servicio.Biblioteca.POR_COMPLEJIDAD);
        cargarLista(copia);
    }

    private void agregarMaterial() {
        try {
            String titulo = requerido(campoTitulo, "título");
            String codigo = requerido(campoCodigo, "código");
            int diasBase = Integer.parseInt(requerido(campoDiasBase, "días base"));
            NivelComplejidad nivel = (NivelComplejidad) comboNivelAlta.getSelectedItem();
            String rutaImagen = campoRutaImagen.getText().trim();

            String tipo = (String) comboTipo.getSelectedItem();
            Material material;
            if ("Libro".equals(tipo)) {
                material = new Libro(titulo, codigo, diasBase, nivel, rutaImagen,
                        requerido(campoAutor, "autor"),
                        Integer.parseInt(requerido(campoPaginas, "número de páginas")),
                        requerido(campoIsbn, "ISBN"));
            } else if ("Revista".equals(tipo)) {
                material = new Revista(titulo, codigo, diasBase, nivel, rutaImagen,
                        Integer.parseInt(requerido(campoEdicion, "número de edición")),
                        (Periodicidad) comboPeriodicidad.getSelectedItem());
            } else {
                material = new Audiovisual(titulo, codigo, diasBase, nivel, rutaImagen,
                        Integer.parseInt(requerido(campoDuracion, "duración")),
                        (FormatoAV) comboFormato.getSelectedItem());
            }

            controlador.agregarMaterial(material);
            limpiarFormularioAlta();
            refrescar();
            JOptionPane.showMessageDialog(this, "Material agregado correctamente.",
                    "Alta de material", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisa los campos numéricos: " + ex.getMessage(),
                    "Dato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String requerido(JTextField campo, String nombre) {
        String valor = campo.getText().trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("El campo \"" + nombre + "\" es obligatorio.");
        }
        return valor;
    }

    private void limpiarFormularioAlta() {
        campoTitulo.setText("");
        campoCodigo.setText("");
        campoDiasBase.setText("");
        campoRutaImagen.setText("");
        campoAutor.setText("");
        campoPaginas.setText("");
        campoIsbn.setText("");
        campoEdicion.setText("");
        campoDuracion.setText("");
    }

    public void refrescar() {
        cargarLista(controlador.obtenerCatalogo());
    }

    private void cargarLista(List<Material> materiales) {
        Material seleccionActual = listaMateriales.getSelectedValue();
        modeloLista.clear();
        for (Material material : materiales) {
            modeloLista.addElement(material);
        }
        if (seleccionActual != null && modeloLista.contains(seleccionActual)) {
            listaMateriales.setSelectedValue(seleccionActual, true);
        } else if (!modeloLista.isEmpty()) {
            listaMateriales.setSelectedIndex(0);
        } else {
            mostrarDetalle(null);
        }
    }

    private void mostrarDetalle(Material material) {
        if (material == null) {
            etiquetaImagen.setIcon(UtilImagen.cargarPortada(null));
            etiquetaNivel.setText(" ");
            etiquetaNivel.setBackground(getBackground());
            areaDetalle.setText("");
            return;
        }
        etiquetaImagen.setIcon(UtilImagen.cargarPortada(material.getRutaImagen()));
        etiquetaNivel.setText(material.getNivel().name());
        etiquetaNivel.setBackground(colorParaNivel(material.getNivel()));
        etiquetaNivel.setForeground(Color.WHITE);

        areaDetalle.setText(material.getDescripcion() + "\n\n"
                + "Días de préstamo (con complejidad ya aplicada): " + material.calcularDiasPrestamo() + "\n"
                + "Estado actual: " + material.getEstado().getEtiqueta() + "\n"
                + "Reservas pendientes: " + material.getColaReservas());
        areaDetalle.setCaretPosition(0);
    }

    private Color colorParaNivel(NivelComplejidad nivel) {
        return switch (nivel) {
            case BAJO -> new Color(46, 139, 87);
            case MEDIO -> new Color(214, 137, 16);
            case ALTO -> new Color(178, 34, 34);
        };
    }

    private static final class RenderizadorMaterial extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            if (value instanceof Material material) {
                String tipo = tipoLegible(material);
                etiqueta.setText(material.getTitulo() + "  [" + material.getCodigo() + "]  ("
                        + tipo + ", " + material.getEstado().getEtiqueta() + ")");
            }
            return etiqueta;
        }

        private String tipoLegible(Material material) {
            if (material instanceof Libro) {
                return "Libro";
            } else if (material instanceof Revista) {
                return "Revista";
            } else if (material instanceof Audiovisual) {
                return "Audiovisual";
            }
            return "Material";
        }
    }
}
