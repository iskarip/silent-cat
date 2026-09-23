package Modelo;

public interface Posicionable {
    int getPosicionX();
    int getPosicionY();
    void setPosicionX(int posicionX);
    void setPosicionY(int posicionY);


    default void colocarEnTile(int columna, int fila) {
        setPosicionX(columna * MapaColision.TILE);
        setPosicionY(fila * MapaColision.TILE);
    }
}