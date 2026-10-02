package Modelo;

import java.awt.Rectangle;
import java.util.Random;

public class Rata extends EntidadPasiva {

    // Ajustalos mirando el sprite de la rata
    private static final int ANCHO_HITBOX = (int) (16 * ESCALA);
    private static final int ALTO_HITBOX = (int) (8 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (8 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (20 * ESCALA);

    private final Random random = new Random();
    private int dx = 0;
    private int dy = 0;
    private int ticksHastaCambiar = 0;
    private boolean moviendose = false;

    public Rata(int columna, int fila) {
        this.rutaSprites = "/Recursos/Sprites/Rata/";
        this.escalaSprite = 0.3;
        colocarEnTile(columna, fila);
    }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(getPosicionX() + OFFSET_X_HITBOX, getPosicionY() + OFFSET_Y_HITBOX,
                             ANCHO_HITBOX, ALTO_HITBOX);
    }

    @Override
    public boolean estaMoviendose() {
        return moviendose;
    }

    @Override
    public void moverse(Personaje objetivo, MapaColision mapa) {
        ticksHastaCambiar--;
        if (ticksHastaCambiar <= 0) {
            elegirDireccion();
            ticksHastaCambiar = 40 + random.nextInt(60);
        }
        moverConLimites(dx, dy, mapa);
        moviendose = (dx != 0 || dy != 0);
    }

    private void elegirDireccion() {
        dx = 0;
        dy = 0;
        switch (random.nextInt(5)) {
            case 1: dx = 1;  direccion = Direccion.DERECHA;   break;
            case 2: dx = -1; direccion = Direccion.IZQUIERDA; break;
            case 3: dy = 1;  direccion = Direccion.ABAJO;     break;
            case 4: dy = -1; direccion = Direccion.ARRIBA;    break;
            default: break; // se queda quieta
        }
    }
}