package Vista;

import Modelo.Gato;
import Modelo.Personaje;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class FlashbackGato {

    // --- VARIABLES Y ATRIBUTOS ---
    private boolean mostrandoFlashback = false;
    private int poseActual = 0;
    private Random random = new Random();

    private Image imagenFlashback1;
    private Image imagenFlashback2;
    private Image imagenFlashback3;
    private Image imagenFlashback4;

    public FlashbackGato() {
        // Carga de imágenes tal como ella las definió
        imagenFlashback1 = cargarImagenFlashback("/Recursos/imagenes_gato/1flashback.png");
        imagenFlashback2 = cargarImagenFlashback("/Recursos/imagenes_gato/2flashback.png");
        imagenFlashback3 = cargarImagenFlashback("/Recursos/imagenes_gato/3flashback.png");
        imagenFlashback4 = cargarImagenFlashback("/Recursos/imagenes_gato/4flashback.png");
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

    // --- LÓGICA DE ACTIVACIÓN cambiada, ahora es parpadeo y animacion
    public void activar(NivelPanel panel) {
        mostrandoFlashback = true;

        Timer parpadeo = new Timer (200, null);
        int[] contador = {0}; // Usamos un array para poder modificarlo dentro del ActionListener

        parpadeo.addActionListener(e -> {
            mostrandoFlashback = !mostrandoFlashback;
            if (mostrandoFlashback){
                    poseActual = random.nextInt(4); // Cambia la pose a una aleatoria entre 0 y 3
            }
            panel.repaint();
            contador[0]++;
            if (contador[0] >= 10) { 
                ((Timer) e.getSource()).stop();
                mostrandoFlashback = false;
                panel.repaint();
        }
    });
    parpadeo.start();

    }

    // --- DIBUJADO EN PANTALLA
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel) {
        if (!mostrandoFlashback) return;

        Image imagenAMostrar;
        switch (poseActual) {
            case 0: imagenAMostrar = imagenFlashback1; break;
            case 1: imagenAMostrar = imagenFlashback2; break;
            case 2: imagenAMostrar = imagenFlashback3; break;
            default: imagenAMostrar = imagenFlashback4; break;
        }

        if (imagenAMostrar != null) {
            Composite composicionOriginal = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 60% opaco
            g2d.drawImage(imagenAMostrar, 0, 0, ancho, alto, panel);
            g2d.setComposite(composicionOriginal); // Restaura la opacidad
        }
    }

    public boolean isMostrandoFlashback() {
        return mostrandoFlashback;
    }
}

