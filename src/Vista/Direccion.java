package Vista;

public enum Direccion {
    ABAJO(0),
    IZQUIERDA (1),
    ARRIBA(2),
    DERECHA (1);

    private final int fila;

    Direccion(int fila) {
        this.fila = fila;
    }

    public int getFila() {
        return fila;
    }
}
