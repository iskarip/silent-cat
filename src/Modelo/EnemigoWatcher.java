package Modelo;
 
/**
 * Enemigo que arranca sentado contra la pared. Al detectar la linterna se
 * levanta y recien ahi persigue y ataca (solo con la linterna encendida).
 * Hereda de EnemigoFotosensible: reutiliza la regla de la linterna.
 * Su comportamiento por fases lo delega en objetos EstadoWatcher (patron State).
 */
public class EnemigoWatcher extends EnemigoFotosensible {
 
    // Los estados son del mismo objeto: se crean una vez y se reutilizan
    private final StateWatcher sentado = new WatcherSentado();
    private final StateWatcher levantandose = new WatcherLevantandose();
    private final StateWatcher activo = new WatcherActivo();
 
    private StateWatcher estado = sentado;
 
    public EnemigoWatcher(int puntosVida, int danioBase, int idEnemigo,int columna, int fila, String rutaSprites) {
        super(puntosVida, danioBase, idEnemigo, columna, fila, rutaSprites);
    }
 
    // -- Transiciones: solo las usan los estados (visibilidad de paquete).
    //    Los estados piden "despertar", no eligen a que clase concreta pasar. --
 
    void despertar() {
        cambiarA(levantandose);
    }
 
    void terminarDeLevantarse() {
        cambiarA(activo);
    }
 
    void comportamientoActivo(Personaje jugador, MapaColision mapa) {
        super.actualizarComportamiento(jugador, mapa);   // persecucion / patrulla de Enemigo
    }
 
    private void cambiarA(StateWatcher nuevo) {
        estado = nuevo;
        estado.alEntrar();
    }
 
    // -- Todo se delega en el estado actual (polimorfismo) --
 
    @Override
    public void actualizarComportamiento(Personaje jugador, MapaColision mapa) {
        estado.actualizar(this, jugador, mapa);
    }
 
    @Override
    protected boolean puedeAtacar(Personaje jugador) {
        return estado.puedeAtacar() && super.puedeAtacar(jugador);
    }
 
    // -- Lo que la Vista consulta (a traves de Entidad) --
 
    @Override
    public PoseEntidadCombatible getPostura() { return estado.getPostura(); }
 
    @Override
    public double getProgresoPostura() { return estado.getProgreso(); }
}
 