package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Esta clase dibuja la barra de vida del personaje en la pantalla.
// Recibe datos primitivos desde el Controlador, sin importar clases de Modelo.
public class BarraVida implements InterfazVisual {

    private static final int X = 20;
    private static final int Y = 60; // debajo de la barra de batería
    private static final int ANCHO = 150;
    private static final int ALTO = 18;
    private static final int RADIO_BORDE = 8;
    private static final int UMBRAL_VIDA_BAJA = 25;

    private int vidaActual;

    public BarraVida(int vidaInicial) {
        this.vidaActual = vidaInicial;
    }

    public void actualizarVida(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(100, vidaActual));
    }

    public int getVidaActual() {
        return vidaActual;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("VIDA", X, Y - 4);

        // Fondo de la barra
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE);

        // Relleno de salud
        int anchoRelleno = (int) (ANCHO * (vidaActual / 100.0));
        if (anchoRelleno > 0) {
            g2d.setColor(vidaActual <= UMBRAL_VIDA_BAJA ? Color.RED : new Color(80, 200, 90));
            g2d.fillRoundRect(X, Y, anchoRelleno, ALTO, RADIO_BORDE, RADIO_BORDE);
        }

        // Borde exterior
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE);
    }
}