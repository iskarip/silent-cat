package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Item;
import Modelo.ObservadorAmbiente;
import java.awt.Rectangle;
import java.util.List;

public class ControladorItems {

    public void actualizar(Nivel nivelActual, Personaje personaje, ObservadorAmbiente observador) {
        if (nivelActual == null || personaje == null) return;

        List<Item> items = nivelActual.getListaItems();
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