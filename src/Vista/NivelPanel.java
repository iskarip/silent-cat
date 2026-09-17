package Vista;

import Modelo.Gato;
import Modelo.Nivel;
import Modelo.Personaje;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class NivelPanel extends JPanel {

private Nivel nivelActual;
private boolean mostrandoFlashback = false;
private Image imagenFlashback1= new ImageIcon("Recursos/imagenes_gato/1flashback.png").getImage();
private Image imagenFlashback2= new ImageIcon("Recursos/imagenes_gato/2flashback.png").getImage();
private Image imagenFlashback3= new ImageIcon("Recursos/imagenes_gato/3flashback.png").getImage();
private Image imagenFlashback4= new ImageIcon("Recursos/imagenes_gato/4flashback.png").getImage();

private int poseActual = 0; // guarda cual de las 4 imágenes toca mostrar ahora
private java.util.Random random = new java.util.Random(); // para elegir al azar

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

        int velocidadBase = 4;
        int velocidad = (int)(velocidadBase * personaje.getMultiplicadorVelocidad());
//modifique los valores que reciben las variables para poder agregarle la lentitud al personaje, por eso quedo velocidad y no el 4 de la velocidad base
        if (teclasPresionadas.contains(KeyEvent.VK_UP)) deltaY -= velocidad;
        if (teclasPresionadas.contains(KeyEvent.VK_DOWN)) deltaY += velocidad;
        if (teclasPresionadas.contains(KeyEvent.VK_LEFT)) deltaX -= velocidad;
        if (teclasPresionadas.contains(KeyEvent.VK_RIGHT)) deltaX += velocidad;

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
    if (mostrandoFlashback) {
      Image imagenAMostrar;
        switch (poseActual) {
        case 0: imagenAMostrar = imagenFlashback1; break;
        case 1: imagenAMostrar = imagenFlashback2; break;
        case 2: imagenAMostrar = imagenFlashback3; break;
        default: imagenAMostrar = imagenFlashback4; break;
    }
    g.drawImage(imagenAMostrar, 0, 0, getWidth(), getHeight(), this);

    }
    
    
    }

    public void setNivelActual(Nivel nivel){
        this.nivelActual = nivel;
    }

    public void activarFlashbackGato(){
        if (nivelActual == null || nivelActual.getGato() == null) return; //significaria q este nivel no tiene gato
    
    
    Gato gato = nivelActual.getGato();
    gato.reproducirSonidoFlashback();
    personaje.aplicarLentitud();
    mostrandoFlashback = true;

    //necesitaba un timer para el parpadeo de la imagen 
    Timer parpadeo = new Timer(200, null);
    int[] contador = {0};
    parpadeo.addActionListener(e -> {
        mostrandoFlashback = !mostrandoFlashback;
                if (mostrandoFlashback) {
                    poseActual = random.nextInt(4);
                }
        repaint();
        contador[0]++;
        if (contador[0] >= 10) { // 10 parpadeos = 2 segundos totales
            ((Timer) e.getSource()).stop();
            mostrandoFlashback = false;
            personaje.quitarLentitud();
            repaint();
        }
    });
    parpadeo.start();
    }

}
