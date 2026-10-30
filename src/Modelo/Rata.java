package Modelo;

import java.awt.Rectangle;
import java.util.Random;

public class Rata extends EntidadPasiva {

    // Ajustalos mirando el sprite de la rata
    private static final int ANCHO_HITBOX_RATA = (int) (6 * ESCALA);
    private static final int ALTO_HITBOX_RATA = (int) (2 * ESCALA);
    private static final int OFFSET_X_HITBOX_RATA = (int) (7 * ESCALA);
    private static final int OFFSET_Y_HITBOX_RATA = (int) (18 * ESCALA);

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

    // -- REUTILIZACIÓN DE CÓDIGO (Template Method de Entidad) --
    // Reutiliza getHitbox() de Entidad definiendo únicamente sus dimensiones específicas

    @Override
    protected int getAnchoHitbox() { return ANCHO_HITBOX_RATA; }

    @Override
    protected int getAltoHitbox() { return ALTO_HITBOX_RATA; }

    @Override
    protected int getOffsetXHitbox() { return OFFSET_X_HITBOX_RATA; }

    @Override
    protected int getOffsetYHitbox() { return OFFSET_Y_HITBOX_RATA; }

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
        if (random.nextInt(5) == 0) return; // 1 de cada 5 veces se queda quieta

        Direccion nueva = Direccion.aleatoria(random);
        setDireccion(nueva);
        dx = nueva.getDx();
        dy = nueva.getDy();
    }
}