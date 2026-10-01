package Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EspacioBase implements EspacioJugable {

    private int posicionInicialX;
    private int posicionInicialY;
    protected MapaColision mapaColision;
    protected String rutaImagenFondo;
    protected List<Item> listaItems;
    protected List<Entidad> listaEntidades;

    public EspacioBase(String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY) {
        this.rutaImagenFondo = rutaImagenFondo;
        this.listaItems = new ArrayList<>();
        this.listaEntidades = new ArrayList<>();
        this.posicionInicialX = posicionInicialX;
        this.posicionInicialY = posicionInicialY;

        this.mapaColision = new MapaColision();
        this.mapaColision.cargar(rutaGrid);
    }

    public int getPosicionInicialX() { return posicionInicialX; }
    public int getPosicionInicialY() { return posicionInicialY; }
    public void setPosicionInicialX(int posicionInicialX) { this.posicionInicialX = posicionInicialX; }
    public void setPosicionInicialY(int posicionInicialY) { this.posicionInicialY = posicionInicialY; }

    public void agregarItem(Item i) {
        if (i != null) {
            this.listaItems.add(i);
        }
    }

    public void agregarEntidad(Entidad e) {
        if (e != null) {
            this.listaEntidades.add(e);
        }
    }

    @Override
    public String getRutaImagenFondo() { return rutaImagenFondo; }

    @Override
    public MapaColision getMapaColision() { return mapaColision; }

    @Override
    public List<Item> getListaItems() { return listaItems; }

    @Override
    public List<Entidad> getListaEntidades() { return listaEntidades; }
}