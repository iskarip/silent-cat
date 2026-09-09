package Vista;

import Modelo.Personaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class NivelPanel extends JPanel {

    private Personaje personaje;
    private Color colorPersonaje = Color.RED; // color por defecto
    private Set<Integer> teclasPresionadas = new HashSet<>();

    private Timer bucleDeJuego;

    public NivelPanel() {
        setFocusable(true);
        configurarTeclas();
        iniciarBucle();
    }

    private void configurarTeclas() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });

    }

    public void iniciarBucle () {
        // Timer de Swing: ejecuta el codigo de adentro cada 16mls
        bucleDeJuego = new Timer(16, e -> actualizarMovimiento());
        bucleDeJuego.start();
    }

    private void actualizarMovimiento() {
        if (personaje == null) return;

        int deltaX = 0;
        int deltaY = 0;

        if (teclasPresionadas.contains(KeyEvent.VK_UP)) deltaY -= 4;
        if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) deltaY += 4;
        if (teclasPresionadas.contains(KeyEvent.VK_LEFT)) deltaX -= 4;
        if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)) deltaX += 4;

        if (deltaX != 0 || deltaY != 0) {
            moverConLimites(deltaX, deltaY);
            repaint();
        }
    }

        // Mueve el personaje impidiendo salir del recuadro

        private void moverConLimites(int deltaX, int deltaY) {
            int nuevoX = personaje.getPosicionX() + deltaX;
            int nuevoY = personaje.getPosicionY() + deltaY;

            int diametro = 30; // mismo valor que usamos en fillOval

            // Clamp horizontal: no menos de 0, no más que el ancho del panel menos el círculo
            if (nuevoX < 0) nuevoX = 0;
            if (nuevoX > getWidth() - diametro) nuevoX = getWidth() - diametro;

            // Clamp vertical
            if (nuevoY < 0) nuevoY = 0;
            if (nuevoY > getHeight() - diametro) nuevoY = getHeight() - diametro;

            personaje.setPosicionX(nuevoX);
            personaje.setPosicionY(nuevoY);
        }


        public void setPersonaje (Personaje personaje) {
            this.personaje = personaje;
            repaint(); // redibuja ahora que ya hay un personaje para mostrar
        }

        public void setColorPersonaje (Color color ) {
        this.colorPersonaje = color;
        repaint();
        }



    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        if(personaje != null) {
            g.setColor (colorPersonaje);
            g.fillOval(personaje.getPosicionX(), personaje.getPosicionY(), 30, 30);

        }
    }


}
