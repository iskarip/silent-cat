package Controlador;

import Vista.ReproductorSonido;
import Vista.Sonidos;
import java.util.HashMap;
import java.util.Map;

public class ControladorMusica implements CicloDeVida {

    private final Map<Integer, String> musicaPorNivel = new HashMap<>();

    public ControladorMusica() {
        musicaPorNivel.put(1, Sonidos.MUSICA_NIVEL_1);
        musicaPorNivel.put(2, Sonidos.MUSICA_NIVEL_2);
    }

    public void reproducirParaNivel(int numeroNivel) {
        String ruta = musicaPorNivel.get(numeroNivel);
        if (ruta != null) {
            ReproductorSonido.reproducirEnLoop(ruta);
        } else {
            ReproductorSonido.detenerMusica(); 
        }
    }

    @Override
    public void alPausar() {
        ReproductorSonido.pausarMusica(); 
    }

    @Override
    public void alReanudar() {
        ReproductorSonido.reanudarMusica(); 
    }

    @Override
    public void alDetener() {
        ReproductorSonido.detenerMusica(); 
    }
}