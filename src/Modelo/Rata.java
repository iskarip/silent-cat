package Modelo;

import java.util.Random;

public class Rata extends EntidadPasiva {

    private static final int ANCHO_HITBOX_RATA = 16;
    private static final int ALTO_HITBOX_RATA = 8;
    private static final int OFFSET_X_HITBOX_RATA = 16;
    private static final int OFFSET_Y_HITBOX_RATA = 18; // Pegada al suelo, no a 45px de distancia

    private final Random random = new Random();
    private int dx = 0;
    private int dy = 0;
    private int ticksHastaCambiar = 0;
    private boolean moviendose = false;

    public Rata(int columna, int fila) {
        setRutaSprites("/Recursos/Sprites/Rata/");
        setEscalaSprite(0.3);
        colocarEnTile(columna, fila);
    }

    // -- REUTILIZACIÓN DE CÓDIGO (Template Method de Entidad) --

    @Override
    protected int getAnchoHitbox() { 
        return ANCHO_HITBOX_RATA; 
    }

    @Override
    protected int getAltoHitbox() { 
        return ALTO_HITBOX_RATA; 
    }

    @Override
    protected int getOffsetXHitbox() { 
        return OFFSET_X_HITBOX_RATA; 
    }

    @Override
    protected int getOffsetYHitbox() { 
        return OFFSET_Y_HITBOX_RATA; 
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
        if (random.nextInt(5) == 0) return; // 1 de cada 5 veces se queda quieta

        Direccion nueva = Direccion.aleatoria(random);
        setDireccion(nueva);
        dx = nueva.getDx();
        dy = nueva.getDy();
    }
}