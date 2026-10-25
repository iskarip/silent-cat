package Controlador;

import Modelo.Item;
import Modelo.ItemPista;
import Modelo.Personaje;
import Vista.PanelPista;
import java.util.List;

// Maneja la lectura de pistas: busca el item en el inventario y le pide a la vista
// que lo muestre en grande. No conoce teclas: quien lo llame decide cuándo.
public class ControladorPista {

    private final PanelPista vista;

    public ControladorPista(PanelPista vista) {
        this.vista = vista;
    }

    // El game loop lo consulta para pausar la actualización mientras se lee
    public boolean estaActivo() {
        return vista.isVisible();
    }

    // Abre la pista del slot indicado (0 a 4). Devuelve true si había una pista para leer.
    public boolean leerSlot(Personaje personaje, int indice) {
        if (estaActivo() || personaje == null) return false;

        List<Item> items = personaje.getInventario().getItems();
        if (indice < 0 || indice >= items.size()) return false;

        Item item = items.get(indice);
        if (!item.esLegible()) return false;   
        // el slot tiene otro tipo de item (medicina, llave...)

        vista.abrir(item.getRutaImagenGrande());
        return true;
    }

    public void cerrar() {
        vista.cerrar();
    }
}