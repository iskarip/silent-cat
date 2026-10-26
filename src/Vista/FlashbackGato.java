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

    private static final String[] SONIDOS = {
        "Recursos/Sonidos/sonidos_gato/flashback1.wav",
        "Recursos/Sonidos/sonidos_gato/flashback2.wav",
        "Recursos/Sonidos/sonidos_gato/flashback3.wav",
        "Recursos/Sonidos/sonidos_gato/flashback4.wav"
    };

    private static final int MS_POR_IMAGEN= 500; //cuanto dura cada imagen en pantalla

    private boolean mostrandoFlashback = false;
    private int poseActual = 0;

    private final Image[] imagenesFlashback = new Image[5];

    public FlashbackGato() {
        for (int i = 0; i < imagenesFlashback.length; i++) {
            imagenesFlashback[i] = cargarImagenFlashback("/Recursos/imagenes_gato/gato_flashback_" + (i + 1) + ".png");
        }
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
//metodo apra activar flashback, muestra todas las imagenes una despues de otra, en orden
//y al termianr la ultima se oculta solo

public void activar(NivelPanel panel, Runnable alTerminar) {
    if (mostrandoFlashback) return; //si ya hay una secuencia en curso, no se pisa

        this.poseActual= 0;
        this.mostrandoFlashback = true;
        ReproductorSonido.reproducir(SONIDOS[(int) (Math.random() * SONIDOS.length)]);

    //un solo timer que se repite: cada disparo pasa a la imagen siguiente
        javax.swing.Timer timer = new javax.swing.Timer(MS_POR_IMAGEN, e -> {
            poseActual++;
            if (poseActual >= imagenesFlashback.length) { //ya se mostraron todas
                mostrandoFlashback = false;
                poseActual = 0;
                ((javax.swing.Timer) e.getSource()).stop();
            
                if (alTerminar != null){//avisa que el flashback termino
                    alTerminar.run();
                }
            }
            if (panel != null) {
                panel.repaint();
            }
        });
        timer.start();   // por defecto se repite hasta que se hace stop()
    
        // refresca la pantalla inmediatamente para ver la primera imagen
    if (panel != null) {
        panel.repaint();
    }
}

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!mostrandoFlashback) return;

        Image imagenAMostrar = imagenesFlashback[poseActual];

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

