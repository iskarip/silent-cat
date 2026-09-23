package Vista;
import Modelo.Personaje;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

//esta clase se escanrga unicamente de pintar el aura de luz al rededor del personaje
//Si la linterna esta encendida.



public class LinternaOverlay implements InterfazVisual {
    
    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        //primero obtengo mi personaje 
        Personaje personaje = panel.getPersonaje();

        //compruebo, si no hay personaje, no hago nada
        if (personaje == null || personaje.getLinterna() == null || !personaje.getLinterna().getEncendido()) {
            return;
        }
        
        //calculo la posicion del personaje en el mapa
        int centroX = personaje.getPosicionX() + (int)((NivelPanel.ANCHO_CUADRO * NivelPanel.ESCALA) / 2);
        int centroY = personaje.getPosicionY() + (int)((NivelPanel.ALTO_CUADRO * NivelPanel.ESCALA) / 2);
  
        //ahora se transformaria a coordenadas de pantalla
    int pantallaX = (int) ((centroX - panel.getCamaraX()) * NivelPanel.ZOOM);
    int pantallaY = (int) ((centroY - panel.getCamaraY()) * NivelPanel.ZOOM);
    
    //ahora dibujaria la luz, el aura al rededor del jugador, suavecita
//podemos ajustar radioAura si queremos que el resplandor del pj alcance mas o menos distancia
    float radioAura = (float) (160 * NivelPanel.ZOOM); 
    Point2D centro = new Point2D.Float(pantallaX, pantallaY);
    float[] distribucion = {0.0f, 0.4f, 1.0f};

    Color[] colores = {
            new Color(255, 240, 180, 120), // Centro: Amarillo/Cálido suave
            new Color(255, 230, 150, 50),  // Intermedio: Luz tenue
            new Color(0, 0, 0, 0)          // Borde: Totalmente transparente
        };

        RadialGradientPaint auraGradiente = new RadialGradientPaint(centro, radioAura, distribucion,colores);

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










