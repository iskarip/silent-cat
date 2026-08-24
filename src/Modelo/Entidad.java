package Modelo;

public abstract class Entidad {

    // -- ATRIBUTOS --
    protected int puntosVida;
    protected int danioBase;

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

    public abstract void atacar(Entidad objetivo);  // método SIN cuerpo, termina en ";" — cada subclase decide cómo atacar


}
