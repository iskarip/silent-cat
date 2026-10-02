package Vista;

import Modelo.Item;
import Modelo.Personaje;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;

// Dibuja los slots del inventario en pantalla (coordenadas de pantalla, sin zoom).
// Se registra como capa visual igual que BarraVida y BarraBateria.
public class InventarioVisual implements InterfazVisual {

    private static final int CANTIDAD_SLOTS = 5;
    private static final int TAM_SLOT = 48;
    private static final int SEPARACION = 8;
    private static final int RADIO_BORDE = 8;
    private static final int MARGEN_INFERIOR = 30;

    private boolean visible = false;

    public void alternar() {
        visible = !visible;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!visible) return;

        Personaje personaje = panel.getPersonaje();
        if (personaje == null || personaje.getInventario() == null) return;

        List<Item> items = personaje.getInventario().getItems();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Centrado abajo
        int anchoTotal = CANTIDAD_SLOTS * TAM_SLOT + (CANTIDAD_SLOTS - 1) * SEPARACION;
        int inicioX = (ancho - anchoTotal) / 2;
        int y = alto - TAM_SLOT - MARGEN_INFERIOR;

        for (int i = 0; i < CANTIDAD_SLOTS; i++) {
            int x = inicioX + i * (TAM_SLOT + SEPARACION);

            // Fondo del slot
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.fillRoundRect(x, y, TAM_SLOT, TAM_SLOT, RADIO_BORDE, RADIO_BORDE);

            // Contenido (si hay item en este índice)
            if (i < items.size()) {
                Item item = items.get(i);
                Image img = GestorSprites.cargarImagen(item.getRutaImagen());
                if (img != null) {
                    int margen = 6;
                    g2d.drawImage(img, x + margen, y + margen,
                            TAM_SLOT - margen * 2, TAM_SLOT - margen * 2, panel);
                } else {
                    // Fallback si no existe el sprite: inicial del nombre
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                    g2d.setColor(Color.WHITE);
                    String inicial = item.getNombre().substring(0, 1);
                    int anchoTxt = g2d.getFontMetrics().stringWidth(inicial);
                    g2d.drawString(inicial, x + (TAM_SLOT - anchoTxt) / 2, y + TAM_SLOT / 2 + 7);
                }
            }

            // Borde
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(x, y, TAM_SLOT, TAM_SLOT, RADIO_BORDE, RADIO_BORDE);

            // Número de tecla (para cuando implementes "usar slot")
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2d.drawString(String.valueOf(i + 1), x + 4, y + 12);
        }
    }
}
