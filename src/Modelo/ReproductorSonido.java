package Modelo;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*; //el * le dice a java que importe todas las clases de este paquete, porque voy a usar varias cosas del mismo lugar.

public class ReproductorSonido {

    private static float volumenMusica = 0.5f;   // 0.0 (silencio) a 1.0 (máximo)
    private static float volumenEfectos = 1.0f;

    private static final List<Clip> clipsActivos = new ArrayList<>(); 
    private static Clip musicaActual; // la canción que está sonando (una sola a la vez)

    
   public static void reproducir(String ruta) {
    Thread hiloSonido = new Thread(() -> {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                ReproductorSonido.class.getClassLoader().getResource(ruta));

            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    aplicarVolumen(clip, volumenEfectos);
                    clipsActivos.remove(clip);
                }
            });

            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido: " + e.getMessage());
        }
    }, "hilo-sonido"); // nombre del hilo, útil para debuggear

    hiloSonido.setDaemon(true); // no bloquea el cierre del programa si queda sonando
    hiloSonido.start();
}
          














    public static void reproducirEnLoop(String ruta) {
        detenerMusica(); // corta la anterior para que no se superpongan
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    ReproductorSonido.class.getClassLoader().getResource(ruta));
            musicaActual = AudioSystem.getClip();
            musicaActual.open(audioIn);
            aplicarVolumen(musicaActual, volumenMusica); 
            musicaActual.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la musica: " + e.getMessage());
        }
    }

    public static void detenerMusica() {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.close();
            musicaActual = null;
        }
    }

    public static float getVolumenMusica() {
        return volumenMusica;
    }

    public static float getVolumenEfectos() {
        return volumenEfectos;
    }

    public static void setVolumenMusica(float volumen) {
        volumenMusica = limitar(volumen);
        if (musicaActual != null) {
            aplicarVolumen(musicaActual, volumenMusica);
        }
    }

    public static void setVolumenEfectos(float volumen) {
        volumenEfectos = limitar(volumen);
    }

    private static float limitar(float volumen) {
        return Math.max(0f, Math.min(1f, volumen));
    }

    private static void aplicarVolumen(Clip clip, float volumen) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float db = (volumen <= 0.0001f)
                ? control.getMinimum()
                : (float) (20.0 * Math.log10(volumen));
        db = Math.max(control.getMinimum(), Math.min(control.getMaximum(), db));
        control.setValue(db);
    }

    }
