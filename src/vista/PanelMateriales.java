package vista;

import controlador.BibliotecaControlador;
import modelo.Audiovisual;
import modelo.FormatoAV;
import modelo.Libro;
import modelo.Material;
import modelo.NivelComplejidad;
import modelo.Periodicidad;
import modelo.Revista;
import javax.swing.*;
import java.awt.*;

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
}
