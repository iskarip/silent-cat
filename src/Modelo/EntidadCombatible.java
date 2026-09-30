package Modelo;

public abstract class EntidadCombatible extends Entidad{

    protected int puntosVida;

    public EntidadCombatible (int puntosVida) {
        this.puntosVida = puntosVida;
    }

    public int getPuntosVida (){
        return puntosVida;
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

    public void recibirDanio (int cantidad){
        setPuntosVida(this.puntosVida - cantidad);
    }

    public boolean estaVivo() {
        return puntosVida > 0;
    }

    public abstract void atacar (EntidadCombatible objetivo);
    // método SIN cuerpo, termina en ";" — cada subclase decide cómo atacar

}
