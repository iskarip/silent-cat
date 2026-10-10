package Controlador;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

// Traduce el estado del mando a teclas, para que el resto del juego no sepa que existe.
public class ControladorMando {

    private static final float ZONA_MUERTA = 0.5f;

    // ---- MAPEO: es lo único que hay que tocar si un botón no coincide ----
    // La clave es el id que imprime MapeoMando ("0", "1", "2"... para botones; "x", "y" para ejes).
    // Pulso: se dispara UNA vez al apretar el botón.
    private static final Map<String, Integer> PULSOS = new LinkedHashMap<>();
    // Mantenido: la tecla queda apretada mientras el botón esté apretado.
    private static final Map<String, Integer> MANTENIDOS = new LinkedHashMap<>();

    static {
        PULSOS.put("2", KeyEvent.VK_E);      // interactuar
        PULSOS.put("3", KeyEvent.VK_SPACE);  // atacar
        PULSOS.put("1", KeyEvent.VK_L);      // linterna
        PULSOS.put("0", KeyEvent.VK_TAB);    // inventario
        MANTENIDOS.put("7", KeyEvent.VK_SHIFT); // correr
    }

    private final ControladorTeclado teclado;
    private final Map<String, Boolean> botonAnterior = new HashMap<>();
    private Controller mando;

    public ControladorMando(ControladorTeclado teclado) {
        this.teclado = teclado;
        try {
            for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
                if (c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK) {
                    mando = c;
                    System.out.println("Mando conectado: " + c.getName());
                    break;
                }
            }
        } catch (Throwable t) {
            System.out.println("No se pudo iniciar el mando: " + t);
        }
    }

    // Se llama una vez por tick
    public void actualizar() {
        if (mando == null) return;

        if (!mando.poll()) { // el mando se desconectó
            soltarTodo();
            mando = null;
            return;
        }

        boolean izquierda = false, derecha = false, arriba = false, abajo = false;

        for (Component c : mando.getComponents()) {
            String id = c.getIdentifier().getName();
            float valor = c.getPollData();

            if (id.equals("x")) {
                izquierda = valor < -ZONA_MUERTA;
                derecha = valor > ZONA_MUERTA;
            } else if (id.equals("y")) {
                arriba = valor < -ZONA_MUERTA;
                abajo = valor > ZONA_MUERTA;
            } else {
                boolean apretado = valor > 0.5f;

                Integer teclaPulso = PULSOS.get(id);
                if (teclaPulso != null) {
                    boolean estabaApretado = botonAnterior.getOrDefault(id, false);
                    if (apretado && !estabaApretado) { // flanco: recién se apretó
                        teclado.ejecutarAccion(teclaPulso);
                    }
                    botonAnterior.put(id, apretado);
                }

                Integer teclaMantenida = MANTENIDOS.get(id);
                if (teclaMantenida != null) {
                    teclado.setTeclaMando(teclaMantenida, apretado);
                }
            }
        }

        teclado.setTeclaMando(KeyEvent.VK_LEFT, izquierda);
        teclado.setTeclaMando(KeyEvent.VK_RIGHT, derecha);
        teclado.setTeclaMando(KeyEvent.VK_UP, arriba);
        teclado.setTeclaMando(KeyEvent.VK_DOWN, abajo);
    }

    private void soltarTodo() {
        teclado.setTeclaMando(KeyEvent.VK_LEFT, false);
        teclado.setTeclaMando(KeyEvent.VK_RIGHT, false);
        teclado.setTeclaMando(KeyEvent.VK_UP, false);
        teclado.setTeclaMando(KeyEvent.VK_DOWN, false);
        for (int tecla : MANTENIDOS.values()) {
            teclado.setTeclaMando(tecla, false);
        }
    }
}