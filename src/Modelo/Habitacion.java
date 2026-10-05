package Modelo;

public class Habitacion extends EspacioBase {

    private String nombreHabitacion;
    private Boolean desbloqueada;
    private int puertaX, puertaY;
    private int puntoAccesoX, puntoAccesoY;

    public Habitacion(String nombreHabitacion, Acertijo acertijo, String rutaGrid, String rutaImagenFondo, 
                      int posicionInicialX, int posicionInicialY, int puertaX, int puertaY, 
                      int puntoAccesoX, int puntoAccesoY) {
        super(rutaGrid, rutaImagenFondo, posicionInicialX, posicionInicialY);
        this.nombreHabitacion = nombreHabitacion;
        this.puertaX = puertaX;
        this.puertaY = puertaY;
        this.puntoAccesoX = puntoAccesoX;
        this.puntoAccesoY = puntoAccesoY;
        this.desbloqueada = false;
        this.acertijo = acertijo; // Asigna al atributo heredado de EspacioBase
    }

    public String getNombreHabitacion() { return nombreHabitacion; }
    public Boolean isDesbloqueada() { return desbloqueada; }
    public void setDesbloqueada(Boolean desbloqueada) { this.desbloqueada = desbloqueada; }
    public int getPuertaX() { return puertaX; }
    public int getPuertaY() { return puertaY; }
    public int getPuntoAccesoX() { return puntoAccesoX; }
    public int getPuntoAccesoY() { return puntoAccesoY; }

    // Mantiene compatibilidad con llamadas anteriores
    public Acertijo getAcertijoAcceso() {
        return getAcertijo();
    }
}