package Modelo;

import java.awt.Rectangle;

public class Gato extends EntidadPasiva implements Interactuable {

    // Constantes de escala visual
    private static final int ANCHO_HITBOX_GATO = (int) (8 * ESCALA);   // ajustar al sprite
    private static final int ALTO_HITBOX_GATO = (int) (6 * ESCALA);
    private static final int OFFSET_X_HITBOX_GATO = (int) (4 * ESCALA);
    private static final int OFFSET_Y_HITBOX_GATO = (int) (8 * ESCALA);

    private boolean encontrado = false;

    private ObservadorGato observador;

    public Gato(int columna, int fila) {
        colocarEnTile(columna, fila);
        this.escalaSprite = 0.7; // Ajustar según el sprite real
        this.rutaSprites = "/Recursos/Sprites/Gato/";
    }

    public boolean getEncontrado() { return encontrado; }
    public void setEncontrado(boolean encontrado) { this.encontrado = encontrado; }

    // -- REUTILIZACIÓN DE CÓDIGO (Template Method de Entidad) --
    // En lugar de instanciar rectángulos manualmente, sobrescribe los métodos de medidas.
    // De este modo getHitbox() y getHitboxEnPosicion() se reutilizan de la superclase Entidad.

    @Override
    protected int getAnchoHitbox() { return ANCHO_HITBOX_GATO; }

    @Override
    protected int getAltoHitbox() { return ALTO_HITBOX_GATO; }

    @Override
    protected int getOffsetXHitbox() { return OFFSET_X_HITBOX_GATO; }

    @Override
    protected int getOffsetYHitbox() { return OFFSET_Y_HITBOX_GATO; }

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
            setDireccion(Direccion.DERECHA);
        } else if (objetivo.getPosicionX() < this.getPosicionX()) {
            deltaX = -1;
            setDireccion(Direccion.IZQUIERDA);
        }
 
        if (objetivo.getPosicionY() > this.getPosicionY()) {
            deltaY = 1;
            setDireccion(Direccion.ABAJO);
        } else if (objetivo.getPosicionY() < this.getPosicionY()) {
            deltaY = -1;
            setDireccion(Direccion.ARRIBA);
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