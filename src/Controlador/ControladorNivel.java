package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Linterna;
import Modelo.ObservadorPersonaje;
import Modelo.ReproductorSonido;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.EstadoPersonaje;
import Vista.GestorSprites;
import Vista.InventarioVisual;
import Vista.AuraVisual;
import Vista.FlashbackGato;
import Vista.BarraBateria;
import Vista.BarraVida;
import Vista.MensajeAmbiental;
import Modelo.Entidad;

import javax.swing.Timer;
import java.awt.Toolkit;

public class ControladorNivel {

    // -- ATRIBUTOS --

    private final Partida partida;
    private final NivelPanel vista;
    private final JuegoFrame ventanaPrincipal;

    private final ControladorTeclado controladorTeclado;
    private final ControladorMovimiento controladorMovimiento;
    private final ControladorCombate controladorCombate;
    private final ControladorEntidades controladorEntidades;
    private final ControladorAcertijo controladorAcertijo;
    private final ControladorItems controladorItems;
    private final ControladorPausa controladorPausa;
    private final ControladorAmbiente controladorAmbiente;

    //instancias del gato(el sonido, lentitud y lo visual)
    private final FlashbackGato flashbackGato;
    private final MensajeAmbiental mensajeAmbiental;

    private Timer bucleDeJuego;
    private boolean personajeMuerto = false;
    private boolean pausado = false;
    private int contadorBateria = 0;

    // Control de tiempo para garantizar fluidez identica en todas las computadoras (60 ticks por segundo)
    private static final double TIEMPO_OPTIMO_NS = 1_000_000_000.0 / 60.0;
    private long tiempoAnterior = 0;
    private double acumulador = 0.0;

    // -- CONSTRUCTOR CONTROLADOR --

    public ControladorNivel(Partida partida, NivelPanel vista, JuegoFrame ventanaPrincipal) {
        this.partida = partida;
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();
        this.controladorMovimiento = new ControladorMovimiento();
        this.controladorCombate = new ControladorCombate();
        this.controladorEntidades = new ControladorEntidades();
        this.controladorAcertijo = new ControladorAcertijo(ventanaPrincipal.getAcertijoPanel(), ventanaPrincipal);
        this.controladorItems = new ControladorItems();

        this.controladorPausa = new ControladorPausa(vista, ventanaPrincipal, this);

        //instanciacion del modelo Gato y vista flashbackGato

        this.flashbackGato = new FlashbackGato();
        this.mensajeAmbiental = new MensajeAmbiental();
        this.controladorAmbiente = new ControladorAmbiente(flashbackGato, mensajeAmbiental, vista);

        this.vista.limpiarCapasVisuales();

        //Instanciacion de interfaces y registro en la lista de capasVisuales de NivelPanel
        AuraVisual linternaOverlay = new AuraVisual();
        this.vista.agregarCapaVisual(linternaOverlay);
        this.vista.agregarCapaVisual(this.flashbackGato);
        this.vista.agregarCapaVisual(this.mensajeAmbiental);

        InventarioVisual inventarioVisual = new InventarioVisual();
        this.vista.agregarCapaVisual(inventarioVisual);
        this.controladorTeclado.setAccionInventario(inventarioVisual::alternar);

        BarraBateria barraBateria = new BarraBateria();
        this.vista.agregarCapaVisual(barraBateria);

        Personaje personajeActual = partida.getPersonaje();
        if (personajeActual != null) {
            BarraVida barraVida = new BarraVida(personajeActual.getPuntosVida());
            this.vista.agregarCapaVisual(barraVida);
            personajeActual.agregarObservador(barraVida);

            if (personajeActual.getLinterna() != null) {
                Linterna linterna = personajeActual.getLinterna();
                linterna.agregarObservador(barraBateria);
                linterna.agregarObservador(linternaOverlay);
            }

            // Observador para activar el sprite de recibir danio al perder vida
            personajeActual.agregarObservador(new ObservadorPersonaje() {
                @Override
                public void vidaCambio(int vidaActual, int vidaMaxima) {
                    if (vidaActual > 0) {
                        vista.activarDanioRecibido();
                    }
                }

                @Override
                public void personajeMurio() {
                    // El ciclo de muerte ya lo maneja NivelPanel y FinDelJuego
                }
            });

            // Botón "Volver al Menú" de FinDelJuego
            this.vista.getFinDelJuego().getBotonVolverMenu().addActionListener(e -> {
                detener(); // Detiene el loop
                this.vista.getFinDelJuego().setVisible(false); // <--- IMPORTANTE: Ocultar el overlay
                this.vista.reiniciarEstadoNivel();             // <--- Reiniciar contadores/estado
                ventanaPrincipal.mostrarPantalla("menu");       // <--- Cambio directo a "menu"
                ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
            });

            // Botón "Reintentar / Reanudar Partida" de FinDelJuego (Revivir en Checkpoint)
            this.vista.getFinDelJuego().getBotonReintentar().addActionListener(e -> {
                reintentarDesdeCheckpoint();
            });
        }


        // Conexión de acciones únicas de teclado
        this.controladorTeclado.setAccionAtaque(this::atacar);
        this.controladorTeclado.setAccionLinterna(() -> {
            if (!pausado && !personajeMuerto) {
                partida.getPersonaje().usarLinterna();
            }
        });

        // Registrar el listener de teclado en la vista
        this.vista.addKeyListener(controladorTeclado);

        // Configurar el Game Loop a 60 FPS 816 milisegundos)
        this.bucleDeJuego = new Timer(16, e -> cicloPrincipal());
    }

