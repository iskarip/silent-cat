package Modelo;

public class Item implements Interactuable, Posicionable {

    protected String nombre;
    protected boolean recogido;
    protected int posicionX;
    protected int posicionY;
    protected String rutaImagen;

    // Constructor básico sin posición
    public Item(String nombre, String rutaImagen) {
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
        this.recogido = false;
    }

    // Constructor que posiciona directamente por Tile (columna, fila)
    public Item(String nombre, String rutaImagen, int columna, int fila) {
        this(nombre, rutaImagen);
        colocarEnTile(columna, fila); // Asigna posicionX y posicionY multiplicando por TILE
    }

    // -- GETTERS Y SETTERS --
    public String getNombre() { return nombre; }
    public boolean isRecogido() { return recogido; }
    public void setRecogido(boolean valor) { this.recogido = valor; }

    @Override public int getPosicionX() { return posicionX; }
    @Override public void setPosicionX(int posicionX) { this.posicionX = posicionX; }

    @Override public int getPosicionY() { return posicionY; }
    @Override public void setPosicionY(int posicionY) { this.posicionY = posicionY; }

    public String getRutaImagen() { return rutaImagen; }

    // -- MÉTODOS --
    public void recoger() {
        this.recogido = true;
    }

    @Override
    public void interactuar(Personaje p) {
        recoger();
        p.getInventario().agregarItem(this);
    }
}