package Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EspacioBase implements EspacioJugable {
    
    int posicionInicialX;
    int posicionInicialY;
    protected MapaColision mapaColision;
    protected String rutaImagenFondo;
    protected List<Item> listaItems; // cada habitacion puede tener varios items, pero por ahora solo vamos a usar uno

    public EspacioBase( String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY) {

        this.rutaImagenFondo = rutaImagenFondo;
        this.listaItems = new ArrayList<>();
        this.posicionInicialX = posicionInicialX;
        this.posicionInicialY = posicionInicialY;

        this.mapaColision = new MapaColision();
        this.mapaColision.cargar(rutaGrid);
        
    }

    public int getPosicionInicialX() { return posicionInicialX; }

    public void setPosicionInicialY(int posicionInicialY) {
        this.posicionInicialY = posicionInicialY;
    }

    public int getPosicionInicialY() { return posicionInicialY; }

    public void setPosicionInicialX(int posicionInicialX) {
        this.posicionInicialX = posicionInicialX;
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
