package Vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class AuraVisual implements InterfazVisual {

    // Textura para la luz y DOS texturas para la viñeta (prendida / apagada)
    private BufferedImage texturaVinietaLuz;
    private BufferedImage texturaVinietaOscura;
    private BufferedImage texturaAura;
    
    private int radioVinietaCache = 0;
    private int radioAuraCache = 0;

    // Estado de la linterna
    private boolean linternaEncendida = true;
    private boolean centroOscuro = false;
    private boolean estadoCentroOscuroCache = false;

    // Datos del controlador
    private int bateria = 100;
    private int tension = 0;

    // Posición del jugador
    private boolean posicionConocida = false;
    private int jugadorX;
    private int jugadorY;

    private float intensidad = 1f;

    // --- MÉTODOS DEL CONTROLADOR ---

    public void setEncendida(boolean encendida) {
        this.linternaEncendida = encendida;
    }
    
    public void setCentroOscuro(boolean centroOscuro) {
        if (this.centroOscuro != centroOscuro) {
            this.centroOscuro = centroOscuro;
            this.radioAuraCache = -1;
        }
    }

    public void setBateria(int bateria) {
        this.bateria = bateria;
    }

    public void setTension(int tension) {
        this.tension = tension;
    }

    public void setPosicionJugador(int x, int y) {
        this.jugadorX = x;
        this.jugadorY = y;
        this.posicionConocida = true;
    }

    private void actualizarIntensidad() {
        double probabilidadDeCorte = 0.004
            + (100 - bateria) / 100.0 * 0.03
            + tension * 0.03;

        float profundidadCorte = Math.max(0f, 0.15f - tension * 0.05f);    
        float variacionLeve = 0.12f + tension * 0.08f;                    
        float suavizado = 0.4f + tension * 0.1f;                          

        float objetivo = (Math.random() < probabilidadDeCorte)
            ? profundidadCorte                                        
            : 1f - (float) Math.random() * variacionLeve;             
        intensidad += (objetivo - intensidad) * suavizado;
    }

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!posicionConocida) {
            return;
        }

        int centroX = jugadorX + (int)((NivelPanel.ANCHO_CUADRO * NivelPanel.ESCALA) / 2);
        int centroY = jugadorY + (int)((NivelPanel.ALTO_CUADRO * NivelPanel.ESCALA) / 2);

        int pantallaX = (int) ((centroX - panel.getCamaraX()) * NivelPanel.ZOOM);
        int pantallaY = (int) ((centroY - panel.getCamaraY()) * NivelPanel.ZOOM);

        int radioVinieta = (int) (200 * NivelPanel.ZOOM);
        int radioAura = (int) (150 * NivelPanel.ZOOM);

        prepararTexturas(radioVinieta, radioAura);

        // 1. DIBUJAR LUZ (Solo si está prendida)
        if (linternaEncendida && texturaAura != null) {
            actualizarIntensidad();
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.SrcOver.derive(intensidad));
            g2d.drawImage(texturaAura, pantallaX - radioAura, pantallaY - radioAura, null);
            g2d.setComposite(original);
        }

        // 2. ELEGIR QUÉ VIÑETA DIBUJAR SEGÚN EL ESTADO DE LA LINTERNA
        BufferedImage vinietaAUsar = linternaEncendida ? texturaVinietaLuz : texturaVinietaOscura;

        if (vinietaAUsar != null) {
            g2d.drawImage(vinietaAUsar, pantallaX - radioVinieta, pantallaY - radioVinieta, null);

            // Relleno negro fuera de los bordes del círculo
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
    }

    private void prepararTexturas(int radioVinieta, int radioAura) {
        if (texturaVinietaLuz == null || radioVinietaCache != radioVinieta) {
            radioVinietaCache = radioVinieta;

            // --- VIÑETA SUAVE (Cuando la linterna está prendida) ---
            texturaVinietaLuz = new BufferedImage(radioVinieta * 2, radioVinieta * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gVL = texturaVinietaLuz.createGraphics();
            float[] distLuz = {0.0f, 0.5f, 1.0f};
            Color[] coloresLuz = {
                    new Color(0, 0, 0, 0),     // Centro despejado
                    new Color(0, 0, 0, 200),   // Penumbra suave
                    new Color(0, 0, 0, 255)    // Negro en bordes
            };
            gVL.setPaint(new RadialGradientPaint(new Point2D.Float(radioVinieta, radioVinieta), radioVinieta, distLuz, coloresLuz));
            gVL.fillRect(0, 0, radioVinieta * 2, radioVinieta * 2);
            gVL.dispose();

            // --- VIÑETA OSCURA Y OPACA (Cuando la linterna está apagada) ---
            texturaVinietaOscura = new BufferedImage(radioVinieta * 2, radioVinieta * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gVO = texturaVinietaOscura.createGraphics();
            float[] distOscura = {0.0f, 0.15f, 0.5f, 1.0f};
            Color[] coloresOscuros = {
                    new Color(0, 0, 0, 140),   // Centro opaco incluso cerca del jugador
                    new Color(0, 0, 0, 220),   // Oscuridad muy pesada pegada al jugador
                    new Color(0, 0, 0, 250),   // Casi negro total
                    new Color(0, 0, 0, 255)    // Negro absoluto
            };
            gVO.setPaint(new RadialGradientPaint(new Point2D.Float(radioVinieta, radioVinieta), radioVinieta, distOscura, coloresOscuros));
            gVO.fillRect(0, 0, radioVinieta * 2, radioVinieta * 2);
            gVO.dispose();
        }

        if (texturaAura == null || radioAuraCache != radioAura || estadoCentroOscuroCache != centroOscuro) {
            radioAuraCache = radioAura;
            estadoCentroOscuroCache = centroOscuro;
            
            texturaAura = new BufferedImage(radioAura * 2, radioAura * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gA = texturaAura.createGraphics();

            if (centroOscuro) {
                float[] distAura = { 0.0f, 0.25f, 0.55f, 1.0f };
                Color[] coloresAura = {
                        new Color(0, 0, 0, 210),
                        new Color(50, 40, 20, 150),
                        new Color(255, 220, 140, 160),
                        new Color(0, 0, 0, 0)
                };
                gA.setPaint(new RadialGradientPaint(new Point2D.Float(radioAura, radioAura), radioAura, distAura, coloresAura));
            } else {
                float[] distAura = { 0.0f, 0.4f, 1.0f };
                Color[] coloresAura = {
                        new Color(255, 230, 160, 140),
                        new Color(255, 210, 130, 40),
                        new Color(0, 0, 0, 0)
                };
                gA.setPaint(new RadialGradientPaint(new Point2D.Float(radioAura, radioAura), radioAura, distAura, coloresAura));
            }

            gA.fillRect(0, 0, radioAura * 2, radioAura * 2);
            gA.dispose();
        }
    }
}