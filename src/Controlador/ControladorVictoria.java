package Controlador;

import Modelo.Partida;
import Vista.JuegoFrame;
import Vista.ReproductorSonido;
import javax.swing.Timer;

public class ControladorVictoria {

    private static final int DEMORA_MS = 3000; // el mensaje ambiental dura 4 s, así alcanza a leerse

    private final Partida partida;
    private final JuegoFrame ventana;
    private final ControladorNivel controladorNivel;

    public ControladorVictoria(Partida partida, JuegoFrame ventana, ControladorNivel controladorNivel) {
        this.partida = partida;
        this.ventana = ventana;
        this.controladorNivel = controladorNivel;
        ventana.getVictoriaPanel().setAlContinuar(this::volverAlMenu);
    }

    public void mostrarConDemora() {
        Timer t = new Timer(DEMORA_MS, e -> {
            if (!partida.getPersonaje().estaVivo()) return; // si murió en el medio, manda el game over
            controladorNivel.setPausado(true);
            ventana.mostrarPantallaConFundido("victoria");
        });
        t.setRepeats(false);
        t.start();
    }

    private void volverAlMenu() {
        controladorNivel.detener();
        ventana.mostrarPantallaConFundido("menu");
        ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
    }
}