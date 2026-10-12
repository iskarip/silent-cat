package Controlador;

// Momentos del juego en los que un componente puede necesitar apagarse o prenderse.
// Cada componente implementa solo los que le importan.
public interface CicloDeVida {

    default void alPausar() { }
    default void alReanudar() { }
    default void alDetener() { }
    
}