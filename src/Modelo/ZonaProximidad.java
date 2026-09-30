
package Modelo;

//va a ser abstracta porque siempre se va a usar coon un puntoFlashback o una zonamensaje, nunca solita

// Base de toda zona del mapa que reacciona UNA sola vez cuando el personaje se acerca.
// Esta clase decide CUÁNDO se activa (comprobar); cada hija decide QUÉ pasa (alActivar).
public abstract class ZonaProximidad {

    protected int x, y, radio;
    protected boolean activado = false;

    // Recibe columna, fila y radio en TILES (igual que los items) y los pasa a píxeles.
    public ZonaProximidad(int columna, int fila, int radioEnTiles) {
        this.x = columna * MapaColision.TILE;
        this.y = fila * MapaColision.TILE;
        this.radio = radioEnTiles * MapaColision.TILE;
    }

    // true si el personaje está dentro del radio de la zona.
    public boolean estaCerca(Personaje personaje) {
        int dx = personaje.getPosicionX() - x;
        int dy = personaje.getPosicionY() - y;
        return Math.sqrt((double) dx * dx + (double) dy * dy) <= radio;
    }

    public boolean getActivado() { return activado; }

    // Se llama en cada tick. Si el personaje está cerca y la zona todavía no se
    // activó, la marca como activada y ejecuta su efecto. Devuelve true solo esa vez.
    public boolean comprobar(Personaje personaje, ObservadorAmbiente observador) {
        if (activado || !estaCerca(personaje)) return false;
        activado = true;
        alActivar(personaje, observador);
        return true;
    }

    // Efecto propio de cada zona. Puede modificar al personaje (modelo) y/o
    // pedirle al observador que muestre algo (vista), sin conocer la vista.
    protected abstract void alActivar(Personaje personaje, ObservadorAmbiente observador);
}
