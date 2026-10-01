package Controlador;

import Modelo.Entidad;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;

import java.awt.Rectangle;
import javax.swing.Timer;

public class ControladorCombate {

    private Timer temporizadorAtaque;

    public void ejecutarAtaque(Personaje personaje, Nivel nivelActual, NivelPanel vista) {
        if (personaje == null || nivelActual == null) return;

        if (temporizadorAtaque != null && temporizadorAtaque.isRunning()) {
            return;
        }

        vista.setEstado(EstadoPersonaje.ATACANDO);
        iniciarTimerVueltaAIdle(vista); // primero el timer, así nunca queda trabado

        Rectangle hitboxGolpe = personaje.getHitboxAtaque();

        if (nivelActual.getListaEntidades() != null) {
            for (Entidad e : nivelActual.getListaEntidades()) {
                if (e.estaVivo() && hitboxGolpe.intersects(e.getHitbox())) {
                    personaje.atacar(e);
                }
            }
        }
    }

    private void iniciarTimerVueltaAIdle(NivelPanel vista) {
        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoPersonaje.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }
}