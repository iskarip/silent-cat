package Vista;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;


public class FlashbackGato implements InterfazVisual {

    // --- VARIABLES Y ATRIBUTOS ---
    private boolean mostrandoFlashback = false;
    private int poseActual = 0;

    private Image imagenFlashback1;
    private Image imagenFlashback2;
    private Image imagenFlashback3;
    private Image imagenFlashback4;
    private Image imagenFlashback5;

    public FlashbackGato() {
        imagenFlashback1 = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_1.png");
        imagenFlashback2 = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_2.png");
        imagenFlashback3 = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_3.png");
        imagenFlashback4 = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_4.png");
        imagenFlashback5 = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_5.png");
    }

    private static Image cargarImagenFlashback(String ruta) {
        try (InputStream is = FlashbackGato.class.getResourceAsStream(ruta)) {
            if (is == null) {
                System.out.println("No se encontró el recurso en: " + ruta);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            System.out.println("Error al leer " + ruta + ": " + e.getMessage());
            return null;
        }
    }
//setters getters
    public void setMostrandoFlashback(boolean mostrandoFlashback){
        this.mostrandoFlashback = mostrandoFlashback;
    }

    public void setPoseActual(int poseActual){
        this.poseActual = poseActual;
    }

    public boolean getMostrandoFlashback(){
        return mostrandoFlashback;
    }
//metodo apra activar flashback
public void activar(NivelPanel panel) {
    // 1. Elige una postura aleatoria entre las 5 imágenes (0, 1, 2, 3 o 4)
    this.poseActual = (int) (Math.random() * 5);
    this.mostrandoFlashback = true;

    // 2. Muestra la imagen durante 1.5 segundos (1500 ms) y luego la oculta
    javax.swing.Timer timer = new javax.swing.Timer(1500, e -> {
        this.mostrandoFlashback = false;
        if (panel != null) {
            panel.repaint();
        }
        ((javax.swing.Timer) e.getSource()).stop();
    });
    timer.setRepeats(false);
    timer.start();

    // Refresca la pantalla inmediatamente
    if (panel != null) {
        panel.repaint();
    }
}

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!mostrandoFlashback) return;

        Image imagenAMostrar;
        switch (poseActual) {
            case 0: imagenAMostrar = imagenFlashback1; break;
            case 1: imagenAMostrar = imagenFlashback2; break;
            case 2: imagenAMostrar = imagenFlashback3; break;
            case 3: imagenAMostrar = imagenFlashback4; break;
            default: imagenAMostrar = imagenFlashback5; break;
        }

        if (imagenAMostrar != null) {
            Composite composicionOriginal = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 60% opaco
            
            g2d.drawImage(imagenAMostrar, 0, 0, ancho, alto, panel);
            g2d.setComposite(composicionOriginal); // Restaura transparencia
        }
        
        dibujarEstatica(g2d, ancho, alto); //esta fuera del if de imagen, cosa de que
        //Si la imagen no se muestra, la estatica no va a depender de esta
    //va a ser un efecto de estatica tipo tv vieja
    }
    private void dibujarEstatica(Graphics2D g2d, int ancho, int alto) {
        Composite original = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));

        int cantidadLineas = 120;
        for (int i = 0; i < cantidadLineas; i++) {
            int x = (int) (Math.random() * ancho);
            int y = (int) (Math.random() * alto);
            int largo = 10 + (int) (Math.random() * 60);
            int grosor = 1 + (int) (Math.random() * 2);

            int gris = 180 + (int) (Math.random() * 75); // entre 180 y 255
            g2d.setColor(new Color(gris, gris, gris));
            g2d.fillRect(x, y, largo, grosor);
        }
    //el efecto pero lineas horizontales que parezca sin señal

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (int i = 0; i < 3; i++) {
            int y = (int) (Math.random() * alto);
            int alturaBanda = 4 + (int) (Math.random() * 10);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, y, ancho, alturaBanda);
        }

        g2d.setComposite(original);

    }
}

