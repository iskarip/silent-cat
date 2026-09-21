package Controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ControladorTeclado extends KeyAdapter {

    // Se conserva el Set de teclas activas exactamente igual a como lo programaron
    private final Set<Integer> teclasPresionadas = new HashSet<>();

    // Acciones para teclas de pulso único (Espacio para atacar, F para debug)
    private Runnable accionAtaque;
    private Runnable accionDebugFlashback;

    public void setAccionAtaque(Runnable accionAtaque) {
        this.accionAtaque = accionAtaque;
    }

    public void setAccionDebugFlashback(Runnable accionDebugFlashback) {
        this.accionDebugFlashback = accionDebugFlashback;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        teclasPresionadas.add(e.getKeyCode());

        // Mantiene la tecla Espacio para ejecutar el ataque
        if (e.getKeyCode() == KeyEvent.VK_SPACE && accionAtaque != null) {
            accionAtaque.run();
        }

        // Mantiene la tecla F para la prueba del flashback
        if (e.getKeyCode() == KeyEvent.VK_F && accionDebugFlashback != null) {
            accionDebugFlashback.run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPresionadas.remove(e.getKeyCode());
    }

    // Método de consulta para saber si una tecla sigue apretada durante el Game Loop
    public boolean estaPresionada(int keyCode) {
        return teclasPresionadas.contains(keyCode);
    }

    public void limpiarTeclas() {
        teclasPresionadas.clear();
    }
}