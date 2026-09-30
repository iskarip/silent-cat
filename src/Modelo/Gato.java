package Modelo;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

public class Gato extends EntidadPasiva implements Interactuable {

    private static final int ANCHO_HITBOX = (int) (8 * ESCALA);   // ajustar al sprite
    private static final int ALTO_HITBOX = (int) (6 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (4 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (8 * ESCALA);

    private boolean encontrado = false;

    private final List<String> sonidosEncuentro = List.of(
        "Recursos/Sonidos/sonidos_gato/encuentro1.wav",
        "Recursos/Sonidos/sonidos_gato/encuentro2.wav",
        "Recursos/Sonidos/sonidos_gato/encuentro3.wav",
        "Recursos/Sonidos/sonidos_gato/encuentro4.wav"
    );
    private final Random random = new Random();

    public Gato(int columna, int fila) {
        colocarEnTile(columna, fila);
    }

    public boolean getEncontrado() { return encontrado; }
    public void setEncontrado(boolean encontrado) { this.encontrado = encontrado; }

    @Override
    public Rectangle getHitbox() {
        return new Rectangle(getPosicionX() + OFFSET_X_HITBOX, getPosicionY() + OFFSET_Y_HITBOX,
                             ANCHO_HITBOX, ALTO_HITBOX);
    }

    public void reproducirSonidoEncuentro() {
        ReproductorSonido.reproducir(sonidosEncuentro.get(random.nextInt(sonidosEncuentro.size())));
    }

    @Override
    public void interactuar(Personaje p) {
        if (!encontrado) {
            encontrado = true;
            reproducirSonidoEncuentro();
        }
    }
}