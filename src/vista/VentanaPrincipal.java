package vista;

//nombre generico en lo que terminan esa clase
import controlador.BibliotecaControlador;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal(BibliotecaControlador controlador) {
        super("Biblioteca - Sistema de gestión de préstamos");

        PanelMateriales panelMateriales = new PanelMateriales(controlador);
        PanelPrestamos panelPrestamos = new PanelPrestamos(controlador, panelMateriales);
        PanelUsuarios panelUsuarios = new PanelUsuarios(controlador);
        PanelPenalizaciones panelPenalizaciones = new PanelPenalizaciones(controlador);

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Materiales", panelMateriales);
        pestanias.addTab("Usuarios y préstamos", panelPrestamos);
        pestanias.addTab("Usuarios", panelUsuarios);
        pestanias.addTab("Penalizaciones", panelPenalizaciones);
        pestanias.addChangeListener(e -> {
            panelMateriales.refrescar();
            panelPrestamos.refrescar();
            panelUsuarios.refrescar();
            panelPenalizaciones.refrescar();
        });

        setContentPane(pestanias);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        pack();
        setLocationRelativeTo(null);
    }

    public static void iniciar(BibliotecaControlador controlador) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
            }
            new VentanaPrincipal(controlador).setVisible(true);
        });
    }
}
