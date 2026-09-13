package Modelo;

import java.util.List;

// Esta interfaz define un contrato para cualquier clase que represente un espacio jugable en el juego. 
// Cualquier clase que implemente esta interfaz debe proporcionar una implementación del método 
// getMapaColision(), que devuelve un objeto de tipo MapaColision. 
// Esto permite que diferentes espacios jugables, como niveles o habitaciones, 
// puedan ser tratados de manera uniforme en el código del juego, 
// facilitando la interacción con el mapa de colisión correspondiente a cada espacio.

public interface EspacioJugable {

    MapaColision getMapaColision(); // Este método debe ser implementado por cualquier clase que represente un espacio jugable,
    // y debe devolver el mapa de colisión asociado a ese espacio.
    List<Item> getListaItems(); // Este método debe ser implementado por cualquier clase que represente un espacio jugable,
    // y debe devolver la lista de items presentes en ese espacio.
    
}