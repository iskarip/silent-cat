package Modelo;

import java.awt.Rectangle;

//Especialización de Entidad para cualquier personaje de combate.
//Maneja el estado de salud y reglas de daño.
 
public abstract class EntidadCombatible extends Entidad {

    protected int puntosVida;

    public EntidadCombatible(int puntosVida) {
        setPuntosVida(puntosVida);
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    // Encapsulamiento de la vida: restringe el rango estrictamente de 0 a 100.
    
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
        if (cantidad > 0) {
            setPuntosVida(this.puntosVida - cantidad);
        }
    }

    @Override
    public boolean estaVivo() {
        return puntosVida > 0;
    }

    @Override
    public boolean mostrarBarraVida() {
        return true;
    }

    @Override
    public int getPorcentajeVida() {
        return puntosVida;
    }

    // Dimensiones base para entidades humanoides (Personaje y Enemigos comunes)
    protected int getAnchoHitbox() { return (int) (6 * ESCALA); }
    protected int getAltoHitbox() { return (int) (2 * ESCALA); }
    protected int getOffsetXHitbox() { return (int) (7 * ESCALA); }
    protected int getOffsetYHitbox() { return (int) (15 * ESCALA); }

    @Override
    public Rectangle getHitbox() {
        return getHitboxEnPosicion(getPosicionX(), getPosicionY());
    }

    public Rectangle getHitboxEnPosicion(int x, int y) {
        return new Rectangle(x + getOffsetXHitbox(), y + getOffsetYHitbox(), getAnchoHitbox(), getAltoHitbox());
    }

    public abstract void atacar(EntidadCombatible objetivo);
}