    // -- METODOS --

    public void iniciar() {
        reiniciarEstado();
        sincronizarModeloConVista();
        this.tiempoAnterior = System.nanoTime();
        this.acumulador = 0.0;
        bucleDeJuego.start();
    }

    public void detener() {
        if (bucleDeJuego != null) {
            bucleDeJuego.stop();
        }
        vista.removeKeyListener(controladorTeclado);
    }

    // --- CONTROL DE PAUSA DESDE EL CONTROLADOR DEDICADO ---
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
        if (bucleDeJuego != null) {
            if (pausado) {
                bucleDeJuego.stop();
            } else {
                this.tiempoAnterior = System.nanoTime();
                this.acumulador = 0.0;
                bucleDeJuego.start();
            }
        }
    }

    public boolean isPausado() {
        return pausado;
    }

    public void reiniciarEstado() {
        personajeMuerto = false;
        pausado = false;
        controladorTeclado.limpiarTeclas();
        vista.reiniciarEstadoNivel();
    }

    // -- LOGICA DE CHECKPOINT Y REINTENTO --
    public void reintentarDesdeCheckpoint() {
        Personaje personaje = partida.getPersonaje();
        Nivel nivel = partida.getNivelActual();

        if (personaje != null && nivel != null) {
            // 1. Ocultar la pantalla de fin de juego y limpiar estado de muerte
            this.vista.getFinDelJuego().setVisible(false);
            this.vista.reiniciarEstadoNivel();
            this.personajeMuerto = false;
            this.controladorTeclado.limpiarTeclas();

            // 2. Obtener la posición donde murió
            int xMuerte = personaje.getPosicionX();
            int yMuerte = personaje.getPosicionY();

            // Verificamos si la hitbox en ese punto quedó incrustada en una pared/bloque
            java.awt.Rectangle hb = personaje.getHitbox();
            if (nivel.getMapaColision() != null &&
                    !nivel.getMapaColision().esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                // Si pisaba una pared, lo subimos 10 px al área transitable
                yMuerte -= 10;
            }

            // 3. Revivir al personaje en esa misma ubicación con vida completa
            personaje.revivirEn(xMuerte, yMuerte);

            // 4. Devolver de inmediato el foco del teclado al panel
            this.vista.requestFocusInWindow();

            // 5. Centrar cámara y redibujar
            this.vista.actualizarCamara();
            this.vista.repaint();

            // 6. Asegurar que el bucle de juego continúe
            if (bucleDeJuego != null && !bucleDeJuego.isRunning()) {
                this.tiempoAnterior = System.nanoTime();
                this.acumulador = 0.0;
                bucleDeJuego.start();
            }
        }
    }

    // -- SINCRONIZACIÓN Y CICLO PRINCIPAL (MVC) --

    private void sincronizarModeloConVista() {
        Nivel nivelActual = partida.getNivelActual();
        Personaje personaje = partida.getPersonaje();

        if (nivelActual != null) {
            vista.setNivelActual(nivelActual);
        }

        if (personaje != null) {
            vista.setPersonaje(personaje);
        }
    }

    private void cicloPrincipal() {
        if (pausado || controladorAcertijo.estaActivo()) {
            tiempoAnterior = System.nanoTime();
            return;
        }

        long tiempoActual = System.nanoTime();
        double transcurrido = (double) (tiempoActual - tiempoAnterior);
        tiempoAnterior = tiempoActual;

        // Evita saltos bruscos si la ventana se minimiza o se bloquea el hilo
        if (transcurrido > 100_000_000.0) {
            transcurrido = 100_000_000.0;
        }

        acumulador += transcurrido;

        // Ejecuta los pasos acumulados (limitando a un máximo de 2 por cuadro para que no acelere de golpe)
        int pasos = 0;
        while (acumulador >= TIEMPO_OPTIMO_NS && pasos < 2) {
            actualizarJuego();
            acumulador -= TIEMPO_OPTIMO_NS;
            pasos++;
        }

        // Si sobró tiempo de más acumulado, se descarta para no arrastrar velocidad extra
        if (acumulador > TIEMPO_OPTIMO_NS) {
            acumulador = 0.0;
        }

        // 6. Redibujado en pantalla
        vista.repaint();
        Toolkit.getDefaultToolkit().sync();
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
                vista.setEstado(EstadoPersonaje.MURIENDO);
            }
            vista.avanzarAnimacionMuerte();
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
        controladorEntidades.actualizar(nivelActual, personaje, vista);

        // 4. Deteccion de proximidad al acertijo del nivel
        controladorAcertijo.comprobarActivacion(nivelActual, personaje);

        //4.1 Deteccion de proximidad de items del nivel
        controladorItems.actualizar(nivelActual, personaje);

        // 4.2 Zonas de proximidad (flashbacks y mensajes): cada zona decide qué hacer
        controladorAmbiente.comprobarZonas(nivelActual, personaje);

        // 4.4 Descuenta los efectos temporales del personaje (lentitud)
        personaje.actualizarEfectos();

        // 5. Consumo de la bateria de la Linterna
        contadorBateria++;
        if(contadorBateria >= 60){
            personaje.getLinterna().gastarBateria(); // Linterna llama a notificar() y la vista se actualiza sola
            contadorBateria = 0;
        }
    }

    // -- ACCIONES --

    private void atacar() {
        if (!pausado && !personajeMuerto) {
            controladorCombate.ejecutarAtaque(partida.getPersonaje(), partida.getNivelActual(), vista);
        }
    }

}