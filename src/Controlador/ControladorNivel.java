package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.EstadoPersonaje;

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

    private Timer bucleDeJuego;
    private boolean personajeMuerto = false;
    private boolean pausado = false;

    // -- CONTROLADOR --

    public ControladorNivel(Partida partida, NivelPanel vista, JuegoFrame ventanaPrincipal) {
        this.partida = partida;
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();
        this.controladorMovimiento = new ControladorMovimiento();
        this.controladorCombate = new ControladorCombate();
        this.controladorEnemigos = new ControladorEnemigos();

        // Conexión de acciones únicas de teclado
        this.controladorTeclado.setAccionAtaque(this::atacar);
        this.controladorTeclado.setAccionDebugFlashback(this::probarFlashback);

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
            vista.setMapaColision(nivelActual.getMapaColision());
        }

        if (personaje != null) {
            vista.setPersonaje(personaje);
        }
    }

    private void actualizarJuego() {
        if (pausado) return;

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

        // 4. Redibujado en pantalla
        vista.repaint();
    }

    // -- ACCIONES --

    private void atacar() {
        if (!pausado && !personajeMuerto) {
            controladorCombate.ejecutarAtaque(partida.getPersonaje(), partida.getNivelActual(), vista);
        }
    }

    private void probarFlashback() {
        if (pausado) return;
        if (partida.getNivelActual() != null && partida.getNivelActual().getGato() != null) {
            vista.activarFlashbackGato();
        }
    }

    public void reiniciarEstado() {
        personajeMuerto = false;
        pausado = false;
        controladorTeclado.limpiarTeclas();
        vista.reiniciarEstadoNivel();
    }
}

