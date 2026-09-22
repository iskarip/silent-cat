package Modelo;

public class Habitacion extends EspacioBase{

    //-- ATRIBUTOS --
    private String nombreHabitacion;
    private Boolean desbloqueada;
    private Acertijo acertijoAcceso; // cada habitacion puede tener un acertijo asociado, que se resuelve para desbloquear la habitacion
    private int puertaX, puertaY;    // dónde está la puerta EN EL NIVEL (afuera)
    private int puntoAccesoX, puntoAccesoY; // dónde aparece el personaje DENTRO del cuarto (sirve para entrar Y salir)

    //-- CONSTRUCTOR --
    public Habitacion(String nombreHabitacion,  Acertijo acertijo, String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY, int puertaX, int puertaY, int puntoAccesoX, int puntoAccesoY) {
        super(rutaGrid , rutaImagenFondo, posicionInicialX, posicionInicialY);
        this.nombreHabitacion = nombreHabitacion;
        this.puertaX = puertaX;
        this.puertaY = puertaY;
        this.acertijoAcceso = acertijo;
        this.puntoAccesoX = puntoAccesoX;
        this.puntoAccesoY = puntoAccesoY;
        this.desbloqueada = false;
    }

    //-- GET y SET --

    public String getNombreHabitacion() {
        return nombreHabitacion;
    }

    public Boolean isDesbloqueada() {
        return desbloqueada;
    }

    public void setDesbloqueada(Boolean desbloqueada) {
        this.desbloqueada = desbloqueada;
    }

    public int getPuertaX() {
        return puertaX;
    }

    public int getPuertaY() {
        return puertaY;
    }

    public Acertijo getAcertijoAcceso() {
        return acertijoAcceso;
    }

    public int getPuntoAccesoX() {
        return puntoAccesoX;
    }

    public int getPuntoAccesoY() {
        return puntoAccesoY;
    }

}
