package Vista;

import java.awt.Color;
import java.awt.Graphics2D;

// Barra de estamina sobre el personaje, solo se ve cuando es necesario
public class BarraEstamina implements InterfazVisual {

    private static final int ANCHO = 20;
    private static final int ALTO = 3;
    private static final int OFFSET_X = 14;   // el cuadro del sprite mide 48 y el cuerpo queda centrado
    private static final int OFFSET_Y = 2;    // apenas arriba de la cabeza

    private int estamina = 100;
    private boolean agotada = false;
    private int posX;
    private int posY;

    // El controlador la actualiza
    public void actualizar(int estamina, boolean agotada, int posX, int posY) {
        this.estamina = Math.max(0, Math.min(100, estamina));
        this.agotada = agotada;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (estamina >= 100) return;   // llena: no se dibuja

        double zoom = NivelPanel.ZOOM;
        int x = (int) ((posX + OFFSET_X - panel.getCamaraX()) * zoom);
        int y = (int) ((posY + OFFSET_Y - panel.getCamaraY()) * zoom);
        int w = (int) (ANCHO * zoom);
        int h = (int) (ALTO * zoom);

        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(x, y, w, h);

        int relleno = (int) (w * (estamina / 100.0));
        g2d.setColor(agotada ? new Color(200, 60, 60) : new Color(100, 200, 255));
        g2d.fillRect(x, y, relleno, h);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, w, h);
    }
}