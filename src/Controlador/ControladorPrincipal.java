package Controlador;

import Modelo.Partida;
import Modelo.Progreso;
import Modelo.Personaje;
import Modelo.ConfiguracionDAO;
import Modelo.ProgresoDAO;
import Vista.ReproductorSonido;
import Vista.GestorSprites;
import Vista.JuegoFrame;
import Vista.MenuPanel;
import Vista.NivelPanel;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;


public class ControladorPrincipal {

    private JuegoFrame ventana;
    private ControladorNivel controladorNivelActual;
    private ControladorProgreso controladorProgreso;
    private final ProgresoDAO progresoDAO;
    public static final String MUSICA_MENU = "Recursos/Sonidos/UI/menu1.wav";

    public ControladorPrincipal(JuegoFrame ventana, ConfiguracionDAO configuracionDAO, ProgresoDAO progresoDAO) {

        // se crea el controlador de progreso antes de configurar los eventos del menu
        this.ventana = ventana;
        this.progresoDAO = progresoDAO;
        // Instancia inicial de Partida inyectada directamente por constructor (sin Singleton)
        this.controladorProgreso = new ControladorProgreso(new Partida(), progresoDAO);

        configurarEventosMenu();
        configurarEventosSeleccion();
        new ControladorAjustes(ventana.getAjustesPanel(), ventana, configuracionDAO); // carga la configuracion guardada

        // muestra u oculta el boton de "continuar" segun si hay datos guardados
        ventana.getMenuPanel().setContinuarVisible(controladorProgreso.hayProgreso());

    }

    private void configurarEventosMenu() {
        MenuPanel menu = ventana.getMenuPanel();
        ReproductorSonido.reproducirEnLoop(MUSICA_MENU);

        menu.getBotonNuevaPartida().addActionListener(e -> {
            ventana.mostrarPantalla("seleccion");
        });

        menu.getBotonContinuar().addActionListener(e -> continuarJuego());

        menu.getBotonAjustes().addActionListener(e -> {
            ventana.mostrarPantalla("ajustes");
        });

        menu.getBotonSalir().addActionListener(e -> {
            System.exit(0);
        });
    }

    private void configurarEventosSeleccion() {
        Vista.SeleccionPersonajePanel seleccion = ventana.getSeleccionPersonajePanel();

        // Iniciar la partida nueva (se le pasa null porq no hay objeto Progreso previo)
        seleccion.getBotonIniciarPartida().addActionListener(e -> {
            comenzarJuego(seleccion.getGeneroSeleccionado(), null);
        });

        // Regresar a la pantalla de menú principal
        seleccion.getBotonVolverMenu().addActionListener(e -> {
            ventana.mostrarPantalla("menu");
        });
    }

    private void continuarJuego() {
     
        MenuPanel menu = ventana.getMenuPanel();
        menu.cargando(true);

     //La lectura de la base va en otro hilo; armar la partida 
     // (modelo y vista) vuelve al hilo de eventos
             new SwingWorker<Progreso, Void>() {
            @Override
            protected Progreso doInBackground() {
                return controladorProgreso.cargar(); // segundo plano
            }

            @Override
            protected void done() { // hilo de eventos
                menu.cargando(false);
                try {
                    Progreso progreso = get();
                    if (progreso == null) return;
                    comenzarJuego(progreso.getGenero(), progreso);
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("No se pudo cargar la partida: " + ex.getMessage());
                }
            }
        }.execute();
    }
   
    // Aca se confirma que el jugador va a jugar. 
    private void comenzarJuego(String genero, Progreso progreso) {

        
        ReproductorSonido.detenerMusica(); 

        // Si ya existía un controlador anterior, paramos su Timer viejo
        if (controladorNivelActual != null) {
            controladorNivelActual.detener();
        }

        // Instanciación  de Partida con operador new 
        Partida partida = new Partida();
        partida.iniciarPartida(3);
        partida.setGenero(genero);
        if (progreso != null) {
            partida.aplicarProgreso(progreso);
        }

        // Se actualiza el ControladorProgreso con la instancia activa de la partida actual
        this.controladorProgreso = new ControladorProgreso(partida, progresoDAO);

        // El personaje ya lo armo la partida
        Personaje personaje = partida.getPersonaje();
        
        // Configuración de GestorSprites
        GestorSprites spritesPersonaje = new GestorSprites("/Recursos/Sprites/Personajes/" + genero + "/");

        // Configuración de NivelPanel
        NivelPanel nivelPanel = ventana.getNivelPanel();
        nivelPanel.reiniciarEstadoNivel();
        nivelPanel.setJugador(new ControladorEntidadVisual(personaje));
        nivelPanel.setGestorSprites(spritesPersonaje);

        // Cambio de pantalla
        ventana.mostrarPantallaConFundido("nivel");

        // Asignar e iniciar el ControladorNivel al atributo global (inyectando la partida)
        controladorNivelActual = new ControladorNivel(partida, nivelPanel, ventana, controladorProgreso);
        controladorNivelActual.iniciar(); 
    }
}