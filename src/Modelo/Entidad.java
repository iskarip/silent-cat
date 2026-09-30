package Modelo;

import java.awt.Rectangle;

public abstract class Entidad implements Posicionable { // Entidad es abstracta porque no se puede instanciar directamente, solo a través de sus subclases

    // -- ATRIBUTOS --

    public static final double ESCALA = 2.5;
    private int posicionX;
    private int posicionY;
    
    protected Direccion direccion = Direccion.ABAJO;

    // -- CONSTRUCTOR --

    // -- SET's y GET's --

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



    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    // -- METODOS --

    public void mover(int deltaX, int deltaY) {
        this.posicionX += deltaX;
        this.posicionY += deltaY;
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
