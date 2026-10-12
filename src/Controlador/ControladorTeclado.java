package Controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ControladorTeclado extends KeyAdapter implements CicloDeVida {

    private final Set<Integer> teclasPresionadas = new HashSet<>();
    // Teclas "virtuales" que mantiene apretadas el mando (separadas del teclado físico)
    private final Set<Integer> teclasMando = new HashSet<>();
    
    private java.util.function.IntConsumer accionSlot;

    private Runnable accionAtaque;
    private Runnable accionDebugFlashback;
    private Runnable accionLinterna;
    private Runnable accionInventario;
    private Runnable accionInteraccionar;
    private Runnable accionPausa;

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

    public void setAccionPausa(Runnable accionPausa) {
        this.accionPausa = accionPausa;
    }

    public void setAccionSlot(java.util.function.IntConsumer accionSlot) {
        this.accionSlot = accionSlot;
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
        if (keyCode == KeyEvent.VK_ESCAPE && accionPausa != null) {
            accionPausa.run();
        }
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_5 && accionSlot != null) {
            accionSlot.accept(keyCode - KeyEvent.VK_1); // 0 a 4
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

    @Override
    public void alPausar() {
        limpiarTeclas(); // Soltar las teclas atascadas al pausar
    }

    @Override
    public void alDetener() {
        limpiarTeclas();
    }
}