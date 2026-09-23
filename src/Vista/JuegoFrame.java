package Vista;

import javax.swing.*;
import java.awt.*;

public class JuegoFrame extends JFrame {

    private  CardLayout cardLayout;
    private  JPanel panelContenedor;

    private MenuPanel menuPanel;
    private SeleccionPersonajePanel seleccionPersonajePanel;
    private NivelPanel nivelPanel;
    private AcertijoPanel acertijoPanel;

    public JuegoFrame() {
        setTitle("Silent Cat");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Instanciación exclusiva de  paneles
        menuPanel = new MenuPanel();
        seleccionPersonajePanel = new SeleccionPersonajePanel();
        nivelPanel = new NivelPanel();
        acertijoPanel = new AcertijoPanel();

        //Registro en el CardLayout
        panelContenedor.add(menuPanel, "menu");
        panelContenedor.add(seleccionPersonajePanel, "seleccion");
        panelContenedor.add(nivelPanel, "nivel");
        panelContenedor.add(acertijoPanel, "acertijo");

        add(panelContenedor);
        setVisible(true);
    }

    // Alternar entre pantallas mediante CardLayout
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
    public AcertijoPanel getAcertijoPanel() { return acertijoPanel; }

}

/*  La ventana contenedora principal
    Usa un CardLayout para cambiar entre pantallas 
    (Menú, Selección, Nivel, etc.) */

