package Modelo;

//la transicion de levantarse. no se mueve ni hace daño hasta terminar

public class WatcherLevantandose implements StateWatcher {

    private static final int DURACION_TICKS = 75;   // bucle de 16 ms: ~1,2 segundos
    private int ticks;

    @Override
    public void alEntrar() {
        ticks = 0;   // cada vez que se levanta, arranca de cero
    }

    @Override
    public void actualizar(EnemigoWatcher watcher, Personaje jugador, MapaColision mapa) {
        ticks++;
        if (ticks >= DURACION_TICKS) {
            watcher.terminarDeLevantarse();
        }
    }

    @Override
    public boolean puedeAtacar() { return false; }

    @Override
    public PoseEntidadCombatible getPostura() { return PoseEntidadCombatible.LEVANTANDOSE; }

    @Override
    public double getProgreso() { return (double) ticks / DURACION_TICKS; }
}