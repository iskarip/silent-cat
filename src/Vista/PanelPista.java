package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

// Muestra una pista en grande, centrada, sobre una sombra oscura.
// El controlador la abre con la ruta de la imagen y la cierra cuando el jugador termina de leer.
public class PanelPista implements InterfazVisual {

    private static final double PROPORCION_MAXIMA = 0.8; // ocupa como mucho el 80% de la pantalla
    private static final String AYUDA = "[E] Cerrar";

    private Image imagen = null;
    private boolean visible = false;

    public void abrir(String rutaImagenGrande) {
        this.imagen = GestorSprites.cargarImagen(rutaImagenGrande);
        this.visible = true;
    }

    public void cerrar() {
        this.visible = false;
        this.imagen = null;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!visible) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Sombra sobre todo el juego
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, ancho, alto);

        // Imagen centrada, sin deformarse
        if (imagen != null) {
            int anchoOrig = imagen.getWidth(panel);
            int altoOrig = imagen.getHeight(panel);

            if (anchoOrig > 0 && altoOrig > 0) {
                double escala = Math.min(ancho * PROPORCION_MAXIMA / anchoOrig,
                                         alto * PROPORCION_MAXIMA / altoOrig);
                int w = (int) (anchoOrig * escala);
                int h = (int) (altoOrig * escala);
                g2d.drawImage(imagen, (ancho - w) / 2, (alto - h) / 2, w, h, panel);
            }
        }

        // Ayuda abajo
        g2d.setFont(new Font("Serif", Font.PLAIN, 22));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(new Color(225, 215, 190));
        g2d.drawString(AYUDA, (ancho - fm.stringWidth(AYUDA)) / 2, alto - 40);
    }
}