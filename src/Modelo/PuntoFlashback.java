package Modelo;

import java.util.List;
import java.util.Random;

// Zona que dispara un flashback: sonido, ralentización y aviso a la vista.
public class PuntoFlashback extends ZonaProximidad {

    private static final int TICKS_LENTITUD = 120; // ~2 segundos a 60 ticks por segundo

    private static final List<String> SONIDOS = List.of(
        "Recursos/Sonidos/sonidos_gato/flashback1.wav",
        "Recursos/Sonidos/sonidos_gato/flashback2.wav",
        "Recursos/Sonidos/sonidos_gato/flashback3.wav",
        "Recursos/Sonidos/sonidos_gato/flashback4.wav"
    );

    private final Random random = new Random();

    public PuntoFlashback(int columna, int fila, int radioEnTiles) {
        super(columna, fila, radioEnTiles);
    }

    // Reproduce un sonido al azar, ralentiza al personaje y avisa al observador
    // para que muestre la imagen del flashback.
    @Override
    protected void alActivar(Personaje personaje, ObservadorAmbiente observador) {
        ReproductorSonido.reproducir(SONIDOS.get(random.nextInt(SONIDOS.size())));
        personaje.aplicarLentitud(TICKS_LENTITUD);
        observador.mostrarFlashback();
    }
}