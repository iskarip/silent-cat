package Controlador;

import Modelo.Item;
import Vista.ItemVisual;

// Adapta un Item del Modelo al contrato ItemVisual que pide la Vista.
public class ControladorItemVisual implements ItemVisual {

    private final Item item;

    public ControladorItemVisual(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("El item es obligatorio");
        }
        this.item = item;
    }

    @Override public int getX() { return item.getPosicionX(); }
    @Override public int getY() { return item.getPosicionY(); }
    @Override public String getRutaImagen() { return item.getRutaImagen(); }
    @Override public double getEscala() { return item.getEscala(); }
}
 