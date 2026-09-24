package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Enemigo;
import Vista.NivelPanel;

import java.awt.Rectangle;
import java.util.List;

public class ControladorEnemigos {

    // Se llama desde ControladorNivel.actualizarJuego(), una vez por frame.
    public void actualizar(Nivel nivelActual, Personaje personaje, NivelPanel vista) {
        if (nivelActual == null || personaje == null) return;

        List<Enemigo> enemigos = nivelActual.getListaEnemigos();
        if (enemigos == null) return;

        // TODO 1: calculá el hitbox del jugador UNA sola vez acá afuera
        //         (antes del for) — no tiene sentido recalcularlo por cada enemigo,
        //         es el mismo rectángulo todo el frame. Usá vista.getHitbox(...)

        Rectangle hbJugador = personaje.getHitbox();

        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                boolean linternaEncendida = personaje.getLinterna().getEncendido();
                e.actualizarComportamiento(personaje, linternaEncendida, nivelActual.getMapaColision());

                Rectangle hbEnemigo = e.getHitbox();
                if (hbJugador.intersects(hbEnemigo)) {
                    e.atacar(personaje);
                }
            } else {
                // TODO 5: si está muerto, avanzále la animación de muerte
                //         (esto ya lo tenías en ControladorNivel, movelo tal cual)
            }
        }
    }
} 
    

