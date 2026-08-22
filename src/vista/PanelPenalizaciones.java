package vista;

import controlador.BibliotecaControlador;
import excepciones.BibliotecaException;
import modelo.Prestamo;
import modelo.Usuario;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

public class PanelPenalizaciones extends JPanel {

    private final BibliotecaControlador controlador;
    private final DefaultListModel<Usuario> modeloUsuarios = new DefaultListModel<>();
    private final JList<Usuario> listaUsuarios = new JList<>(modeloUsuarios);
    private final JTextArea areaPenalizacion = new JTextArea(10, 30);

    public PanelPenalizaciones(BibliotecaControlador controlador) {
        super(new BorderLayout(8, 8));
        this.controlador = controlador;

        add(construirPanelUsuarios(), BorderLayout.WEST);
        add(construirPanelDetalle(), BorderLayout.CENTER);

        listaUsuarios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarPenalizacion(listaUsuarios.getSelectedValue());
            }
        });

        refrescar();
    }

    private JPanel construirPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Usuarios"));

        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setCellRenderer(new RenderizadorUsuario());
        panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

        JButton botonActualizar = new JButton("Actualizar lista");
        botonActualizar.addActionListener(e -> refrescar());
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filaBoton.add(botonActualizar);
        panel.add(filaBoton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel construirPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Estado de penalización"));

        JButton botonConsultar = new JButton("Consultar penalización");
        botonConsultar.addActionListener(e -> mostrarPenalizacion(listaUsuarios.getSelectedValue()));
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaBotones.add(botonConsultar);

        areaPenalizacion.setEditable(false);
        areaPenalizacion.setLineWrap(true);
        areaPenalizacion.setWrapStyleWord(true);

        panel.add(filaBotones, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaPenalizacion), BorderLayout.CENTER);
        return panel;
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
        } else {
            mostrarPenalizacion(null);
        }
    }

    private void mostrarPenalizacion(Usuario usuario) {
        if (usuario == null) {
            areaPenalizacion.setText("");
            return;
        }
        StringBuilder texto = new StringBuilder();
        texto.append(usuario).append("\n\n");

        if (usuario.estaPenalizado()) {
            texto.append("PENALIZADO hasta: ").append(usuario.getPenalizadoHasta().getTime()).append("\n");
        } else {
            texto.append("Sin penalización vigente.\n");
        }

        try {
            int diasAcumulados = controlador.diasPenalizacionAcumulados(usuario.getId());
            texto.append("Días de penalización acumulados en su historial: ").append(diasAcumulados).append("\n");
        } catch (BibliotecaException ex) {
            texto.append("No se pudo calcular la penalización acumulada: ").append(ex.getMessage()).append("\n");
        }

        texto.append("\nHistorial de préstamos:\n");
        List<Prestamo> historial = usuario.getHistorial();
        if (historial.isEmpty()) {
            texto.append("  (sin préstamos registrados)\n");
        } else {
            for (Prestamo prestamo : historial) {
                texto.append("  - ").append(prestamo)
                        .append("  (").append(prestamo.getDiasRetraso()).append(" día(s) de retraso")
                        .append(prestamo.estaVencido() ? ", VENCIDO" : "").append(")\n");
            }
        }

        areaPenalizacion.setText(texto.toString());
        areaPenalizacion.setCaretPosition(0);
    }

    private final class RenderizadorUsuario extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            if (value instanceof Usuario usuario) {
                etiqueta.setText(usuario.getNombre() + "  [" + usuario.getId() + "]"
                        + (usuario.estaPenalizado() ? "  ⚠ penalizado" : ""));
                if (usuario.estaPenalizado() && !isSelected) {
                    etiqueta.setForeground(new Color(178, 34, 34));
                }
            }
            return etiqueta;
        }
    }
}
