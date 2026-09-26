package Modelo;

public class ItemMedicina extends Item {

    private int curacion;

    // Constructor con curación personalizada y posición
    public ItemMedicina(int columna, int fila, int curacion) {
        super("Medicina", "/Recursos/Sprites/Items/medicina.png", columna, fila);
        this.curacion = curacion;
    }

    // Constructor con curación estándar (25)
    public ItemMedicina(int columna, int fila) {
        this(columna, fila, 25);
    }

    public int getCantidadCuracion() {
        return curacion;
    }

    public void setCuracion(int curacion) {
        this.curacion = curacion;
    }

    public void aplicarCuracion(Personaje personaje) {
        if (!isRecogido() && personaje.getPuntosVida() < 100) {
            recoger();
            int nuevaVida = Math.min(100, personaje.getPuntosVida() + this.curacion);
            personaje.setPuntosVida(nuevaVida);
        }
    }

    @Override
    public void interactuar(Personaje p) {
        aplicarCuracion(p);
    }
}
