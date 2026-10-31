package Modelo;

//Especialización de Entidad para cualquier personaje de combate.
//Maneja el estado de salud y reglas de daño.
 
public abstract class EntidadCombatible extends Entidad {

    private int puntosVida;

    public EntidadCombatible(int puntosVida) {
        this.puntosVida = limitarVida(puntosVida);// Asignación segura en el constructor sin llamar métodos sobreescribibles
    }

    public int getPuntosVida() { return puntosVida; }

    public void setPuntosVida(int puntosVida) {
        this.puntosVida = limitarVida(puntosVida);
    }

    private static int limitarVida(int vida) {
        return Math.max(0, Math.min(100, vida));
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (cantidad > 0) {
            setPuntosVida(this.puntosVida - cantidad);
            System.out.println("[Silent Cat] " + this.getClass().getSimpleName() 
                               + " recibio daño. Vida restante: " + this.puntosVida);
        }
    }

    @Override
    public boolean estaVivo() { return puntosVida > 0; }

    @Override
    public boolean mostrarBarraVida() { return true; }

    @Override
    public int getPorcentajeVida() { return puntosVida; }

    public abstract void atacar(EntidadCombatible objetivo);
}