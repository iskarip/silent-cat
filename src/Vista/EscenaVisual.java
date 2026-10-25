package Vista;

import java.util.List;

// Lo que NivelPanel necesita saber del espacio que se está mostrando (un nivel o una habitación).
// Solo expone lo que hay que dibujar: ya viene filtrado (ítems sin recoger, entidades vivas).
public interface EscenaVisual {

    String getRutaImagenFondo();
    int getAnchoPx();                 // tamaño total del mapa en píxeles
    int getAltoPx();
    int getTamanioTile();             // lado de cada tile en píxeles
    List<ItemVisual> getItems();      // ítems que siguen en el piso
    List<EntidadVisual> getEntidades(); // entidades vivas
}
 