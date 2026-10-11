package Controlador;

import Modelo.Personaje;
import Modelo.Item;
import Modelo.ObservadorAmbiente;
import java.awt.Rectangle;
import java.util.List;
import Modelo.EspacioJugable;

public class ControladorItems {

  


    public void actualizar(EspacioJugable espacio, Personaje personaje, ObservadorAmbiente observador) {
        if (espacio == null || personaje == null) return;

        List<Item> items = espacio.getListaItems();
        if (items == null) return;

        Rectangle hitboxJugador = personaje.getHitbox();

        for (Item item : items) {
            if (item.estaAlAlcance(hitboxJugador)) {
                personaje.interactuarCon(item);
                if (item.isRecogido() && item.getMensajeRecoger() != null) {
                    observador.mostrarMensaje(item.getMensajeRecoger());
                }
            }
        }
    }
}