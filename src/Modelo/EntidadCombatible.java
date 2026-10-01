package Modelo;

public abstract class EntidadCombatible extends Entidad {

    protected int puntosVida;

    public EntidadCombatible(int puntosVida) {
        this.puntosVida = puntosVida;
    }

    public int getPuntosVida() { return puntosVida; }

    public void setPuntosVida(int puntosVida) {
        if (puntosVida < 0) {
            this.puntosVida = 0;
        } else if (puntosVida > 100) {
            this.puntosVida = 100;
        } else {
            this.puntosVida = puntosVida;
        }
    }

    @Override
    public void recibirDanio(int cantidad) {
        setPuntosVida(this.puntosVida - cantidad);
    }

    @Override
    public boolean estaVivo() { return puntosVida > 0; }

    @Override
    public boolean mostrarBarraVida() { return true; }

    @Override
    public int getPorcentajeVida() { return puntosVida; }

    public abstract void atacar(EntidadCombatible objetivo);
}