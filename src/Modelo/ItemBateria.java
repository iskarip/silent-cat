package Modelo;

public class ItemBateria extends Item {

    private int cantidadCarga;

    // Constructor con posición en Tile
    public ItemBateria(int columna, int fila) {
        super("Bateria", "/Recursos/Sprites/Items/bateria.png", columna, fila);
        this.cantidadCarga = 10;
    }

    // Constructor sobrecargado por si querés variar la carga
    public ItemBateria(int columna, int fila, int cantidadCarga) {
        super("Bateria", "/Recursos/Sprites/Items/bateria.png", columna, fila);
        this.cantidadCarga = cantidadCarga;
    }

    public int getCantidadCarga() {
        return cantidadCarga;
    }

    public void aplicarCarga(Personaje p) {
        if (!isRecogido()) {
            recoger();
            p.recargarLinterna(this.cantidadCarga);
        }
    }

    @Override
    public void interactuar(Personaje p) {
        aplicarCarga(p);
    }

}