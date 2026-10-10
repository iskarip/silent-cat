package Modelo;

import java.util.Random;

public enum Direccion {
    ABAJO(0, 0, 1),
    IZQUIERDA(1, -1, 0),
    ARRIBA(2, 0, -1),
    DERECHA(3, 1, 0);

    private final int fila;
    private final int dx;   // hacia donde se mueve en X (-1, 0 o 1)
    private final int dy;   // hacia donde se mueve en Y (-1, 0 o 1)

    Direccion(int fila, int dx, int dy) {
        this.fila = fila;
        this.dx = dx;
        this.dy = dy;
    }

    public int getFila() { return fila; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

    // Devuelve una dirección cualquiera de las cuatro
    public static Direccion aleatoria(Random random) {
        Direccion[] todas = values();
        return todas[random.nextInt(todas.length)];
    }
}
