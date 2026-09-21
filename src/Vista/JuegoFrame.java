package Vista;

import javax.swing.*;
import java.awt.*;

public class JuegoFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;

    private MenuPanel menuPanel;
    private SeleccionPersonajePanel seleccionPersonajePanel;
    private NivelPanel nivelPanel;

    public JuegoFrame() {
        setTitle("Silent Cat");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Instanciación de paneles puros
        menuPanel = new MenuPanel();
        seleccionPersonajePanel = new SeleccionPersonajePanel();
        nivelPanel = new NivelPanel();

        panelContenedor.add(menuPanel, "menu");
        panelContenedor.add(seleccionPersonajePanel, "seleccion");
        panelContenedor.add(nivelPanel, "nivel");

        add(panelContenedor);
        setVisible(true);
    }

    public void mostrarPantalla(String pantalla) {
        cardLayout.show(panelContenedor, pantalla);
        if ("nivel".equals(pantalla)) {
            nivelPanel.requestFocusInWindow();
        }
    }

    // Getters de pantallas para los Controladores
    public MenuPanel getMenuPanel() { return menuPanel; }
    public SeleccionPersonajePanel getSeleccionPersonajePanel() { return seleccionPersonajePanel; }
    public NivelPanel getNivelPanel() { return nivelPanel; }
}

/*  La ventana contenedora principal
    Usa un CardLayout para cambiar entre pantallas 
    (Menú, Selección, Nivel, etc.) */

