package Vista;

import Modelo.Personaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class NivelPanel extends JPanel {

    private Personaje personaje;

    public NivelPanel() {
        configurarTeclas();
    }

    private void configurarTeclas() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "mover_arriba");
        actionMap.put("mover_arriba", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (personaje != null) {
                    personaje.mover(0, -5);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "mover_abajo");
        actionMap.put("mover_abajo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (personaje != null) {
                    personaje.mover(0, 5);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "mover_izquierda");
        actionMap.put("mover_izquierda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (personaje != null) {
                    personaje.mover(-5, 0);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "mover_derecha");
        actionMap.put("mover_derecha", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (personaje != null) {
                    personaje.mover(5, 0);
                    repaint();
                }
            }
        });
    }

    public void setPersonaje (Personaje personaje) {
        this.personaje = personaje;
        repaint(); // redibuja ahora que ya hay un personaje para mostrar
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(personaje != null) {
            g.setColor (Color.RED);
            g.fillOval(personaje.getPosicionX(), personaje.getPosicionY(), 30, 30);

        }
    }


}
