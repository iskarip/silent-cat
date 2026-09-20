package Vista;
import javax.swing.*;
import java.awt.*;
import Modelo.Personaje;
import Modelo.Enemigo;
import java.util.ArrayList;
import java.util.List;
import Controlador.ControladorNivel;


public class JuegoFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private NivelPanel nivelPanel;
    private ControladorNivel controladorNivel;

    public JuegoFrame() {
        setTitle("Silent Cat");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // para que se muestre en pantalla completa
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);


        panelContenedor.add (new MenuPanel(this), "menu");
        panelContenedor.add (new SeleccionPersonajePanel(this), "seleccion");
        panelContenedor.add (new AcertijoPanel(this), "acertijo");

        nivelPanel = new NivelPanel();
        nivelPanel.setVentanaPrincipal(this);
        controladorNivel = new ControladorNivel(nivelPanel); // se arma el controlador junto con la vista
        panelContenedor.add(nivelPanel, "nivel");

        add(panelContenedor);

        setVisible(true);

    }
    // Este método es el que van a llamar desde cualquier pantalla
    // para pedirle a la ventana "mostrame otra carta"
    public void mostrarPantalla (String pantalla){
        cardLayout.show(panelContenedor, pantalla);
    }

    public void iniciarNivelConPersonaje(Personaje personaje, GestorSprites sprites) {
        nivelPanel.reiniciarEstadoNivel();
        controladorNivel.reiniciarEstado();

        nivelPanel.setPersonaje(personaje);
        nivelPanel.setGestorSprites(sprites);

        List<Enemigo> listaEnemigos = new ArrayList<>();
        listaEnemigos.add(new Enemigo(100, 10, 1, 400, 150));
        listaEnemigos.add(new Enemigo(100, 10, 2, 550, 320));

        mostrarPantalla("nivel");
        SwingUtilities.invokeLater(() -> nivelPanel.requestFocusInWindow());

        // -- CARGA DEL MAPA DE COLISION --
        Modelo.MapaColision mapa = new Modelo.MapaColision();
        mapa.cargar("src/Recursos/Mapas/primer_piso.txt");
        nivelPanel.setMapaColision(mapa);

        // el personaje se encuentra en la entrada de la casa
        personaje.setPosicionX(19 * 32);
        personaje.setPosicionY(19 * 32);
    }


/*  La ventana contenedora principal
    Usa un CardLayout para cambiar entre pantallas 
    (Menú, Selección, Nivel, etc.) */
    
/*Este archivo va a sostener todo nuestro juego
va a intercambiar las pantallas sin tener que abrir ni 
cerrar ventanas nuevas */



}
