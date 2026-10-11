package Vista;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


// Esta clase se encarga de la atmosfera de iluminacion y oscuridad de nuestros niveles.
// Escucha a la Linterna para saber si dibujar el aura de luz o no (Observer).
public class AuraVisual implements InterfazVisual {

    // -- ATRIBUTOS --
    private BufferedImage texturaVinieta;
    private BufferedImage texturaAura;
    private int radioVinietaCache = 0;
    private int radioAuraCache = 0;

    // Estado local notificado por la Linterna
    private boolean linternaEncendida = true;
    
    //datos q le pasa el controlador
    private int bateria = 100;      // 0 a 100
    private int tension = 0;        // 0 a 3

    //posicion del jugador en el mapa (la actualiza el controlador)
    private boolean posicionConocida = false;
    private int jugadorX;
    private int jugadorY;

    // Intensidad actual de la luz (1 = plena, 0 = apagada)
    private float intensidad = 1f;
   


    // --- METODO QUE USA EL CONTROLADOR ---

    // Lo llama el controlador cuando la linterna se prende o se apaga
    public void setEncendida(boolean encendida) {
        this.linternaEncendida = encendida;
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
    
// Poca bateria o un enemigo cerca hacen que la luz titile mas seguido
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
        //Si todavia no sabemos donde esta el jugador, no hay nada q iluminar
        if (!posicionConocida){
            return;
        }

        // Calculo la posicion del personaje en el mapa
        int centroX = jugadorX + (int)((NivelPanel.ANCHO_CUADRO * NivelPanel.ESCALA) / 2);
        int centroY = jugadorY + (int)((NivelPanel.ALTO_CUADRO * NivelPanel.ESCALA) / 2);

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
        
        //luz de la linterna
        if (linternaEncendida && texturaAura != null) {
            actualizarIntensidad();
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.SrcOver.derive(intensidad));
            g2d.drawImage(texturaAura, pantallaX - radioAura, pantallaY - radioAura, null);
            g2d.setComposite(original);
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

