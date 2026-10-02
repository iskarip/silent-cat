package Controlador;

import Modelo.Nivel;
import Modelo.Personaje;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;

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

        nivelActual.resolverAtaque(personaje);
    }

    private void iniciarTimerVueltaAIdle(NivelPanel vista) {
        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoPersonaje.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }
}