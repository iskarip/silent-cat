package Vista;
import javax.swing.*;
import java.awt.*;


public class JuegoFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;

    public JuegoFrame() {
        setTitle("Silent Cat");
        setSize(800, 600);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        panelContenedor.add (new MenuPanel(this), "menu");
        panelContenedor.add (new SeleccionPersonajePanel(this), "seleccion");
        panelContenedor.add (new AcertijoPanel(this), "acertijo");

        add(panelContenedor);

        setVisible(true);

    }
    // Este método es el que van a llamar desde cualquier pantalla
    // para pedirle a la ventana "mostrame otra carta"
    public void mostrarPantalla (String pantalla){
        cardLayout.show(panelContenedor, pantalla);
    }


/*  La ventana contenedora principal
    Usa un CardLayout para cambiar entre pantallas 
    (Menú, Selección, Nivel, etc.) */
    
/*Este archivo va a sostener todo nuestro juego
va a intercambiar las pantallas sin tener que abrir ni 
cerrar ventanas nuevas */



}
