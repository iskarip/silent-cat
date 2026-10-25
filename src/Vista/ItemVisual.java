package Vista;

// Lo que NivelPanel necesita saber de un ítem que está en el piso para dibujarlo.
public interface ItemVisual {

    int getX();               // posición en píxeles del mapa (esquina del tile)
    int getY();
    String getRutaImagen();
    double getEscala();       // proporción del tile que ocupa la imagen
}
 