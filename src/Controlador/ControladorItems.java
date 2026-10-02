package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Item;
import java.awt.Rectangle;
import java.util.List;

public class ControladorItems {

    public void actualizar(Nivel nivelActual, Personaje personaje) {
        if (nivelActual == null || personaje == null) return;

        List<Item> items = nivelActual.getListaItems();
        if (items == null) return;

        Rectangle hitboxJugador = personaje.getHitbox();

        for (Item item : items) {
            if (item.estaAlAlcance(hitboxJugador)) {
                personaje.interactuarCon(item);
            }
        }
    }
}