package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Enemigo;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;

import java.awt.Rectangle;
import javax.swing.Timer;

public class ControladorCombate {

    private Timer temporizadorAtaque;

    // Se llama desde ControladorNivel cuando el jugador aprieta la tecla de ataque.
    public void ejecutarAtaque(Personaje personaje, Nivel nivelActual, NivelPanel vista) {
        if (personaje == null || nivelActual == null) return;

        // TODO 1: chequear personaje.puedeAtacar() — si no puede, return acá y listo
        //         (nada de animación ni de daño mientras esté en cooldown)

        if (temporizadorAtaque != null && temporizadorAtaque.isRunning()) {
            return;
        }

        // TODO 2: registrar el uso del arma (personaje.registrarAtaque())
        //         ¿antes o después de calcular el hitbox? pensalo en términos de
        //         "una vez por swing, no una vez por enemigo golpeado"

        vista.setEstado(EstadoPersonaje.ATACANDO);

        // TODO 3: calcular el hitbox de ataque con vista.getHitboxAtaque(...)
        //         y recorrer nivelActual.getListaEnemigos(), igual que antes,
        //         pero delegando el daño en personaje.atacar(e)

        Rectangle hitboxGolpe = personaje.getHitboxAtaque();
        
        // TODO 4: Evaluar la colision contra cada enemigo vivo

        if (nivelActual.getListaEnemigos() != null) {
            for (Enemigo enemigo : nivelActual.getListaEnemigos()) {
                if (enemigo.estaVivo()) {
                    Rectangle cuerpoEnemigo = enemigo.getHitbox();
                    if (hitboxGolpe.intersects(cuerpoEnemigo)) {
                        // Delegación polimórfica: el personaje ataca usando su arma
                        personaje.atacar(enemigo);
                    }
                }
            }
        }

        // TODO 5: Temporizador con lambda para volver al estado IDLE

        iniciarTimerVueltaAIdle(vista);
    }

    private void iniciarTimerVueltaAIdle(NivelPanel vista) {
        temporizadorAtaque = new Timer(350, e -> vista.setEstado(EstadoPersonaje.IDLE));
        temporizadorAtaque.setRepeats(false);
        temporizadorAtaque.start();
    }
}