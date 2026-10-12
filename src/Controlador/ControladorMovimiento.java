package Controlador;

import Modelo.*;
import Vista.EstadoAnimacion;
import Vista.NivelPanel;
import java.awt.event.KeyEvent;


public class ControladorMovimiento {

    private final Pasos pasos;


    public void procesarMovimientoJugador(Personaje personaje, MapaColision mapa, TipoSuelo suelo,
                                        ControladorTeclado teclado, NivelPanel vista) {
        if (personaje == null) return;

        // leer teclas y traducirlas a un movimiento
        int direccionX = 0;
        int direccionY = 0;

        if (teclado.estaPresionada(KeyEvent.VK_UP) || teclado.estaPresionada(KeyEvent.VK_W)) {
            direccionY -= 1;
        }
        if (teclado.estaPresionada(KeyEvent.VK_DOWN) || teclado.estaPresionada(KeyEvent.VK_S)) {
            direccionY += 1;
        }
        if (teclado.estaPresionada(KeyEvent.VK_LEFT) || teclado.estaPresionada(KeyEvent.VK_A)) {
            direccionX -= 1;
        }
        if (teclado.estaPresionada(KeyEvent.VK_RIGHT) || teclado.estaPresionada(KeyEvent.VK_D)) {
            direccionX += 1;
        }

        boolean quiereCorrer = teclado.estaPresionada(KeyEvent.VK_SHIFT);

        // pedimos al modelo que se mueva, ahi decide velocidad, diagonal y paredes
        personaje.mover(direccionX, direccionY, quiereCorrer, mapa);

        // le avisamos a la Vista qué animación mostrar
        boolean seEstaMoviendo = personaje.estaMoviendose();

        // ACÁ, fuera de cualquier if: corre en todos los ticks
        pasos.actualizar(seEstaMoviendo, suelo);

        if (!vista.estaAtacando() && !vista.estaRecibiendoDanio()) {
            EstadoAnimacion nuevoEstado;

            if (!seEstaMoviendo) {
                nuevoEstado = EstadoAnimacion.IDLE;
            } else if (personaje.estaCorriendo()) {
                nuevoEstado = EstadoAnimacion.CORRIENDO;
            } else {
                nuevoEstado = EstadoAnimacion.CAMINANDO;
            }

            vista.setEstado(nuevoEstado);
        }

        vista.actualizarAnimacion(seEstaMoviendo);
    }

    public ControladorMovimiento(Pasos pasos) {
        this.pasos = pasos;
    }
}