package Vista;

import java.awt.Graphics2D;

// Capa de polvo flotando sobre el juego. Reutiliza ParticulasAmbiente (la del menú).
public class PolvoVisual implements InterfazVisual {

    private final ParticulasAmbiente particulas = new ParticulasAmbiente(70, 1280, 720);
    private int anchoPrevio = 0;
    private int altoPrevio = 0;

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (ancho != anchoPrevio || alto != altoPrevio) {
            particulas.setTamanio(ancho, alto);
            anchoPrevio = ancho;
            altoPrevio = alto;
        }
        particulas.actualizar();
        particulas.dibujar(g2d);
    }
}