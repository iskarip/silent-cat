package Vista;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
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

    public FlashbackGato() {
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
    // 1. Elige una postura aleatoria entre las 4 imágenes (0, 1, 2 o 3)
    this.poseActual = (int) (Math.random() * 4);
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
            default: imagenAMostrar = imagenFlashback4; break;
        }

        if (imagenAMostrar != null) {
            Composite composicionOriginal = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 60% opaco
            g2d.drawImage(imagenAMostrar, 0, 0, ancho, alto, panel);
            g2d.setComposite(composicionOriginal); // Restaura transparencia
        }
    }
}

