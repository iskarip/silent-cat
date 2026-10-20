package Controlador;

import Modelo.EspacioBase;
import Modelo.Personaje;
import Vista.EstadoAnimacion;
import Vista.NivelPanel;

import javax.swing.Timer;

public class ControladorCombate {

    private Timer temporizadorAtaque;

    public void ejecutarAtaque(Personaje personaje, EspacioBase espacio, NivelPanel vista) {
        if (personaje == null || espacio == null) return;

        // el Modelo decide si puede atacar (enfriamiento del arma)
        if (!personaje.intentarAtacar()) return;

        vista.setEstado(EstadoAnimacion.ATACANDO);
        iniciarTimerVueltaAIdle(vista);

        espacio.resolverAtaque(personaje);
    }

    private void iniciarTimerVueltaAIdle(NivelPanel vista) {
        if (temporizadorAtaque != null) {
            temporizadorAtaque.stop();
        }
        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoAnimacion.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }
}