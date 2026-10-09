package Modelo;

import java.awt.Rectangle;

public class Gato extends EntidadPasiva implements Interactuable {

    private static final int ANCHO_HITBOX = (int) (8 * ESCALA);   // ajustar al sprite
    private static final int ALTO_HITBOX = (int) (6 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (4 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (8 * ESCALA);

    private boolean encontrado = false;

    private ObservadorGato observador;

    public Gato(int columna, int fila) {
        colocarEnTile(columna, fila);
        this.escalaSprite = 0.3; // Ajustar según el sprite real
        this.rutaSprites = "/Recursos/Sprites/Gato/";
    }

    public boolean getEncontrado() { return encontrado; }
    public void setEncontrado(boolean encontrado) { this.encontrado = encontrado; }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(getPosicionX() + OFFSET_X_HITBOX, getPosicionY() + OFFSET_Y_HITBOX,
                             ANCHO_HITBOX, ALTO_HITBOX);
    }

    private Rectangle getHitboxEnPosicion(int x, int y){
        return new Rectangle (x + OFFSET_X_HITBOX, y + OFFSET_Y_HITBOX, ANCHO_HITBOX, ALTO_HITBOX);
    }

    public void setObservador(ObservadorGato observador) {
        this.observador = observador;
    }

//implementacion del metodo abstracto de EntidadPasiva, nuestro gato caminaria
//hacia donde esta el personaje, sin atravesar paredes. Antes de ser encontardo se queda quieto?


   @Override
    public void moverse(Personaje objetivo, MapaColision mapa) {
        if(!encontrado) return; //si no lo encontramos, no se mueve.
       
        int deltaX = 0;
        int deltaY = 0;
 
        if (objetivo.getPosicionX() > this.getPosicionX()) {
            deltaX = 1;
            direccion = Direccion.DERECHA;
        } else if (objetivo.getPosicionX() < this.getPosicionX()) {
            deltaX = -1;
            direccion = Direccion.IZQUIERDA;
        }
 
        if (objetivo.getPosicionY() > this.getPosicionY()) {
            deltaY = 1;
            direccion = Direccion.ABAJO;
        } else if (objetivo.getPosicionY() < this.getPosicionY()) {
            deltaY = -1;
            direccion = Direccion.ARRIBA;
        }
 
        moverConLimites(deltaX, deltaY, mapa);
    }

    @Override
    public void interactuar(Personaje p) {
        if (!encontrado) {
            setEncontrado(true);
            if (observador != null) {
                observador.gatoEncontrado();
            }
        }
    }
}