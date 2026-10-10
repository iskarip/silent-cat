package Controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ControladorTeclado extends KeyAdapter {

    private final Set<Integer> teclasPresionadas = new HashSet<>();
    // Teclas "virtuales" que mantiene apretadas el mando (separadas del teclado físico)
    private final Set<Integer> teclasMando = new HashSet<>();

    private Runnable accionAtaque;
    private Runnable accionDebugFlashback;
    private Runnable accionLinterna;
    private Runnable accionInventario;
    private Runnable accionInteraccionar;

    public void setAccionAtaque(Runnable accionAtaque) {
        this.accionAtaque = accionAtaque;
    }

    public void setAccionDebugFlashback(Runnable accionDebugFlashback) {
        this.accionDebugFlashback = accionDebugFlashback;
    }

    public void setAccionLinterna(Runnable accionLinterna){
        this.accionLinterna = accionLinterna;
    }

    public void setAccionInventario(Runnable accionInventario) {
        this.accionInventario = accionInventario;
    }

    public void setAccionInteraccionar(Runnable accionInteraccionar) {
        this.accionInteraccionar = accionInteraccionar;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        teclasPresionadas.add(e.getKeyCode());
        ejecutarAccion(e.getKeyCode());
    }

    // Acciones de pulso único. Las usan el teclado (keyPressed) y el mando.
    public void ejecutarAccion(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE && accionAtaque != null) {
            accionAtaque.run();
        }
        if (keyCode == KeyEvent.VK_F && accionDebugFlashback != null) {
            accionDebugFlashback.run();
        }
        if (keyCode == KeyEvent.VK_L && accionLinterna != null) {
            accionLinterna.run();
        }
        if (keyCode == KeyEvent.VK_TAB && accionInventario != null) {
            accionInventario.run();
        }
        if (keyCode == KeyEvent.VK_E && accionInteraccionar != null) {
            accionInteraccionar.run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        teclasPresionadas.remove(e.getKeyCode());
    }

    // El mando marca o suelta una tecla virtual
    public void setTeclaMando(int keyCode, boolean presionada) {
        if (presionada) {
            teclasMando.add(keyCode);
        } else {
            teclasMando.remove(keyCode);
        }
    }

    public boolean estaPresionada(int keyCode) {
        return teclasPresionadas.contains(keyCode) || teclasMando.contains(keyCode);
    }

    public void limpiarTeclas() {
        teclasPresionadas.clear();
        teclasMando.clear();
    }
}