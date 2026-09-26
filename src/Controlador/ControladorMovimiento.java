package Controlador;

import Modelo.MapaColision;
import Modelo.Personaje;
import Modelo.Direccion;
import Vista.EstadoPersonaje;
import Vista.NivelPanel;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class ControladorMovimiento {

    public void procesarMovimientoJugador(Personaje personaje, MapaColision mapa, ControladorTeclado teclado, NivelPanel vista) {
        if (personaje == null) return;

        int deltaX = 0;
        int deltaY = 0;

        int velocidadBase = 3;
        int velocidad = (int) (velocidadBase * personaje.getMultiplicadorVelocidad());

        // Lectura de teclas activas
        if (teclado.estaPresionada(KeyEvent.VK_UP) || teclado.estaPresionada(KeyEvent.VK_W)) { 
            deltaY -= velocidad; 
            personaje.setDireccion(Direccion.ARRIBA);
        }
        if (teclado.estaPresionada(KeyEvent.VK_DOWN) || teclado.estaPresionada(KeyEvent.VK_S)) { 
            deltaY += velocidad; 
            personaje.setDireccion(Direccion.ABAJO);
        }
        if (teclado.estaPresionada(KeyEvent.VK_LEFT) || teclado.estaPresionada(KeyEvent.VK_A)) { 
            deltaX -= velocidad; 
            personaje.setDireccion(Direccion.IZQUIERDA);
        }
        if (teclado.estaPresionada(KeyEvent.VK_RIGHT) || teclado.estaPresionada(KeyEvent.VK_D)) { 
            deltaX += velocidad; 
            personaje.setDireccion(Direccion.DERECHA);
        }

        boolean seEstaMoviendo = (deltaX != 0 || deltaY != 0);
        
        //por si se mueve en diagonal (dos teclas presionadas a la vez)
        //debemos reescalar el vecor para que la velocidad total sea siempre la misma
        //sin importar en cuantos ejes se mueva a la vez.
        
        if (deltaX != 0 && deltaY !=0){
            deltaX = (int) Math.round(deltaX * 0.7071); 
            deltaY = (int) Math.round(deltaY * 0.7071);
        }


        
        // Asignación explícita del estado
        if (!vista.estaAtacando()) {
            if (seEstaMoviendo) {
                vista.setEstado(EstadoPersonaje.CAMINANDO);
                moverConLimites(personaje, mapa, vista, deltaX, deltaY);
            } else {
                vista.setEstado(EstadoPersonaje.IDLE);
            }
        } else if (seEstaMoviendo) {
            moverConLimites(personaje, mapa, vista, deltaX, deltaY);
        }

        vista.actualizarAnimacion(seEstaMoviendo);
    }

    private void moverConLimites(Personaje personaje, MapaColision mapa, NivelPanel vista, int deltaX, int deltaY) {
        int nuevoX = personaje.getPosicionX();
        int nuevoY = personaje.getPosicionY();

        // Avance en X
        int intentoX = nuevoX + deltaX;
        Rectangle hbX = personaje.getHitboxEnPosicion(intentoX, nuevoY);
        if (mapa == null || mapa.esRectanguloValido(hbX.x, hbX.y, hbX.width, hbX.height)) {
            personaje.setPosicionX(intentoX);
        } 

        // Avance en Y
        int intentoY = nuevoY + deltaY;
        Rectangle hbY = personaje.getHitboxEnPosicion(personaje.getPosicionX(), intentoY);
        if (mapa == null || mapa.esRectanguloValido(hbY.x, hbY.y, hbY.width, hbY.height)) {
            personaje.setPosicionY(intentoY);
        }
    }
}