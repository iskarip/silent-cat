package Modelo;

import java.awt.Rectangle;

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

    public String getNombreHabitacion() {
        return nombreHabitacion;
    }

    public int getPuertaX() {
        return puertaX;
    }

    public int getPuertaY() {
        return puertaY;
    }

    // Se calcula con el acertijo heredado de EspacioBase
    public boolean isDesbloqueada() {
        return getAcertijo() == null || getAcertijo().getResuelto();
    }

    public boolean puertaAlAlcance(Personaje personaje) {
        if (personaje == null || !isDesbloqueada()) return false;

        int centroX = puertaX * MapaColision.TILE + MapaColision.TILE / 2;
        int centroY = puertaY * MapaColision.TILE + MapaColision.TILE / 2;

        Rectangle hb = personaje.getHitbox();

        return Math.hypot(hb.getCenterX() - centroX,
                hb.getCenterY() - centroY) <= RADIO_PUERTA;
    }

    // Tile del nivel donde reaparece el personaje al salir
    public int getRetornoX() { return puertaX; }
    public int getRetornoY() { return puertaY + 1; }
    
}
