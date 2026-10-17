package Vista;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class ReproductorSonido {

    private static float volumenMusica = 0.5f;   // 0.0 (silencio) a 1.0 (máximo)
    private static float volumenEfectos = 1.0f;

    private static final List<Clip> clipsActivos = new ArrayList<>(); 
    private static Clip musicaActual; // La canción que está sonando (una sola a la vez)

    private static final Map<String, Clip> clipsLoopEfecto = new HashMap<>(); // Clips ya leídos, por ruta
    private static Clip loopEfectoActual; // El efecto en loop que está sonando ahora

    private static long posicionPausa = 0;

    public static void reproducir(String ruta) {
        Thread hiloSonido = new Thread(() -> {
            try {
                java.net.URL url = ReproductorSonido.class.getClassLoader().getResource(ruta);
                if (url == null && !ruta.startsWith("/")) {
                    url = ReproductorSonido.class.getResource("/" + ruta);
                }

                AudioInputStream audioIn;
                if (url != null) {
                    audioIn = AudioSystem.getAudioInputStream(url);
                } else {
                    java.io.File archivo = new java.io.File(ruta.startsWith("src/") ? ruta : "src/" + ruta);
                    if (!archivo.exists()) {
                        archivo = new java.io.File(ruta);
                    }
                    if (archivo.exists()) {
                        audioIn = AudioSystem.getAudioInputStream(archivo);
                    } else {
                        System.err.println("[ReproductorSonido] No se encontró el archivo de sonido: " + ruta);
                        return;
                    }
                }

                Clip clip = AudioSystem.getClip();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        clipsActivos.remove(clip);
                    }
                });

                clip.open(audioIn);
                aplicarVolumen(clip, volumenEfectos);
                clip.start();
            } catch (Exception e) {
                System.out.println("No se pudo reproducir el sonido (" + ruta + "): " + e.getMessage());
                e.printStackTrace();
            }
        }, "hilo-sonido");

        hiloSonido.setDaemon(true);
        hiloSonido.start();
    }

    // Permite reproducir un sonido especificando un volumen puntual entre 0.0f y 1.0f
    public static void reproducir(String ruta, float volumenEspecifico) {
    Thread hiloSonido = new Thread(() -> {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                ReproductorSonido.class.getClassLoader().getResource(ruta));

            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    clipsActivos.remove(clip);
                }
            });

            clip.open(audioIn);
            // Aplica el volumen puntual pasado por parámetro
            aplicarVolumen(clip, limitar(volumenEspecifico));
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido: " + e.getMessage());
        }
    }, "hilo-sonido");

    hiloSonido.setDaemon(true);
    hiloSonido.start();
    }
    
    public static void reproducirEnLoop(String ruta) {
        Thread hiloMusica = new Thread(() -> {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        ReproductorSonido.class.getClassLoader().getResource(ruta));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                aplicarVolumen(clip, volumenMusica);

                synchronized (ReproductorSonido.class) {
                    detenerMusica(); // Corta la anterior
                    musicaActual = clip;
                    musicaActual.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch (Exception e) {
                System.out.println("No se pudo reproducir la musica: " + e.getMessage());
            }
        }, "hilo-musica");

        hiloMusica.setDaemon(true);
        hiloMusica.start();
    }

    public static synchronized void detenerMusica() {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.close();
            musicaActual = null;
            posicionPausa = 0;
        }
    }

    // --- MÉTODOS DE PAUSA Y REANUDACIÓN DE MÚSICA CORREGIDOS ---

    public static synchronized void pausarMusica() {
        if (musicaActual != null && musicaActual.isRunning()) {
            posicionPausa = musicaActual.getMicrosecondPosition();
            musicaActual.stop();
        }
    }

    public static synchronized void reanudarMusica() {
        if (musicaActual != null && !musicaActual.isRunning()) {
            musicaActual.setMicrosecondPosition(posicionPausa);
            musicaActual.start();
            musicaActual.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // --------------------------------------------------------

    public static float getVolumenMusica() {
        return volumenMusica;
    }

    public static float getVolumenEfectos() {
        return volumenEfectos;
    }

    public static synchronized void setVolumenMusica(float volumen) {
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

    public static synchronized void reproducirEfectoEnLoop(String ruta) {
        Clip clip = obtenerClipLoop(ruta);
        if (clip == null || clip == loopEfectoActual) return;

        detenerEfectoEnLoop();
        aplicarVolumen(clip, volumenEfectos);
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        loopEfectoActual = clip;
    }

    public static synchronized void detenerEfectoEnLoop() {
        if (loopEfectoActual != null) {
            loopEfectoActual.stop();
            loopEfectoActual = null;
        }
    }

    private static Clip obtenerClipLoop(String ruta) {
        Clip clip = clipsLoopEfecto.get(ruta);
        if (clip != null) return clip;

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    ReproductorSonido.class.getClassLoader().getResource(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clipsLoopEfecto.put(ruta, clip);
            return clip;
        } catch (Exception e) {
            System.out.println("No se pudo cargar el efecto en loop: " + e.getMessage());
            return null;
        }
    }
}