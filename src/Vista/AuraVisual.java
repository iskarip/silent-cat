package Vista;

import Modelo.Personaje;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

//esta clase se va a encargar de la atmosfera de iluminacion y oscuridad de nuestros niveles
//primero dibuja la viñeta constante alrededor del jugador
//Segundo si la linterna esta encencida suma el aura alredor del personaje

public class AuraVisual implements InterfazVisual {
    
    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        //primero obtengo mi personaje 
        Personaje personaje = panel.getPersonaje();

        //compruebo, si no hay personaje, no hago nada
        if (personaje == null ){
             return;
        }
        
        //calculo la posicion del personaje en el mapa
        int centroX = personaje.getPosicionX() + (int)((NivelPanel.ANCHO_CUADRO * NivelPanel.ESCALA) / 2);
        int centroY = personaje.getPosicionY() + (int)((NivelPanel.ALTO_CUADRO * NivelPanel.ESCALA) / 2);
  
        //ahora se transformaria a coordenadas de pantalla
    int pantallaX = (int) ((centroX - panel.getCamaraX()) * NivelPanel.ZOOM);
    int pantallaY = (int) ((centroY - panel.getCamaraY()) * NivelPanel.ZOOM);
    Point2D centro = new Point2D. Float(pantallaX, pantallaY);

   //dibujo la viñeta para darle ambiente >:)
   float radioVinieta = (float) (130 *NivelPanel.ZOOM); //es la distancia del sombreado
   float [] distVinieta = { 0.0f, 0.5f, 1.0f};

    Color[] coloresVinieta = {
        new Color (0, 0, 0, 60), //este seria nuestro centro
        new Color (0, 0, 0, 180) , //esta la zona media
        new Color( 0, 0, 0, 255) //los bordes de la pantalla, la oscuridd
    };
    //aca se oscureceria la pantalla, teniendo en cuenta el degradado definido 
    RadialGradientPaint vinyetaGradiente = new RadialGradientPaint(centro, radioVinieta, distVinieta, coloresVinieta);
    g2d.setPaint(vinyetaGradiente);
    g2d.fillRect(0, 0, ancho, alto);

//______________________________________________
if (personaje.getLinterna() != null && personaje.getLinterna().getEncendido()) {

//ahora dibujaria la luz, el aura al rededor del jugador, suavecita
//podemos ajustar radioAura si queremos que el resplandor del pj alcance mas o menos distancia

    float radioAura = (float) (130 * NivelPanel.ZOOM); 
    float[] distAura = {0.0f, 0.4f, 1.0f};

    Color[] coloresAura = {
            new Color(255, 230, 160, 100), // este seria nuestro centro
            new Color(255, 210, 130, 20),  // esta la zona media
            new Color(0, 0, 0, 0)          // los bordes de la pantalla
        };

        RadialGradientPaint auraGradiente = new RadialGradientPaint(centro, radioAura, distAura, coloresAura);

        //ahora pintaria
        g2d.setPaint(auraGradiente);
            g2d.fillOval(
                (int) (pantallaX - radioAura), 
                (int) (pantallaY - radioAura), 
                (int) (radioAura * 2), 
                (int) (radioAura * 2)
            );
        }
    }
}









