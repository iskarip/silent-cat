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

        // el Modelo decide si puede atacar (enfriamiento del arma)
        if (!personaje.intentarAtacar()) return;

        vista.setEstado(EstadoPersonaje.ATACANDO);
        iniciarTimerVueltaAIdle(vista);

        nivelActual.resolverAtaque(personaje);
    }

    private void iniciarTimerVueltaAIdle(NivelPanel vista) {
        if (temporizadorAtaque != null) {
            temporizadorAtaque.stop();
        }
        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoPersonaje.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }
}