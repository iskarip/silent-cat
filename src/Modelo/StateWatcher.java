package Modelo;

//patron state. cada estado del enemigo watcher es una clase que decide
//como se comporta, el enemigo solo delega(polimorfismo)

public interface StateWatcher {
    
    //que hace el enemigo en cada tick mientras esta en este estado.
    void actualizar(EnemigoWatcher watcher, Personaje jugador, MapaColision mapa);

    boolean puedeAtacar();
    
    PoseEntidadCombatible getPostura();

    default void alEntrar() { }// Se llama una vez al entrar al estado (por defecto no hace nada)

    default double getProgreso() { return 0; } // Avance 0.0 a 1.0 de las transiciones (por defecto 0).
    


}
