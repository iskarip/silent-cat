package Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EspacioBase implements EspacioJugable {
    
    protected MapaColision mapaColision;
    protected String rutaImagenFondo;
    protected List<Item> listaItems; // cada habitacion puede tener varios items, pero por ahora solo vamos a usar uno

    public EspacioBase( String rutaGrid, String rutaImagenFondo) {

        this.rutaImagenFondo = rutaImagenFondo;
        this.listaItems = new ArrayList<>();

        this.mapaColision = new MapaColision();
        this.mapaColision.cargar(rutaGrid);
        
    }

    public String getRutaImagenFondo() {
        return rutaImagenFondo;
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
