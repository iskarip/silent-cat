package Controlador;

import Modelo.Personaje;
import Vista.Direccion;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;
import java.util.List;

public class ControladorNivel {

    private final NivelPanel vista;
    private final Set<Integer> teclasPresionadas = new HashSet<>();
    private Timer bucleDeJuego;
    private Timer temporizadorAtaque;

    public ControladorNivel(NivelPanel vista) {
        this.vista = vista;
        configurarTeclas();
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
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });
    }

    private void iniciarBucle() {
        bucleDeJuego = new Timer (16, e -> actualizarMovimiento());
        bucleDeJuego.start();
    }

    private void actualizarMovimiento() {
        Personaje personaje = vista.getPersonaje();
        if (personaje == null) return;

        // 1. Movimiento del Jugador
        int deltaX = 0;
        int deltaY = 0;

        if (teclasPresionadas.contains(KeyEvent.VK_UP)) { deltaY -= 4; vista.setDireccion(Direccion.ARRIBA); }
        if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) { deltaY += 4; vista.setDireccion(Direccion.ABAJO); }
        if (teclasPresionadas.contains(KeyEvent.VK_LEFT)) { deltaX -= 4; vista.setDireccion(Direccion.IZQUIERDA); }
        if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)) { deltaX += 4; vista.setDireccion(Direccion.DERECHA); }

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
                }
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

    // Mueve el personaje impidiendo que salga del recuadro
    private void moverConLimites(Personaje personaje, int deltaX, int deltaY) {
        int nuevoX = personaje.getPosicionX() + deltaX;
        int nuevoY = personaje.getPosicionY() + deltaY;

        // Calculamos la hitbox proyectada a donde se quiere mover
        Rectangle hbFutura = vista.getHitbox(nuevoX, nuevoY);

        // 1. Límites contra los bordes de la ventana
        if (hbFutura.x < 0 || (hbFutura.x + hbFutura.width) > vista.getWidth()) {
            nuevoX = personaje.getPosicionX(); // Cancela movimiento horizontal
        }
        if (hbFutura.y < 0 || (hbFutura.y + hbFutura.height) > vista.getHeight()) {
            nuevoY = personaje.getPosicionY(); // Cancela movimiento vertical
        }

        personaje.setPosicionX(nuevoX);
        personaje.setPosicionY(nuevoY);
    }

}
