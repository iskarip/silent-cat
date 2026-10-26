package Modelo;

// Zona que dispara un flashback
public class PuntoFlashback extends ZonaProximidad {

    private static final int TICKS_LENTITUD = 150;

    // lo que piensa el personaje cuando termina el flashback (null = no dice nada)
    private final String mensajeAlTerminar;

    public PuntoFlashback(int columna, int fila, int radioEnTiles, String mensajeAlTerminar) {
        super(columna, fila, radioEnTiles);
        this.mensajeAlTerminar= mensajeAlTerminar;
    }   
    public PuntoFlashback(int columna, int fila, int radioEnTiles) {
        this(columna, fila, radioEnTiles, null);
    }

    public String getMensajeAlTerminar() { return mensajeAlTerminar; }

    @Override
    protected void alActivar(Personaje personaje, ObservadorAmbiente observador) {
        personaje.aplicarLentitud(TICKS_LENTITUD);
        observador.mostrarFlashback(mensajeAlTerminar);
    }
}