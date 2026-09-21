package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Enemigo;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;

import java.awt.Rectangle;
import javax.swing.Timer;

public class ControladorNivel {

    private final Partida partida;
    private final NivelPanel vista;
    
    private final ControladorTeclado controladorTeclado;
    private final ControladorMovimiento controladorMovimiento;
    private final ControladorCombate controladorCombate;
    private final ControladorEnemigos controladorEnemigos;

    private Timer bucleDeJuego;
    private boolean personajeMuerto = false;

    public ControladorNivel(Partida partida, NivelPanel vista) {
        this.partida = partida;
        this.vista = vista;

        // 1. Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();
        this.controladorMovimiento = new ControladorMovimiento();
        this.controladorCombate = new ControladorCombate();
        this.controladorEnemigos = new ControladorEnemigos();

        // 2. Conexión de acciones únicas de teclado
        this.controladorTeclado.setAccionAtaque(this::atacar);
        this.controladorTeclado.setAccionDebugFlashback(this::probarFlashback);

        // 3. Registrar el listener de teclado en la vista
        this.vista.addKeyListener(controladorTeclado);

        // 4. Configuración del Game Loop (60 FPS)
        configurarBucle();
    }

    private void configurarBucle() {
        bucleDeJuego = new Timer(16, e -> actualizarJuego());
    }

    // Método para arrancar el nivel desde ControladorPrincipal

    public void iniciar() {
    reiniciarEstado();
    sincronizarModeloConVista();
    bucleDeJuego.start();
    
    // Garantiza que Swing otorgue el foco de entrada al panel
    javax.swing.SwingUtilities.invokeLater(() -> {
        vista.requestFocusInWindow();
    });
    }

    // Método para pausar o detener el bucle si volvemos al menú
    public void detener() {
        if (bucleDeJuego != null) {
            bucleDeJuego.stop();
        }
    }

    // Carga los datos actuales de Partida hacia NivelPanel
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
        Personaje personaje = partida.getPersonaje();
        Nivel nivelActual = partida.getNivelActual();

        if (personaje == null || nivelActual == null) return;

        // 1. Muerte del personaje
        if (!personaje.estaVivo()) {
            if (!personajeMuerto) {
                personajeMuerto = true;
                System.out.println("El personaje murio.");
                vista.setEstado(EstadoPersonaje.MURIENDO);
            }
            vista.avanzarAnimacionMuerte();
            vista.repaint();
            return;
        }

        // 2. DELEGACIÓN DEL MOVIMIENTO AL CONTROLADOR ESPECÍFICO
        controladorMovimiento.procesarMovimientoJugador(
            personaje, 
            nivelActual.getMapaColision(), 
            controladorTeclado, 
            vista
        );

        // 3. IA de todos los enemigos (lógica original intacta)
        
        if (nivelActual.getListaEnemigos() != null) {
            controladorEnemigos.actualizar(nivelActual, personaje, vista);
        }

        // 4. Redibujar la vista
        vista.repaint();
    }

    private void atacar() {
    controladorCombate.ejecutarAtaque(partida.getPersonaje(), partida.getNivelActual(), vista);
    }

     private void probarFlashback() {
        if (partida.getNivelActual() != null && partida.getNivelActual().getGato() != null) {
            vista.activarFlashbackGato();
        } else {
            vista.activarFlashbackPrueba();
        }
    }

    public void reiniciarEstado() {
        personajeMuerto = false;
        controladorTeclado.limpiarTeclas();
    }
}