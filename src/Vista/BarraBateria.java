package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Esta clase dibuja la barra de la batería sin depender de ninguna clase o interfaz de Modelo.
public class BarraBateria implements InterfazVisual {

    private static final int X = 20;
    private static final int Y = 20;
    private static final int ANCHO = 150;
    private static final int ALTO = 18;
    private static final int RADIO_BORDE = 8;

    // Estado local que actualiza el Controlador mediante datos primitivos
    private int bateriaActual = 100;

    public void actualizarBateria(int bateriaActual) {
        this.bateriaActual = Math.max(0, Math.min(100, bateriaActual));
    }

    public int getBateriaActual() {
        return bateriaActual;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("LINTERNA", X, Y - 4);

        // Fondo de la barra
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE);

        // Relleno de la batería basado en el valor primitivo actual
        int anchoRelleno = (int) (ANCHO * (bateriaActual / 100.0));
        if (anchoRelleno > 0) {
            g2d.setColor(bateriaActual < 20 ? Color.RED : new Color(255, 210, 80));
            g2d.fillRoundRect(X, Y, anchoRelleno, ALTO, RADIO_BORDE, RADIO_BORDE);
        }

        // Borde exterior
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE);
    }
}