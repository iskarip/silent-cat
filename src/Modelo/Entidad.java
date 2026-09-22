package Modelo;

import java.awt.Rectangle;

public abstract class Entidad implements Posicionable { // Entidad es abstracta porque no se puede instanciar directamente, solo a través de sus subclases

    // -- ATRIBUTOS --

    public static final double ESCALA = 2.5;

    protected int puntosVida;
    private int posicionX;
    private int posicionY;
    protected Direccion direccion = Direccion.ABAJO;

    // -- CONSTRUCTOR --
    public Entidad (int puntosVida) {
        this.puntosVida = puntosVida;
    }

    // -- SET's y GET's --
    public int getPuntosVida (){
        return puntosVida;
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

    public void setPuntosVida(int puntosVida) {
        if (puntosVida < 0 ) {
            this.puntosVida = 0;
        } else if (puntosVida > 100) {
            this.puntosVida = 100;
        } else {
            this.puntosVida = puntosVida;
        }

    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    // -- METODOS --

    public void recibirDanio (int cantidad){
        setPuntosVida(this.puntosVida - cantidad);
    }

    public void mover(int deltaX, int deltaY) {
        this.posicionX += deltaX;
        this.posicionY += deltaY;
    }

    public abstract void atacar(Entidad objetivo);
    // método SIN cuerpo, termina en ";" — cada subclase decide cómo atacar

    public abstract Rectangle getHitbox();
    // cada subclase conoce su propio tamaño de hitbox

    public boolean estaVivo() {
        return puntosVida > 0;
    }


}
