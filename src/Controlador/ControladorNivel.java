package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Linterna;
import Modelo.ObservadorPersonaje;
import Modelo.ObservadorLinterna;
import Vista.ReproductorSonido;
import Modelo.EspacioBase;
import Modelo.Habitacion;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.EstadoAnimacion;
import Vista.InventarioVisual;
import Vista.AuraVisual;
import Vista.FlashbackGato;
import Vista.BarraBateria;
import Vista.BarraVida;
import Vista.BarraEstamina;
import Vista.MensajeAmbiental;

import javax.swing.Timer;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
    private EspacioBase espacioActivo;
    private final ControladorMando controladorMando;

    // Componentes visuales desacoplados (reciben tipos planos / primitivos)
    private final FlashbackGato flashbackGato;
    private final MensajeAmbiental mensajeAmbiental;
    private final BarraBateria barraBateria;
    private final BarraVida barraVida;
    private final BarraEstamina barraEstamina;
    private final AuraVisual linternaOverlay;
    private int tensionActual = 0;
    
    private final InventarioVisual inventarioVisual;

    private Timer bucleDeJuego;
    private boolean personajeMuerto = false;
    private boolean pausado = false;
    private ActionListener alVolverDesdeGameOver;
    private int contadorBateria = 0;

    // Control de tiempo para 60 FPS estables
    private static final double TIEMPO_OPTIMO_NS = 1_000_000_000.0 / 60.0;
    private long tiempoAnterior = 0;
    private double acumulador = 0.0;

    // -- CONSTRUCTOR CONTROLADOR --

    public ControladorNivel(Partida partida, NivelPanel vista, JuegoFrame ventanaPrincipal, ControladorProgreso controladorProgreso) {
        this.partida = partida;
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();

                ControladorMando mando = null;
            try {
                mando = new ControladorMando(controladorTeclado);
            } catch (Throwable t) {
                System.out.println("Soporte de mando desactivado: " + t);
            }
            this.controladorMando = mando;

        this.controladorMovimiento = new ControladorMovimiento();
        this.controladorCombate = new ControladorCombate();
        this.controladorEntidades = new ControladorEntidades();
        this.controladorItems = new ControladorItems();

        this.controladorPausa = new ControladorPausa(vista, ventanaPrincipal, this, controladorProgreso);

        this.flashbackGato = new FlashbackGato();
        this.mensajeAmbiental = new MensajeAmbiental();
        this.controladorAmbiente = new ControladorAmbiente(flashbackGato, mensajeAmbiental, vista);

        this.controladorAcertijo = new ControladorAcertijo(vista, controladorAmbiente);

        // Limpieza y registro polimórfico de capas visuales
        this.vista.limpiarCapasVisuales();

        this.linternaOverlay = new AuraVisual();
        this.vista.agregarCapaVisual(linternaOverlay);
        this.vista.agregarCapaVisual(this.flashbackGato);
        this.vista.agregarCapaVisual(this.mensajeAmbiental);

        this.inventarioVisual = new InventarioVisual();
        this.vista.agregarCapaVisual(inventarioVisual);
        this.controladorTeclado.setAccionInventario(() -> {
            actualizarInventarioVisual();
            inventarioVisual.alternar();
        });

        this.barraBateria = new BarraBateria();
        this.vista.agregarCapaVisual(barraBateria);

        this.barraEstamina = new BarraEstamina();
        this.vista.agregarCapaVisual(barraEstamina);

        Personaje personajeActual = partida.getPersonaje();
        if (personajeActual != null) {
            this.barraVida = new BarraVida(personajeActual.getPuntosVida());
            this.vista.agregarCapaVisual(barraVida);

            // Observer: el Controlador escucha al Modelo y actualiza la Vista mediante primitivos
            personajeActual.agregarObservador(new ObservadorPersonaje() {
                @Override
                public void vidaCambio(int vidaActual, int vidaMaxima) {
                    barraVida.actualizarVida(vidaActual);
                    if (vidaActual > 0) {
                        vista.activarDanioRecibido();
                    }
                }

                @Override
                public void personajeMurio() {
                    barraVida.actualizarVida(0);
                }
            });

            // Sincronizar estado inicial de linterna
            Linterna linterna = personajeActual.getLinterna();
            if (linterna != null) {
                barraBateria.actualizarBateria(linterna.getBateria());

                linterna.agregarObservador(new ObservadorLinterna() {
                    @Override 
                    public void linternaCambio(int bateriaActual, boolean encendida) {
                        linternaOverlay.setEncendida(encendida);
                        linternaOverlay.setBateria(bateriaActual);
                        barraBateria.actualizarBateria(bateriaActual);
                    }
                });
            }

            // Configurar botones de FinDelJuego (con hover desacoplado vía Runnable)
            this.alVolverDesdeGameOver = e -> {
                detener();
                this.vista.getFinDelJuego().setVisible(false);
                this.vista.reiniciarEstadoNivel();
                ventanaPrincipal.mostrarPantalla("menu");
                ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
            };
            this.vista.getFinDelJuego().getBotonVolverMenu().addActionListener(this.alVolverDesdeGameOver);
            this.vista.getFinDelJuego().getBotonVolverMenu().setAccionHover(() ->
                    ReproductorSonido.reproducir("Recursos/Sonidos/UI/sonido3.wav")
            );

            this.vista.getFinDelJuego().getBotonReintentar().addActionListener(e -> reintentarDesdeCheckpoint());
            this.vista.getFinDelJuego().getBotonReintentar().setAccionHover(() ->
                    ReproductorSonido.reproducir("Recursos/Sonidos/UI/sonido3.wav")
            );
        } else {
            this.barraVida = new BarraVida(100);
            this.vista.agregarCapaVisual(barraVida);
        }

        // Asignación de interacción con 'E'

        this.controladorTeclado.setAccionInteraccionar(() -> {
            Personaje personaje = partida.getPersonaje();
            Nivel nivel = partida.getNivelActual();

            if (espacioActivo instanceof Habitacion habitacion) {
                if (habitacion.salidaAlAlcance(personaje)) {
                    salirDeHabitacion(habitacion);
                }
                return;
            }

            Habitacion habitacion = nivel.getHabitacionAlAlcance(personaje);
            if (habitacion != null) {
                entrarAHabitacion(habitacion);
            } else {
                controladorAcertijo.intentarInteraccion(nivel, personaje, controladorTeclado);
            }
        });

        // Asignación de acción de Linterna
        this.controladorTeclado.setAccionLinterna(() -> {
            if (!pausado && !personajeMuerto) {
                Personaje p = partida.getPersonaje();
                p.usarLinterna();
                if (p.getLinterna() != null) {
                    barraBateria.actualizarBateria(p.getLinterna().getBateria());
                }
            }
        });

        this.controladorTeclado.setAccionAtaque(this::atacar);

        this.vista.addKeyListener(controladorTeclado);
        this.bucleDeJuego = new Timer(16, e -> cicloPrincipal());
    }

    // -- SINCRONIZACIÓN DE INVENTARIO DESACOPLADO --

    private void actualizarInventarioVisual() {
        Personaje p = partida.getPersonaje();
        if (p != null && p.getInventario() != null) {
            List<InventarioVisual.SlotVisual> slots = new ArrayList<>();
            for (Modelo.Item it : p.getInventario().getItems()) {
                slots.add(new InventarioVisual.SlotVisual(it.getRutaImagen(), it.getNombre()));
            }
            inventarioVisual.actualizarItems(slots);
        }
    }

    // -- CICLO DE JUEGO --

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
        controladorPausa.desconectar();
        controladorAcertijo.desconectar();
        if (alVolverDesdeGameOver != null) {
            vista.getFinDelJuego().getBotonVolverMenu().removeActionListener(alVolverDesdeGameOver);
        }
    }

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

    public void reintentarDesdeCheckpoint() {
        Personaje personaje = partida.getPersonaje();
        Nivel nivel = partida.getNivelActual();

        if (personaje != null && nivel != null) {
            this.vista.getFinDelJuego().setVisible(false);
            this.vista.reiniciarEstadoNivel();
            this.personajeMuerto = false;
            this.controladorTeclado.limpiarTeclas();

            int xMuerte = personaje.getPosicionX();
            int yMuerte = personaje.getPosicionY();

            java.awt.Rectangle hb = personaje.getHitbox();
            if (nivel.getMapaColision() != null &&
                    !nivel.getMapaColision().esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                yMuerte -= 10;
            }

            personaje.revivirEn(xMuerte, yMuerte);
            barraVida.actualizarVida(100);

            this.vista.requestFocusInWindow();
            this.vista.actualizarCamara();
            this.vista.repaint();

            if (bucleDeJuego != null && !bucleDeJuego.isRunning()) {
                this.tiempoAnterior = System.nanoTime();
                this.acumulador = 0.0;
                bucleDeJuego.start();
            }
        }
    }

    private void sincronizarModeloConVista() {
        Nivel nivelActual = partida.getNivelActual();
        Personaje personaje = partida.getPersonaje();

        if (nivelActual != null) {
            espacioActivo = nivelActual;
            vista.setNivelActual(nivelActual);
        }

        if (personaje != null) {
            vista.setPersonaje(personaje);
            linternaOverlay.setPosicionJugador(personaje.getPosicionX(), personaje.getPosicionY());
            barraVida.actualizarVida(personaje.getPuntosVida());
            if (personaje.getLinterna() != null) {
                barraBateria.actualizarBateria(personaje.getLinterna().getBateria());
            }
            actualizarInventarioVisual();
        }
        if (personaje.getLinterna() != null) {
            barraBateria.actualizarBateria(personaje.getLinterna().getBateria());
            linternaOverlay.setBateria(personaje.getLinterna().getBateria());   
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

        if (transcurrido > 100_000_000.0) {
            transcurrido = 100_000_000.0;
        }

        acumulador += transcurrido;

        int pasos = 0;
        while (acumulador >= TIEMPO_OPTIMO_NS && pasos < 2) {
            actualizarJuego();
            acumulador -= TIEMPO_OPTIMO_NS;
            pasos++;
        }

        if (acumulador > TIEMPO_OPTIMO_NS) {
            acumulador = 0.0;
        }
    // El aura de la linterna necesita saber donde esta el jugador (se lo pasamos como numeros)
        Personaje jugador = partida.getPersonaje();
        if (jugador != null) {
            linternaOverlay.setPosicionJugador(jugador.getPosicionX(), jugador.getPosicionY());
        }

        vista.repaint();
        Toolkit.getDefaultToolkit().sync();
    }

    private void actualizarJuego() {
        if (pausado || controladorAcertijo.estaActivo()) return;

        Personaje personaje = partida.getPersonaje();
        Nivel nivelActual = partida.getNivelActual();

        if (personaje == null || nivelActual == null) return;

        if (!personaje.estaVivo()) {
            if (!personajeMuerto) {
                personajeMuerto = true;
                vista.setEstado(EstadoAnimacion.MURIENDO);
                tensionActual = 0;
                linternaOverlay.setTension(0);
            
            }
            vista.avanzarAnimacionMuerte();
            return;
        }

        if (nivelActual.verificarSiCompleto() && !nivelActual.getNivelSuperado()) {
            cambiarDeNivel();
            return;
        }

        controladorMovimiento.procesarMovimientoJugador(
                personaje,
                espacioActivo.getMapaColision(),
                controladorTeclado,
                vista
        );

        controladorEntidades.actualizar(nivelActual, personaje, vista);
        actualizarTension(personaje); 

        if (controladorMando != null) controladorMando.actualizar();

        if (espacioActivo == nivelActual) {
            controladorAcertijo.comprobarProximidad(nivelActual, personaje);
        }

        controladorItems.actualizar(nivelActual, personaje, controladorAmbiente);
        controladorAmbiente.comprobarZonas(nivelActual, personaje);
        personaje.actualizarEfectos();
        barraEstamina.actualizar(personaje.getNivelEstamina(), personaje.estaAgotado(),
        personaje.getPosicionX(), personaje.getPosicionY());

        // Gasto periódico de batería y sincronización con BarraBateria
        contadorBateria++;
        if (contadorBateria >= 60) {
            Linterna linterna = personaje.getLinterna();
            if (linterna != null) {
                linterna.gastarBateria();
                barraBateria.actualizarBateria(linterna.getBateria());
            }
            contadorBateria = 0;
        }
    }

    private void actualizarTension(Personaje personaje) {
        int nueva = controladorEntidades.calcularTension(espacioActivo, personaje);
        if (nueva != tensionActual) {
        tensionActual = nueva;
        linternaOverlay.setTension(nueva);
        }
    }

    private void atacar() {
        if (!pausado && !personajeMuerto) {
            controladorCombate.ejecutarAtaque(partida.getPersonaje(), partida.getNivelActual(), vista);
        }
    }

    private void entrarAHabitacion(Habitacion habitacion) {
        espacioActivo = habitacion;
        partida.getPersonaje().colocarEnTile(habitacion.getPosicionInicialX(),
                habitacion.getPosicionInicialY());
        vista.setEspacioActual(habitacion);
        controladorTeclado.limpiarTeclas();
    }

    private void salirDeHabitacion(Habitacion habitacion) {
        Nivel nivel = partida.getNivelActual();
        espacioActivo = nivel;
        partida.getPersonaje().colocarEnTile(habitacion.getRetornoX(), habitacion.getRetornoY());
        vista.setEspacioActual(nivel);
        controladorTeclado.limpiarTeclas();
    }

    private void cambiarDeNivel() {
        partida.getNivelActual().setNivelSuperado(true);

        if (!partida.avanzarSiguienteNivel()) {
            return;
        }

        Nivel nivelNuevo = partida.getNivelActual();
        espacioActivo = nivelNuevo;

        partida.getPersonaje().colocarEnTile(nivelNuevo.getPosicionInicialX(),
                nivelNuevo.getPosicionInicialY());
        vista.setNivelActual(nivelNuevo);
        actualizarInventarioVisual();
        controladorTeclado.limpiarTeclas();
    }
}