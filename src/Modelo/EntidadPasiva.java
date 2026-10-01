package Modelo;

public abstract class EntidadPasiva extends Entidad {

    // Cada pasiva define cómo se mueve (el gato camina hacia el personaje, la rata deambula, etc.)
    public abstract void moverse(Personaje objetivo, MapaColision mapa);

    // El controlador solo llama a actualizar(); en las pasivas eso es moverse().
    @Override
    public void actualizar(Personaje jugador, MapaColision mapa) {
        moverse(jugador, mapa);
    }
}