package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Gato;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.EstadoPersonaje;
import Vista.GestorSprites;
import Vista.AuraVisual;
import Vista.FlashbackGato;
import Vista.BarraBateria;
import Vista.BarraVida;

import javax.swing.Timer;

public class ControladorNivel {

    // -- ATRIBUTOS --

    private final Partida partida;
    private final NivelPanel vista;
    private final JuegoFrame ventanaPrincipal;

    private final ControladorTeclado controladorTeclado;
    private final ControladorMovimiento controladorMovimiento;
    private final ControladorCombate controladorCombate;
    private final ControladorEnemigos controladorEnemigos;
    private final ControladorAcertijo controladorAcertijo;
    private final ControladorItems controladorItems;

    //instancias del gato(el sonido, lentitud y lo visual)
    private final FlashbackGato flashbackGato;


    private Timer bucleDeJuego;
    private boolean personajeMuerto = false;
    private boolean pausado = false;
    private int contadorBateria = 0; 

    // -- CONSTRUCTOR CONTROLADOR --

    public ControladorNivel(Partida partida, NivelPanel vista, JuegoFrame ventanaPrincipal) {
        this.partida = partida;
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();
        this.controladorMovimiento = new ControladorMovimiento();
        this.controladorCombate = new ControladorCombate();
        this.controladorEnemigos = new ControladorEnemigos();
        this.controladorAcertijo = new ControladorAcertijo(ventanaPrincipal.getAcertijoPanel(), ventanaPrincipal);
        this.controladorItems = new ControladorItems();


        //instanciacion del modelo Gato y vista flashbackGato
    
        this.flashbackGato = new FlashbackGato();

        //Instanciacion de interfaces y registro en la lista de capasVisuales de NivelPanel
        AuraVisual linternaOverlay = new AuraVisual();
        this.vista.agregarCapaVisual(linternaOverlay);
        this.vista.agregarCapaVisual(this.flashbackGato);

        BarraBateria barraBateria = new BarraBateria();
        this.vista.agregarCapaVisual(barraBateria);

        Personaje personajeActual = partida.getPersonaje();
        if (personajeActual != null) {
            BarraVida barraVida = new BarraVida(personajeActual.getPuntosVida());
            this.vista.agregarCapaVisual(barraVida);
            personajeActual.addPropertyChangeListener(barraVida);
        }


        // Conexión de acciones únicas de teclado
        this.controladorTeclado.setAccionAtaque(this::atacar);
        this.controladorTeclado.setAccionDebugFlashback(this::probarFlashback);
        this.controladorTeclado.setAccionLinterna(() -> {
            if (!pausado && !personajeMuerto) {
                partida.getPersonaje().usarLinterna();
            }
        });

        // Registrar el listener de teclado en la vista
        this.vista.addKeyListener(controladorTeclado);

        // Configurar botones de pausa con lambdas
        configurarBotonesPausa();

        // Configurar el Game Loop a 60 FPS 816 milisegundos)
        this.bucleDeJuego = new Timer(16, e -> actualizarJuego());
    }

    // -- METODOS --

    private void configurarBotonesPausa() {
        vista.getBotonPausa().addActionListener(e -> pausarJuego());
        vista.getBotonReanudar().addActionListener(e -> reanudarJuego());
        vista.getBotonMenuPrincipal().addActionListener(e -> volverAlMenu());
        vista.getBotonVolverMenu().addActionListener(e -> volverAlMenu());
    }

    // -- CONTROL DE EJECUCION DEL NIVEL --
    public void iniciar() {
        reiniciarEstado();
        sincronizarModeloConVista();
        bucleDeJuego.start();
    }

        // Pausar o detener el bucle si volvemos al menú
    public void detener () {
        if (bucleDeJuego != null) {
            bucleDeJuego.stop();
        }
    vista.removeKeyListener(controladorTeclado);
    }

    private void pausarJuego () {
        pausado = true;
        vista.mostrarPausa();
    }

    private void reanudarJuego () {
        pausado = false;
        vista.ocultarPausa();
        vista.requestFocusInWindow(); // recupera el foco para el teclado
    }
//debe llamar a detener antes de cambiar de pantalla, para que no siga ejecutando en 2do plano
    private void volverAlMenu () {
        detener();
        pausado = false;
        vista.ocultarPausa();
        ventanaPrincipal.mostrarPantalla("menu");
    }

    // -- SINCRONIZACIÓN Y CICLO PRINCIPAL (MVC) --

    private void sincronizarModeloConVista() {
        Nivel nivelActual = partida.getNivelActual();
        Personaje personaje = partida.getPersonaje();

        if (nivelActual != null) {
        vista.setNivelActual(nivelActual);
        if (nivelActual.getRutaSpritesEnemigos() != null) {
            vista.setSpritesEnemigo(new GestorSprites(nivelActual.getRutaSpritesEnemigos()));
            }
        }

        if (personaje != null) {
            vista.setPersonaje(personaje);
        }
    }

    private void actualizarJuego() {
        if (pausado || controladorAcertijo.estaActivo()) return;

        Personaje personaje = partida.getPersonaje();
        Nivel nivelActual = partida.getNivelActual();

        if (personaje == null || nivelActual == null) return;

        // 1. Estado de muerte del protagonista
        if (!personaje.estaVivo()) {
            if (!personajeMuerto) {
                personajeMuerto = true;
                System.out.println("El personaje murió.");
                vista.setEstado(EstadoPersonaje.MURIENDO);
            }
            vista.avanzarAnimacionMuerte();
            vista.repaint();
            return;
        }

        // 2. Delegación del movimiento al controlador especializado
        controladorMovimiento.procesarMovimientoJugador(
                personaje,
                nivelActual.getMapaColision(),
                controladorTeclado,
                vista
        );

        // 3. IA y actualización de los enemigos
        if (nivelActual.getListaEnemigos() != null) {
            controladorEnemigos.actualizar(nivelActual, personaje, vista);
        }

        // 4. Deteccion de proximidad al acertijo del nivel
        controladorAcertijo.comprobarActivacion(nivelActual, personaje);

        //4.1 Deteccion de proximidad de items del nivel
        controladorItems.actualizar(nivelActual, personaje);

        // 5. Consumo de la bateria de la Linterna
        contadorBateria++;
        
        if(contadorBateria >= 60){
            personaje.getLinterna().gastarBateria();
            contadorBateria= 0;
        }

        // 6. Redibujado en pantalla
        vista.repaint();
    }

    // -- ACCIONES --

    private void atacar() {
        if (!pausado && !personajeMuerto) {
            controladorCombate.ejecutarAtaque(partida.getPersonaje(), partida.getNivelActual(), vista);
        }
    }


//ahora el controlador le preguntaria a PARTIDA.JAVA ya no a nivel, ya que agregue el gato en partida para que aparezca el flashback desde el nivel 1
private void probarFlashback() {
    if (pausado) return;

    Personaje personaje = partida.getPersonaje();
    Gato gato = partida.getGato();

    if (personaje != null && gato != null) {
        // 1. Sonido y ralentización del personaje (Modelo)
        gato.activarEfectoFlashback(personaje);

        // 2. Muestra la imagen del flashback en pantalla (Vista)
        flashbackGato.activar(vista);
    }
}
   
    public void reiniciarEstado() {
        personajeMuerto = false;
        pausado = false;
        controladorTeclado.limpiarTeclas();
        vista.reiniciarEstadoNivel();
    }
}

