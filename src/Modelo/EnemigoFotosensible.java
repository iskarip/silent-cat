package Modelo;

//este va a ser nuestro tipo de enemigo que te detecta y ataca si se tiene la linetrna encendida
    //hereda todo el mov y combate de la clase enemigo, solo define sus propias reglas.
public class EnemigoFotosensible extends Enemigo {
 
    public EnemigoFotosensible(int puntosVida, int danioBase, int idEnemigo, int columna, int fila, String rutaSprites) {
        super(puntosVida, danioBase, idEnemigo, columna, fila, rutaSprites);
    }
 
    @Override
    public boolean detectaAlJugador(Personaje jugador) {
        if (jugador.isInvulnerableRespawn() || !jugador.linternaEncendida()) return false;
        return jugadorDentroDeRadio(jugador, getRadioDeteccion());
    }
 
    @Override
    protected boolean puedeAtacar(Personaje jugador) {
        return jugador.linternaEncendida() && !jugador.isInvulnerableRespawn();
    }
}
