package Modelo;

// Zona que dispara un flashback
public class PuntoFlashback extends ZonaProximidad {

    private static final int TICKS_LENTITUD = 120;

    public PuntoFlashback(int columna, int fila, int radioEnTiles) {
        super(columna, fila, radioEnTiles);
    }

    @Override
    protected void alActivar(Personaje personaje, ObservadorAmbiente observador) {
        personaje.aplicarLentitud(TICKS_LENTITUD);
        observador.mostrarFlashback();
    }
}