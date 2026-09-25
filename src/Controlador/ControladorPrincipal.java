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
    private ControladorNivel controladorNivelActual;

    public ControladorPrincipal(JuegoFrame ventana) {
        this.ventana = ventana;

        configurarEventosMenu();
        configurarEventosSeleccion();
    }

    private void configurarEventosMenu() {
        MenuPanel menu = ventana.getMenuPanel();

        menu.getBotonNuevaPartida().addActionListener(e -> {
            ventana.mostrarPantalla("seleccion");
        });

        menu.getBotonAjustes().addActionListener(e -> {
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

        // Si ya existía un controlador previo, detenemos su Timer viejo
        if (controladorNivelActual != null) {
            controladorNivelActual.detener();
        }

        // 1. Instanciación de Partida (Singleton)
        Partida partida = Partida.getInstancia();
        partida.iniciarPartida(3);

        // 2. Creación del personaje
        Personaje personaje = new Personaje("Protagonista", new Arma(), new Linterna());
        partida.setPersonaje(personaje);
        
        Nivel nivel = partida.getNivelActual();
        personaje.colocarEnTile(nivel.getPosicionInicialX(), nivel.getPosicionInicialY());
        
        // 3. Configuración de GestorSprites
        GestorSprites spritesPersonaje = new GestorSprites("/Recursos/Sprites/Personajes/" + genero + "/");

        // 4. Configuración de NivelPanel
        NivelPanel nivelPanel = ventana.getNivelPanel();
        nivelPanel.reiniciarEstadoNivel();
        nivelPanel.setPersonaje(personaje);
        nivelPanel.setGestorSprites(spritesPersonaje);

        // 5. Cambio de pantalla
        ventana.mostrarPantallaConFundido("nivel");

        // 6. Asignar e iniciar el ControladorNivel al atributo global
        controladorNivelActual = new ControladorNivel(partida, nivelPanel, ventana);
        controladorNivelActual.iniciar(); 
    }
}

