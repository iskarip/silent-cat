package Modelo;

import java.awt.Rectangle;

public abstract class Entidad implements Posicionable { // Entidad es abstracta porque no se puede instanciar directamente, solo a través de sus subclases

    // -- ATRIBUTOS --

    public static final double ESCALA = 2.5;
    private int posicionX;
    private int posicionY;
    protected String rutaSprites;
    
    protected Direccion direccion = Direccion.ABAJO;

    // -- CONSTRUCTOR --

    // -- SET's y GET's --

    public String getRutaSprites (){
        return rutaSprites;
    }

    public void setRutaSprites (String ruta) { 
        this.rutaSprites = ruta;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public Direccion getDireccion () {
        return direccion;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    // -- COMPORTAMIENTO POR DEFECTO (las subclases lo sobrescriben) --

    public boolean estaVivo() { return true; }
    public boolean estaMoviendose() { return false; }
    public boolean mostrarBarraVida() { return false; }
    public int getPorcentajeVida() { return 100; }
    public void recibirDanio(int cantidad) { }                        // una entidad pasiva no recibe daño
    public void actualizar(Personaje jugador, MapaColision mapa) { }  // por defecto no hace nada


    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    // -- METODOS --

    public void mover(int deltaX, int deltaY) {
        this.posicionX += deltaX;
        this.posicionY += deltaY;
    }

    protected void moverConLimites(int deltaX, int deltaY, MapaColision mapa) {
        if (deltaX != 0) {
            Rectangle hb = getHitbox();
            hb.translate(deltaX, 0);
            if (mapa == null || mapa.esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                mover(deltaX, 0);
            }
        }
        if (deltaY != 0) {
            Rectangle hb = getHitbox();
            hb.translate(0, deltaY);
            if (mapa == null || mapa.esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                mover(0, deltaY);
            }
        }
    }


    public abstract Rectangle getHitbox();
    // cada subclase conoce su propio tamaño de hitbox


        @Override
    public void colocarEnTile(int columna, int fila) {
        Rectangle hb = getHitbox();
        int offsetX = hb.x - getPosicionX(); // cuánto está corrido el hitbox respecto del sprite
        int offsetY = hb.y - getPosicionY();
        int centroX = columna * MapaColision.TILE + MapaColision.TILE / 2;
        int centroY = fila * MapaColision.TILE + MapaColision.TILE / 2;
        setPosicionX(centroX - offsetX - hb.width / 2);
        setPosicionY(centroY - offsetY - hb.height / 2);
    }


}
