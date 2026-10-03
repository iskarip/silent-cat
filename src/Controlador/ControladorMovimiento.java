package Controlador;

import Modelo.MapaColision;
import Modelo.Personaje;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;
import java.awt.event.KeyEvent;

public class ControladorMovimiento {

    public void procesarMovimientoJugador(Personaje personaje, MapaColision mapa, ControladorTeclado teclado, NivelPanel vista) {
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

        // pedimos al modelo que se mueva, ahi decide velocidad, diagonal y paredes
        personaje.mover(direccionX, direccionY, mapa);

        // le avisamos a la Vista qué animación mostrar
        boolean seEstaMoviendo = personaje.estaMoviendose();

        if (!vista.estaAtacando() && !vista.estaRecibiendoDanio()) {
            if (seEstaMoviendo) {
                vista.setEstado(EstadoPersonaje.CAMINANDO);
            } else {
                vista.setEstado(EstadoPersonaje.IDLE);
            }
        }

        vista.actualizarAnimacion(seEstaMoviendo);
    }
}