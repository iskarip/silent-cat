package Controlador;

import Modelo.*;
import Vista.*;

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
    private final ControladorMando controladorMando;
    private final ControladorEspacio controladorEspacio;
    private final Pasos pasos;
   
    // Componentes visuales desacoplados (reciben tipos planos / primitivos)
    private final FlashbackGato flashbackGato;
    private final MensajeAmbiental mensajeAmbiental;
    private final BarraBateria barraBateria;
    private final BarraVida barraVida;
    private final BarraEstamina barraEstamina;
    private final AuraVisual linternaOverlay;
    private final PolvoVisual polvoVisual;
    private int tensionActual = 0;
    private final ControladorMusica controladorMusica;
    private final InventarioVisual inventarioVisual;
    private final AvisoInteraccion avisoInteraccion;
    private final ControladorGameOver controladorGameOver;

    private final PanelPista pistaVisual;
    private final ControladorPista controladorPista;

    private boolean personajeMuerto = false;
    private boolean pausado = false;
    private int contadorBateria = 0;

    private final BucleDeJuego bucleDeJuego;
    private final List<CicloDeVida> componentes = new ArrayList<>();

    // -- CONSTRUCTOR CONTROLADOR --

    public ControladorNivel(Partida partida, NivelPanel vista, JuegoFrame ventanaPrincipal, ControladorProgreso controladorProgreso) {
        this.partida = partida;
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;
        this.controladorEspacio = new ControladorEspacio(partida, vista);

        // Instanciación de controladores específicos
        this.controladorTeclado = new ControladorTeclado();
        componentes.add(this.controladorTeclado);

                ControladorMando mando = null;
            try {
                mando = new ControladorMando(controladorTeclado);
            } catch (Throwable t) {
                System.out.println("Soporte de mando desactivado: " + t);
            }
            this.controladorMando = mando;

            
        this.controladorMusica = new ControladorMusica();
        componentes.add(this.controladorMusica);

        this.pasos = new Pasos();
        ControladorPasos controladorPasos = new ControladorPasos(pasos);
        this.pasos.agregarObservador(controladorPasos);
        componentes.add(controladorPasos);

        this.controladorGameOver = new ControladorGameOver(vista, ventanaPrincipal, this);
        componentes.add(this.controladorGameOver);

        this.controladorMovimiento = new ControladorMovimiento(pasos);

        this.controladorCombate = new ControladorCombate();
        this.controladorEntidades = new ControladorEntidades();
        this.controladorItems = new ControladorItems();
        
        this.controladorPausa = new ControladorPausa(vista, ventanaPrincipal, this, controladorProgreso);
        this.polvoVisual = new PolvoVisual();
        this.flashbackGato = new FlashbackGato();
        this.mensajeAmbiental = new MensajeAmbiental();
        this.avisoInteraccion = new AvisoInteraccion();
        this.controladorAmbiente = new ControladorAmbiente(flashbackGato, mensajeAmbiental, this.avisoInteraccion, vista);

        this.controladorAcertijo = new ControladorAcertijo(vista, controladorAmbiente);

        // Limpieza y registro polimórfico de capas visuales
        this.vista.limpiarCapasVisuales();
        this.linternaOverlay = new AuraVisual();
        this.vista.agregarCapaVisual(polvoVisual);
        this.vista.agregarCapaVisual(linternaOverlay);
        this.vista.agregarCapaVisual(this.flashbackGato);
        this.vista.agregarCapaVisual(this.mensajeAmbiental);
        this.vista.agregarCapaVisual(this.avisoInteraccion);
        
        this.inventarioVisual = new InventarioVisual();
        this.vista.agregarCapaVisual(inventarioVisual);
        
        this.controladorTeclado.setAccionInventario(() -> {
            actualizarInventarioVisual();
            inventarioVisual.alternar();
        });
        
        this.pistaVisual = new PanelPista();
        this.vista.agregarCapaVisual(pistaVisual);
        this.controladorPista = new ControladorPista(pistaVisual);

        ControladorInteraccion controladorInteraccion = new ControladorInteraccion(
        controladorPista, controladorEspacio, controladorAcertijo, controladorTeclado, partida
        );

        // Asignación de interacción con 'E' delegada al nuevo controlador
        this.controladorTeclado.setAccionInteraccionar(() -> {
            controladorInteraccion.manejarInteraccion();
        });

// Inicialización del Bucle de Juego delegando actualización y renderizado
        this.bucleDeJuego = new BucleDeJuego(
            this::actualizarJuego,
            () -> {
                Personaje jugador = partida.getPersonaje();
                if (jugador != null) {
                    linternaOverlay.setPosicionJugador(jugador.getPosicionX(), jugador.getPosicionY());
                }
                vista.repaint();
            },
            () -> (pausado || controladorAcertijo.estaActivo() || controladorPista.estaActivo())
        );
        componentes.add(this.bucleDeJuego);



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

        } else {
            this.barraVida = new BarraVida(100);
            this.vista.agregarCapaVisual(barraVida);
        }

        this.controladorTeclado.setAccionSlot(indice -> {
                if (!pausado && !personajeMuerto && !controladorAcertijo.estaActivo()
                        && inventarioVisual.isVisible()) {
                    controladorPista.leerSlot(partida.getPersonaje(), indice);
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

        this.controladorTeclado.setAccionPausa(controladorPausa::alternarPausa);
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
        Nivel nivelInicial = partida.getNivelActual();
        if (nivelInicial != null) {
            controladorMusica.reproducirParaNivel(nivelInicial.getNumeroNivel());
        }

        bucleDeJuego.iniciar();
    }

    public void detener() {
        detenerComponentes();
        vista.removeKeyListener(controladorTeclado);
        controladorPausa.desconectar();
        controladorAcertijo.desconectar();
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
            if (pausado) {
                pausarComponentes(); 
            } else {
                reanudarComponentes(); 
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

            reanudarComponentes();

            this.vista.requestFocusInWindow();
            this.vista.actualizarCamara();
            this.vista.repaint();

                bucleDeJuego.iniciar();
        }
    }

    private void sincronizarModeloConVista() {
        Personaje personaje = partida.getPersonaje();

        controladorEspacio.sincronizarVista();

        if (personaje != null) {
            vista.setPersonaje(personaje);
            linternaOverlay.setPosicionJugador(personaje.getPosicionX(), personaje.getPosicionY());
            barraVida.actualizarVida(personaje.getPuntosVida());
            if (personaje.getLinterna() != null) {
                barraBateria.actualizarBateria(personaje.getLinterna().getBateria());
            }
            actualizarInventarioVisual();
        }
    }

    private void actualizarJuego() {
        if (pausado || controladorAcertijo.estaActivo() || controladorPista.estaActivo()) return;

        Personaje personaje = partida.getPersonaje();
        Nivel nivelActual = partida.getNivelActual();

        if (personaje == null || nivelActual == null) return;

        if (!personaje.estaVivo()) {
            if (!personajeMuerto) {
                personajeMuerto = true;
                pausarComponentes();
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
                controladorEspacio.getEspacioActivo().getMapaColision(),
                controladorEspacio.getEspacioActivo().getTipoSuelo(),
                controladorTeclado,
                vista
        );

        controladorEntidades.actualizar(controladorEspacio.getEspacioActivo(), personaje, vista);
        actualizarTension(personaje); 

        if (controladorMando != null) controladorMando.actualizar();

        actualizarAvisoInteraccion();

        controladorItems.actualizar(controladorEspacio.getEspacioActivo(), personaje, controladorAmbiente);
        controladorAmbiente.comprobarZonas(controladorEspacio.getEspacioActivo(), personaje);
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
        int nueva = controladorEntidades.calcularTension(controladorEspacio.getEspacioActivo(), personaje);
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
        controladorAmbiente.ocultarAvisoInteraccion();
        controladorEspacio.entrar(habitacion);
        controladorTeclado.limpiarTeclas();
    }

    private void salirDeHabitacion() {
        controladorAmbiente.ocultarAvisoInteraccion();
        controladorEspacio.salir();
        controladorTeclado.limpiarTeclas();
    }

    private void cambiarDeNivel() {
        if (!controladorEspacio.cambiarDeNivel()) return;
        actualizarInventarioVisual();
        controladorTeclado.limpiarTeclas();
        controladorMusica.reproducirParaNivel(partida.getNivelActual().getNumeroNivel());
    }

    private void actualizarAvisoInteraccion() {
    if (controladorEspacio.hayInteraccionAlAlcance()) {
        controladorAmbiente.mostrarAvisoInteraccion("[E] Interactuar");
    } else {
        controladorAmbiente.ocultarAvisoInteraccion();
    }
}

    private void pausarComponentes() {
        for (CicloDeVida c : componentes) c.alPausar();
    }

    private void reanudarComponentes() {
        for (CicloDeVida c : componentes) c.alReanudar();
    }

    private void detenerComponentes() {
        for (CicloDeVida c : componentes) c.alDetener();
    }
}