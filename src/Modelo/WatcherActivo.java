package Modelo;

//perseguiria si ve la linterna, sino solo patrulla

public class WatcherActivo implements StateWatcher {
 
    @Override
    public void actualizar(EnemigoWatcher watcher, Personaje jugador, MapaColision mapa) {
        watcher.comportamientoActivo(jugador, mapa);
    }
 
    @Override
    public boolean puedeAtacar() { return true; }
 
    @Override
    public PoseEntidadCombatible getPostura() { return PoseEntidadCombatible.NORMAL; }
    
    @Override
    public int getNivelAmenaza() { return 3; }   // despierto y persiguiendo seria maximo peligro

}
