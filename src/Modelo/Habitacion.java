package Modelo;

public class Habitacion extends EspacioBase {

    private final String nombreHabitacion;
    private static final int RADIO_PUERTA = 40;
    private final int puertaX, puertaY;

    public Habitacion(String nombreHabitacion, Acertijo acertijo, String rutaGrid, String rutaImagenFondo,
                      int posicionInicialX, int posicionInicialY, int puertaX, int puertaY) {
        super(rutaGrid, rutaImagenFondo, posicionInicialX, posicionInicialY, acertijo);
        this.nombreHabitacion = nombreHabitacion;
        this.puertaX = puertaX;
        this.puertaY = puertaY;
    }

    public String getNombreHabitacion() { return nombreHabitacion; }
    public int getPuertaX() { return puertaX; }
    public int getPuertaY() { return puertaY; }

    // Se calcula, no se guarda: así no puede quedar desincronizada con el acertijo.
    public boolean isDesbloqueada() {
        return getAcertijo() == null || getAcertijo().getResuelto();
    }

    public boolean puertaAlAlcance(Personaje personaje) {
        if (personaje == null || !isDesbloqueada()) return false;

        int centroX = puertaX * MapaColision.TILE + MapaColision.TILE / 2;
        int centroY = puertaY * MapaColision.TILE + MapaColision.TILE / 2;

        return Math.hypot(personaje.getPosicionX() - centroX,
                        personaje.getPosicionY() - centroY) <= RADIO_PUERTA;
    }
}