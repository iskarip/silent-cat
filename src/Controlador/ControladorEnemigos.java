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

        Rectangle hbJugador = vista.getHitbox(personaje.getPosicionX(), personaje.getPosicionY());

        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                // TODO 2: pedile al enemigo que actualice su propio comportamiento
                //         (la decisión de moverse/patrullar/atacar es DE ÉL,
                //         vos solo se lo pedís — e.actualizarComportamiento(...))

                // TODO 3: calculá el hitbox de ESTE enemigo con vista.getHitboxEnemigo(e)
                //         y comparalo contra el hitbox del jugador (el del TODO 1)

                // TODO 4: si hay intersección, quién ataca a quién acá?
                //         pensalo: la colisión la detecta el Controller,
                //         pero el golpe en sí, ¿quién lo ejecuta?
            } else {
                // TODO 5: si está muerto, avanzále la animación de muerte
                //         (esto ya lo tenías en ControladorNivel, movelo tal cual)
            }
        }
    }
} 
    

