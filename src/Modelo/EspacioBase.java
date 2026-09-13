package Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EspacioBase implements EspacioJugable {
    
    protected MapaColision mapaColision;
    protected List<Item> listaItems; // cada habitacion puede tener varios items, pero por ahora solo vamos a usar uno

    public EspacioBase( String rutaGrid) {
        this.mapaColision = new MapaColision();
        this.listaItems = new ArrayList<>();

        this.mapaColision = new MapaColision();
        this.mapaColision.cargar(rutaGrid);
    }

    public void agregarItem(Item i) {
        if (i != null) {
            this.listaItems.add(i);
        }
    }

    @Override
    public MapaColision getMapaColision() {
        return mapaColision;
    }

    @Override
    public List<Item> getListaItems() {
        return listaItems;
    }

}
