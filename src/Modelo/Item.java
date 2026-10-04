package Modelo;

import java.awt.Rectangle;

public class Item implements Interactuable, Posicionable {

    private static final int RADIO_RECOGIDA = 6;
    public static final double ESCALA_DEFECTO = 0.5;

    protected String nombre;
    protected boolean recogido;
    protected int posicionX;
    protected int posicionY;
    protected String rutaImagen;
    protected double escala = ESCALA_DEFECTO;
    protected String mensajeRecoger;
    
    // Constructor básico sin posición
    public Item(String nombre, String rutaImagen) {
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
        this.recogido = false;
    }

    // Constructor que posiciona directamente por Tile (columna, fila)
    public Item(String nombre, String rutaImagen, int columna, int fila) {
        this(nombre, rutaImagen);
        colocarEnTile(columna, fila); // Asigna posicionX y posicionY multiplicando por TILE
    }

    // -- GETTERS Y SETTERS --
    public String getNombre() { return nombre; }
    public boolean isRecogido() { return recogido; }
    public void setRecogido(boolean valor) { this.recogido = valor; }

    @Override public int getPosicionX() { return posicionX; }
    @Override public void setPosicionX(int posicionX) { this.posicionX = posicionX; }

    @Override public int getPosicionY() { return posicionY; }
    @Override public void setPosicionY(int posicionY) { this.posicionY = posicionY; }

    public double getEscala() { return escala; }
    public void setEscala(double escala) { this.escala = Math.max(0.1, escala); }

    public String getMensajeRecoger() { return mensajeRecoger; }
    public void setMensajeRecoger(String mensajeRecoger) { this.mensajeRecoger = mensajeRecoger; }

    public String getRutaImagen() { return rutaImagen; }
    
    // -- MÉTODOS --
    public void recoger() {
        this.recogido = true;
    }

    // true si todavía está en el piso y el hitbox recibido lo toca
    public boolean estaAlAlcance(Rectangle hitboxJugador) {
        if (recogido) return false;

        int centroX = posicionX + (MapaColision.TILE / 2);
        int centroY = posicionY + (MapaColision.TILE / 2);

        Rectangle zonaRecogida = new Rectangle(
            centroX - RADIO_RECOGIDA,
            centroY - RADIO_RECOGIDA,
            RADIO_RECOGIDA * 2,
            RADIO_RECOGIDA * 2
        );
        return hitboxJugador.intersects(zonaRecogida);
    }

    @Override
    public void interactuar(Personaje p) {
        recoger();
        p.getInventario().agregarItem(this);
    }
}