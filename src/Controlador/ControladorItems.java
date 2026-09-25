package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Item;
import Modelo.MapaColision;
import java.awt.Rectangle;
import java.util.List;

public class ControladorItems {
    
    // Reducimos el radio para que la zona de recogida no sea tan gigante
    private static final int RADIO_RECOGIDA = 6; 

    public void actualizar(Nivel nivelActual, Personaje personaje) {
        if (nivelActual == null || personaje == null) return;
    
        List<Item> items = nivelActual.getListaItems();
        if (items == null) return;

        Rectangle hitboxJugador = personaje.getHitbox();

        for (Item item : items) {
            if (item.isRecogido()) continue;
    
            // Centro exacto del tile
            int centroX = item.getPosicionX() + (MapaColision.TILE / 2);
            int centroY = item.getPosicionY() + (MapaColision.TILE / 2);
    
            // Hitbox más pequeña (16x16 píxeles)
            Rectangle hitboxItem = new Rectangle(
                centroX - RADIO_RECOGIDA, 
                centroY - RADIO_RECOGIDA, 
                RADIO_RECOGIDA * 2, 
                RADIO_RECOGIDA * 2
            );
    
            if (hitboxJugador.intersects(hitboxItem)) {
                personaje.interactuarCon(item);
            }
        }
    }
}