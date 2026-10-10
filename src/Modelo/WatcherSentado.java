package Modelo;
//sentado contra la pared, se despierta si detecta la loz

public class WatcherSentado implements StateWatcher {
    
  @Override
    public void actualizar(EnemigoWatcher watcher, Personaje jugador, MapaColision mapa) {
        // detectaAlJugador ya exige linterna encendida y estar dentro del radio
        if (watcher.detectaAlJugador(jugador)) {
            watcher.despertar();
        }
    }
 
    @Override
    public boolean puedeAtacar() { return false; }
 
    @Override
    public PoseEntidadCombatible getPostura() { return PoseEntidadCombatible.SENTADO; }  
}
