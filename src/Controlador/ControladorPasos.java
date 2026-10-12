package Controlador;

import Modelo.*;
import Vista.ReproductorSonido;
import Vista.Sonidos;
import java.util.EnumMap;
import java.util.Map;

// Escucha al modelo (Pasos) y reproduce el sonido que corresponde al piso.
// Es el único que sabe qué sonido va con qué material.
public class ControladorPasos implements ObservadorPasos,CicloDeVida {

    private final Pasos pasos;
    private final Map<TipoSuelo, String> sonidosPorSuelo = new EnumMap<>(TipoSuelo.class);

    public ControladorPasos(Pasos pasos) {
        this.pasos = pasos;
        sonidosPorSuelo.put(TipoSuelo.MADERA, Sonidos.PASOS_MADERA);
        sonidosPorSuelo.put(TipoSuelo.METAL, Sonidos.PASOS_METAL);
    }

    @Override
    public void pasosIniciados(TipoSuelo suelo) {
        String ruta = sonidosPorSuelo.get(suelo);
        if (ruta != null) {
            ReproductorSonido.reproducirEfectoEnLoop(ruta);
        }
    }

    @Override
    public void pasosDetenidos() {
        ReproductorSonido.detenerEfectoEnLoop();
    }

    @Override
    public void alPausar() { pasos.detener(); }

    @Override
    public void alDetener() { pasos.detener(); }
}