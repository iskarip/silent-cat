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

        int velocidadBase = 4;
        int velocidad = (int) (velocidadBase * personaje.getMultiplicadorVelocidad());

        // Lectura del teclado manteniendo la lógica de direcciones
        if (teclado.estaPresionada(KeyEvent.VK_UP) || teclado.estaPresionada(KeyEvent.VK_W)) { 
            deltaY -= velocidad; 
            vista.setDireccion(Direccion.ARRIBA); 
        }
        if (teclado.estaPresionada(KeyEvent.VK_DOWN) || teclado.estaPresionada(KeyEvent.VK_S)) { 
            deltaY += velocidad; 
            vista.setDireccion(Direccion.ABAJO); 
        }
        if (teclado.estaPresionada(KeyEvent.VK_LEFT) || teclado.estaPresionada(KeyEvent.VK_A)) { 
            deltaX -= velocidad; 
            vista.setDireccion(Direccion.IZQUIERDA); 
        }
        if (teclado.estaPresionada(KeyEvent.VK_RIGHT) || teclado.estaPresionada(KeyEvent.VK_D)) { 
            deltaX += velocidad; 
            vista.setDireccion(Direccion.DERECHA); 
        }

        boolean seEstaMoviendo = (deltaX != 0 || deltaY != 0);

        // Actualización visual de estados y animaciones
        vista.setEstado(seEstaMoviendo ? EstadoPersonaje.CAMINANDO : EstadoPersonaje.IDLE);
        vista.actualizarAnimacion(seEstaMoviendo);

        // Desplazamiento si hubo intención de movimiento
        if (seEstaMoviendo) {
            moverConLimites(personaje, mapa, vista, deltaX, deltaY);
        }
    }

    private void moverConLimites(Personaje personaje, MapaColision mapa, NivelPanel vista, int deltaX, int deltaY) {
        int nuevoX = personaje.getPosicionX();
        int nuevoY = personaje.getPosicionY();

        // Avance y validación en eje X
        int intentoX = nuevoX + deltaX;
        Rectangle hbX = vista.getHitbox(intentoX, nuevoY);
        if (mapa == null || mapa.esRectanguloValido(hbX.x, hbX.y, hbX.width, hbX.height)) {
            personaje.setPosicionX(intentoX);
        }

        // Avance y validación en eje Y
        int intentoY = nuevoY + deltaY;
        Rectangle hbY = vista.getHitbox(personaje.getPosicionX(), intentoY);
        if (mapa == null || mapa.esRectanguloValido(hbY.x, hbY.y, hbY.width, hbY.height)) {
            personaje.setPosicionY(intentoY);
        }
    }

    }
