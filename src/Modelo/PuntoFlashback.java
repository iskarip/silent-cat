package Modelo;

//para que cuando el personaje se acerque a un lugar del mapa, se active el flashback, solo cuando
//pasa una vez, asi no se repite cuando pase de nuevo
//hereda de zona proximidadd

public class PuntoFlashback extends ZonaProximidad {
    
    public PuntoFlashback(int columna, int fila, int radioEnTiles){
        super(columna, fila, radioEnTiles);
    }
}