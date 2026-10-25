package Modelo;

// Item que se recoge como cualquier otro, pero que además tiene una
// imagen grande para leerla en pantalla desde el inventario.
public class ItemPista extends Item {

    private final String rutaImagenGrande;

    // rutaImagen (heredada) = icono chico, el del piso y el del slot
    public ItemPista(String nombre, String rutaImagen, String rutaImagenGrande, int columna, int fila) {
        super(nombre, rutaImagen, columna, fila);
        this.rutaImagenGrande = rutaImagenGrande;
    }

    @Override
    public boolean esLegible() {
        return true;
    }

    @Override
    public String getRutaImagenGrande() {
        return rutaImagenGrande;
    }
}