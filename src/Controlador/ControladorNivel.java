package Controlador;

import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.EstadoPersonaje;
import Vista.GestorSprites;
import Vista.AuraVisual;
import Vista.FlashbackGato;
import Vista.BarraBateria;
import Vista.BarraVida;
import Vista.MensajeAmbiental;

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
    private final ControladorPausa controladorPausa;
    private final ControladorAmbiente controladorAmbiente;
    private final ControladorEntidadesPasivas controladorEntidadesPasivas;


    //instancias del gato(el sonido, lentitud y lo visual)
    private final FlashbackGato flashbackGato;
    private final MensajeAmbiental mensajeAmbiental;

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
        this.controladorEntidadesPasivas = new ControladorEntidadesPasivas();

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

        BarraBateria barraBateria = new BarraBateria();
        this.vista.agregarCapaVisual(barraBateria);

        Personaje personajeActual = partida.getPersonaje();
        if (personajeActual != null) {
            BarraVida barraVida = new BarraVida(personajeActual.getPuntosVida());
            this.vista.agregarCapaVisual(barraVida);
            personajeActual.agregarObservador(barraVida);

            // FinDelJuego esta relacionado con las acciones del personaje.
            personajeActual.agregarObservador(this.vista.getFinDelJuego());

            // Botón "Volver al Menú" de FinDelJuego
            this.vista.getFinDelJuego().getBotonVolverMenu().addActionListener(e -> {
                detener(); // Detiene el loop
                this.vista.getFinDelJuego().setVisible(false); // <--- IMPORTANTE: Ocultar el overlay
                this.vista.reiniciarEstadoNivel();             // <--- Reiniciar contadores/estado
                ventanaPrincipal.mostrarPantalla("menu");       // <--- Cambio directo a "menu"
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
        this.bucleDeJuego = new Timer(16, e -> actualizarJuego());
    }

    // -- METODOS --

    public void iniciar() {
        reiniciarEstado();
        sincronizarModeloConVista();
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

        // 4.2 Zonas de proximidad (flashbacks y mensajes): cada zona decide qué hacer
        controladorAmbiente.comprobarZonas(nivelActual, personaje);

        //4.3 IA de entidades pasivas (gato y quizas a futuro otras) cada zona decidiira que hacer
        controladorEntidadesPasivas.actualizar(nivelActual, personaje);

        // 4.4 Descuenta los efectos temporales del personaje (lentitud)
        personaje.actualizarEfectos();

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

}