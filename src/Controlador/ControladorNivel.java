package Controlador;

import Modelo.Personaje;
import Modelo.Partida;
import Modelo.Nivel;
import Modelo.Enemigo;
import Modelo.MapaColision;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.Direccion;
import Vista.EstadoPersonaje;
import Vista.GestorSprites;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;

public class ControladorNivel {

    private final NivelPanel vista;
    private final JuegoFrame ventanaPrincipal;
    private final Set<Integer> teclasPresionadas = new HashSet<>();
    private Timer bucleDeJuego;
    private Timer temporizadorAtaque;
    private boolean personajeMuerto = false;
    private boolean pausado = false;

    public ControladorNivel(NivelPanel vista, JuegoFrame ventanaPrincipal) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;
        configurarTeclas();
        configurarBotonesPausa();
        iniciarBucle();
    }

    private void configurarTeclas() {
        //Clase anonima sobre KeyAdapter: KeyListener tiene tres metodos
        // asi que una lambda no alcanza.

        vista.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    atacar();
                }
            //Temporal: es una tecla de prueba para el flashback del gato
            //la voy a sacar y reemplazar cuando se defina el disparador del flashback del juego
            if (e.getKeyCode() == KeyEvent.VK_F){
                vista.probarFlashbackGato();
            }
            
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });
    }

    // -- UN METODO ABSTRACTO POR BOTON --
    // Se utiliza LAMDBA

    private void configurarBotonesPausa () {
        vista.getBotonPausa().addActionListener (e -> pausarJuego());
        vista.getBotonReanudar().addActionListener ( e-> volverAlMenu());
        vista.getBotonMenuPrincipal().addActionListener(e -> volverAlMenu());
    }

    private void pausarJuego() {
        pausado = true;
        vista.mostrarPausa();
    }

    private void reanudarJuego() {
        pausado = false;
        vista.ocultarPausa();
        vista.requestFocusInWindow(); // recupera el foco para el teclado
    }

    private void volverAlMenu() {
        pausado = false;
        vista.ocultarPausa();
        ventanaPrincipal.mostrarPantalla("menu");
    }

    private void iniciarBucle() {
        bucleDeJuego = new Timer (16, e -> actualizarMovimiento());
        bucleDeJuego.start();
    }

    private void actualizarMovimiento() {

        if (pausado) return; // mientras este pausado, el personaje no se mueve

        Personaje personaje = vista.getPersonaje();
        if (personaje == null) return;

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

        // 1. Movimiento del Jugador
        int deltaX = 0;
        int deltaY = 0;

        int velocidadBase= 4;
        int velocidad = (int) (velocidadBase * personaje.getMultiplicadorVelocidad());
//le puse multiplicar para que se note la lentitud del flashback del gato, si no se nota mucho, podemos poner un multiplicador mas bajo (0.5 o 0.6)

//se aplica el multiplicador de velocidad para que la lentitud del flashback del gato tenga efecto en el pj

        if (teclasPresionadas.contains(KeyEvent.VK_UP)|| teclasPresionadas.contains(KeyEvent.VK_W)) { deltaY -= velocidad; vista.setDireccion(Direccion.ARRIBA); }
        if (teclasPresionadas.contains(KeyEvent.VK_DOWN)|| teclasPresionadas.contains(KeyEvent.VK_S)) { deltaY += velocidad; vista.setDireccion(Direccion.ABAJO); }
        if (teclasPresionadas.contains(KeyEvent.VK_LEFT)|| teclasPresionadas.contains(KeyEvent.VK_A)) { deltaX -= velocidad; vista.setDireccion(Direccion.IZQUIERDA); }
        if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)|| teclasPresionadas.contains(KeyEvent.VK_D)) { deltaX += velocidad; vista.setDireccion(Direccion.DERECHA); }

        boolean seEstaMoviendo = (deltaX != 0 || deltaY != 0);

        if (temporizadorAtaque == null || !temporizadorAtaque.isRunning()) {
            vista.setEstado(seEstaMoviendo ? EstadoPersonaje.CAMINANDO : EstadoPersonaje.IDLE);
        }

        vista.actualizarAnimacion(seEstaMoviendo);

        if (seEstaMoviendo) {
            moverConLimites(personaje, deltaX, deltaY);
        }

        // 2. IA de todos los enemigos en la lista
        Rectangle hbJugador = vista.getHitbox(personaje.getPosicionX(), personaje.getPosicionY());

        for (Modelo.Enemigo e : vista.getEnemigos()) {
            if (e.estaVivo()) {
                e.actualizarComportamiento(personaje, false);

                Rectangle hbEnemigo = vista.getHitboxEnemigo(e);
                if (hbJugador.intersects(hbEnemigo)) {
                    e.atacar(personaje);
                    System.out.println("Vida del personaje: " + personaje.getPuntosVida()); //TEMPORAL
                }
            } else {
                e.avanzarAnimacionMuerte(vista.obtenerTotalFramesMuerteEnemigo());
            }
        }

        vista.repaint();
    }

    private void atacar() {
        vista.setEstado(EstadoPersonaje.ATACANDO);

        Personaje personaje = vista.getPersonaje();
        if (personaje != null) {
            Rectangle golpe = vista.getHitboxAtaque(personaje.getPosicionX(), personaje.getPosicionY());

            // Verifica si el golpe impacta contra alguno de los enemigos vivos
            for (Modelo.Enemigo e : vista.getEnemigos()) {
                if (e.estaVivo()) {
                    Rectangle cuerpoEnemigo = vista.getHitboxEnemigo(e);
                    if (golpe.intersects(cuerpoEnemigo)) {
                        e.recibirDanio(35);
                    }
                }
            }
        }

        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoPersonaje.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }

    // Movimiento del personaje - mapaColision
    private void moverConLimites(Personaje personaje, int deltaX, int deltaY) {
        int nuevoX = personaje.getPosicionX();
        int nuevoY = personaje.getPosicionY();
        Modelo.MapaColision mapa = vista.getMapaColision();

        // Validacion del avance horizontal
        int intentoX = nuevoX + deltaX;
        Rectangle hbX = vista.getHitbox(intentoX, nuevoY);

        if(mapa == null || mapa.esRectanguloValido(hbX.x, hbX.y, hbX.width, hbX.height )) {
            personaje.setPosicionX(intentoX);
        }

        // Validacion del avance vertical
        int intentoY = nuevoY + deltaY;
        Rectangle hbY = vista.getHitbox(personaje.getPosicionX(), intentoY);
        if (mapa == null || mapa.esRectanguloValido(hbY.x, hbY.y, hbY.width, hbY.height)) {
            personaje.setPosicionY(intentoY);
        }

    }

    public void iniciarNivel(Personaje personaje, GestorSprites sprites, NivelPanel vistaPanel) {
        Partida.getInstancia().iniciarPartida(1);
        Nivel nivelActual = Partida.getInstancia().getNivelActual();

        vistaPanel.setPersonaje(personaje);
        vistaPanel.setGestorSprites(sprites);
        vistaPanel.setNivelActual(nivelActual);
        vistaPanel.setMapaColision(nivelActual.getMapaColision());
        personaje.setPosicionX(19 * 32);
        personaje.setPosicionY(19 * 32);
    }

    public void reiniciarEstado() {
        personajeMuerto = false;
    }

}
