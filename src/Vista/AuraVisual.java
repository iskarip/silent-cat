package Vista;

import Modelo.ObservadorLinterna;
import Modelo.Personaje;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

// Esta clase se encarga de la atmosfera de iluminacion y oscuridad de nuestros niveles.
// Escucha a la Linterna para saber si dibujar el aura de luz o no (Observer).
public class AuraVisual implements InterfazVisual, ObservadorLinterna {

    // -- ATRIBUTOS --
    private BufferedImage texturaVinieta;
    private BufferedImage texturaAura;
    private int radioVinietaCache = 0;
    private int radioAuraCache = 0;

    // Estado local notificado por la Linterna
    private boolean linternaEncendida = true;

    // --- METODO DEL OBSERVADOR ---
    @Override
    public void linternaCambio(int bateriaActual, boolean encendida) {
        this.linternaEncendida = encendida;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        // Primero obtengo mi personaje
        Personaje personaje = panel.getPersonaje();

        // Compruebo, si no hay personaje, no hago nada
        if (personaje == null) {
            return;
        }

        // Calculo la posicion del personaje en el mapa
        int centroX = personaje.getPosicionX() + (int)((NivelPanel.ANCHO_CUADRO * NivelPanel.ESCALA) / 2);
        int centroY = personaje.getPosicionY() + (int)((NivelPanel.ALTO_CUADRO * NivelPanel.ESCALA) / 2);

        // Coordenadas de pantalla
        int pantallaX = (int) ((centroX - panel.getCamaraX()) * NivelPanel.ZOOM);
        int pantallaY = (int) ((centroY - panel.getCamaraY()) * NivelPanel.ZOOM);

        int radioVinieta = (int) (130 * NivelPanel.ZOOM);
        int radioAura = (int) (130 * NivelPanel.ZOOM);

        // Genera la imagen en memoria una sola vez
        prepararTexturas(radioVinieta, radioAura);

        // Viñeta constante
        if (texturaVinieta != null) {
            g2d.drawImage(texturaVinieta, pantallaX - radioVinieta, pantallaY - radioVinieta, null);

            // Relleno negro de los bordes sobrantes de la pantalla
            g2d.setColor(Color.BLACK);
            if (pantallaX - radioVinieta > 0) {
                g2d.fillRect(0, 0, pantallaX - radioVinieta, alto);
            }
            if (pantallaX + radioVinieta < ancho) {
                g2d.fillRect(pantallaX + radioVinieta, 0, ancho - (pantallaX + radioVinieta), alto);
            }
            if (pantallaY - radioVinieta > 0) {
                g2d.fillRect(0, 0, ancho, pantallaY - radioVinieta);
            }
            if (pantallaY + radioVinieta < alto) {
                g2d.fillRect(0, pantallaY + radioVinieta, ancho, alto - (pantallaY + radioVinieta));
            }
        }

        // Luz de la linterna: se dibuja basandose en el estado notificado por el Observer
        if (linternaEncendida && texturaAura != null) {
            g2d.drawImage(texturaAura, pantallaX - radioAura, pantallaY - radioAura, null);
        }
    }

    // Dibuja los gradientes en memoria una sola vez
    private void prepararTexturas(int radioVinieta, int radioAura) {
        if (texturaVinieta == null || radioVinietaCache != radioVinieta) {
            radioVinietaCache = radioVinieta;
            texturaVinieta = new BufferedImage(radioVinieta * 2,  radioVinieta * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gV = texturaVinieta.createGraphics();

            float[] distVinieta = {0.0f, 0.5f, 1.0f};
            Color[] coloresVinieta = {
                    new Color(0,0,0,60),
                    new Color(0, 0, 0, 180),
                    new Color(0, 0, 0, 255)
            };

            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Float(radioVinieta, radioVinieta),
                    radioVinieta, distVinieta, coloresVinieta
            );
            gV.setPaint(rgp);
            gV.fillRect(0, 0, radioVinieta * 2, radioVinieta * 2);
            gV.dispose();
        }

        if (texturaAura == null || radioAuraCache != radioAura) {
            radioAuraCache = radioAura;
            texturaAura = new BufferedImage(radioAura * 2, radioAura * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gA = texturaAura.createGraphics();

            float[] distAura = { 0.0f, 0.4f, 1.0f };
            Color[] coloresAura = {
                    new Color(255, 230, 160, 100),
                    new Color(255, 210, 130, 20),
                    new Color(0, 0, 0, 0)
            };

            RadialGradientPaint rgpAura = new RadialGradientPaint(
                    new Point2D.Float(radioAura, radioAura),
                    radioAura, distAura, coloresAura
            );
            gA.setPaint(rgpAura);
            gA.fillRect(0, 0, radioAura * 2, radioAura * 2);
            gA.dispose();
        }
    }
}

