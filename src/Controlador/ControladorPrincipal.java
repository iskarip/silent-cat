package Controlador;

import Modelo.Arma;
import Modelo.Linterna;
import Modelo.Nivel;
import Modelo.Partida;
import Modelo.Personaje;
import Vista.GestorSprites;
import Vista.JuegoFrame;
import Vista.MenuPanel;
import Vista.NivelPanel;
import javax.swing.JOptionPane;

public class ControladorPrincipal {

    private JuegoFrame ventana;

    public ControladorPrincipal(JuegoFrame ventana) {
        this.ventana = ventana;

        configurarEventosMenu();
        configurarEventosSeleccion();
        // Aquí le asignas los ActionListeners a los botones de MenuPanel, SeleccionPersonajePanel, etc.
    }

    private void configurarEventosMenu() {
        MenuPanel menu = ventana.getMenuPanel();

        menu.getBotonNuevaPartida().addActionListener(e -> {
            ventana.mostrarPantalla("seleccion");
        });

        menu.getBotonAjustes().addActionListener(e -> {
            // Aquí puedes abrir un panel de ajustes o mostrar un mensaje
            JOptionPane.showMessageDialog(ventana, "Ajustes no implementados aún.");
        });

        menu.getBotonSalir().addActionListener(e -> {
            System.exit(0);
        });
    }

    private void configurarEventosSeleccion() {
        Vista.SeleccionPersonajePanel seleccion = ventana.getSeleccionPersonajePanel();

        seleccion.getBotonIniciarPartida().addActionListener(e -> {
            comenzarJuego(seleccion.getGeneroSeleccionado());
        });
    }
    
    private void comenzarJuego(String genero) {

        // 1. Instanciación de Partida (Singleton)
        Partida partida = Partida.getInstancia();
        partida.iniciarPartida(3);

        // 2. Creación del personaje
        Personaje personaje = new Personaje("Protagonista", new Arma(), new Linterna());
        partida.setPersonaje(personaje); // Importante para el Modelo
        
        // Colocar al personaje en la posición inicial del nivel actual
        Nivel nivel = partida.getNivelActual();
        personaje.colocarEnTile(nivel.getPosicionInicialX(), nivel.getPosicionInicialY());
        
        // 3. Configuración de GestorSprites (Vista)
        GestorSprites spritesPersonaje = new GestorSprites("/Recursos/Sprites/Personajes/" + genero + "/");

        // 4. Configuración de NivelPanel (Vista)
        NivelPanel nivelPanel = ventana.getNivelPanel();
        nivelPanel.reiniciarEstadoNivel();
        nivelPanel.setPersonaje(personaje);
        nivelPanel.setGestorSprites(spritesPersonaje);

        // 5. Cambio de pantalla PRIMERO para que el panel sea visible en el CardLayout
        ventana.mostrarPantallaConFundido("nivel");

        // 6. Instanciación e inicio del ControladorNivel SEGUNDO (para ganar el foco)
        ControladorNivel controladorNivel = new ControladorNivel(partida, nivelPanel, ventana);
        controladorNivel.iniciar(); 
    }

}

