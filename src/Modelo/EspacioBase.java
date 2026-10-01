package Modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EspacioBase implements EspacioJugable {
    
    private int posicionInicialX;
    private int posicionInicialY;
    protected String rutaSpritesEnemigos; // NUEVO
    protected MapaColision mapaColision;
    protected String rutaImagenFondo;
    protected List<Item> listaItems;
    protected List<Enemigo> listaEnemigos; // NUEVO
    protected List<EntidadPasiva> listaEntidadPasivas; //nuevo a implementar owo


    public EspacioBase(String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY) {
        this.rutaImagenFondo = rutaImagenFondo;
        this.listaItems = new ArrayList<>();
        this.listaEnemigos = new ArrayList<>(); // NUEVO
        this.listaEntidadPasivas = new ArrayList<>(); //nuevo owo
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

    public void agregarEnemigo(Enemigo e) { // NUEVO (antes vivía en Nivel)
        if (e != null) {
            this.listaEnemigos.add(e);
        }
    }
    //al igual que agregar enemigo voy a pdoer agregar entidades pasivas sin immportar el tipo
    public void agregarEntidadPasiva (EntidadPasiva e){
        if (e!= null){
            this.listaEntidadPasivas.add(e);
        }
    }


    
    public void setRutaSpritesEnemigos(String ruta) { // NUEVO
        this.rutaSpritesEnemigos = ruta;
    }

    @Override
    public String getRutaSpritesEnemigos() { // NUEVO
        return rutaSpritesEnemigos;
    }

    @Override
    public MapaColision getMapaColision() {
        return mapaColision;
    }

    @Override
    public List<Item> getListaItems() {
        return listaItems;
    }

    @Override
    public List<Enemigo> getListaEnemigos() { // NUEVO
        return listaEnemigos;
    }

    @Override 
    public List<EntidadPasiva> getListaEntidadesPasivas(){
        return listaEntidadPasivas;
    }

}
