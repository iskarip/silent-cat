package Modelo;


public class EnemigoComun extends Enemigo {
    
 
    private static final int BONUS_DETECCION_CON_LINTERNA = 40;
 
    public EnemigoComun(int puntosVida, int danioBase, int idEnemigo,
                        int columna, int fila, String rutaSprites) {
        super(puntosVida, danioBase, idEnemigo, columna, fila, rutaSprites);
    }
 
    @Override
    public boolean detectaAlJugador(Personaje jugador) {
        if (jugador.isInvulnerableRespawn()) return false;
 
        int radio = getRadioDeteccion();
        if (jugador.linternaEncendida()) radio += BONUS_DETECCION_CON_LINTERNA;
        return jugadorDentroDeRadio(jugador, radio);
    }
 
    @Override
    protected boolean puedeAtacar(Personaje jugador) {
        return !jugador.isInvulnerableRespawn();
    }
}
 
