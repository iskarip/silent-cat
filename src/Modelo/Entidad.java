package Modelo;

public abstract class Entidad {

    // -- ATRIBUTOS --
    protected int puntosVida;
    protected int danioBase;
    private int posicionX;
    private int posicionY;

    // -- CONSTRUCTOR --
    public Entidad (int puntosVida,int danioBase) {
        this.puntosVida = puntosVida;
        this.danioBase = danioBase;
    }

    // -- SET's y GET's --
    public int getPuntosVida (){
        return puntosVida;
    }

    public int getDanioBase() {
        return danioBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
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


}
