package Vista;

import java.awt.*;

public class AvisoInteraccion implements InterfazVisual {

    private static final long RETRASO_MS = 400;     // espera antes de empezar a aparecer
    private static final long APARICION_MS = 600;   // lo que tarda en llegar a opacidad completa

    private String texto = null;
    private long momentoInicio = 0;

    public void mostrar(String texto) {
        // Solo reinicia el reloj si antes no se mostraba, así el fade no se reinicia en cada tick
        if (this.texto == null) {
            momentoInicio = System.currentTimeMillis();
        }
        this.texto = texto;
    }

    public void ocultar() {
        this.texto = null;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (texto == null) return;

        long transcurrido = System.currentTimeMillis() - momentoInicio;
        if (transcurrido < RETRASO_MS) return;

        // 0.0 -> 1.0 durante APARICION_MS
        float aparicion = Math.min(1f, (transcurrido - RETRASO_MS) / (float) APARICION_MS);

        // Pulso lento, aplicado una vez que ya apareció
        float pulso = 0.8f + 0.2f * (float) Math.sin(System.currentTimeMillis() / 500.0);
        float opacidad = aparicion * pulso;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Serif", Font.PLAIN, 26));
        FontMetrics fm = g2d.getFontMetrics();

        int x = (ancho - fm.stringWidth(texto)) / 2;
        int y = alto - 150;

        Composite original = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawString(texto, x + 2, y + 2);

        g2d.setColor(new Color(225, 215, 190));
        g2d.drawString(texto, x, y);

        g2d.setComposite(original);
    }
}