package Vista;

import Modelo.Direccion;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class GestorSprites {

    // EnumMap: un Map para cuando la clave es un enum.
    // Guarda una imagen fija para cada estado del personaje (excepto IDLE, que es un spritesheet animado).
    private final Map<EstadoPersonaje, BufferedImage> spritesFijos = new EnumMap<>(EstadoPersonaje.class);

    // cache compartido para imagenes.
    private static final Map<String, Image> CACHE_IMAGENES = new HashMap<>();

    public GestorSprites(String carpetaBase) {
        if (!carpetaBase.endsWith("/")) {
            carpetaBase += "/";
        }

        cargar(EstadoPersonaje.IDLE, carpetaBase + "idle.png");
        cargar(EstadoPersonaje.CAMINANDO, carpetaBase + "caminar.png");
        cargar(EstadoPersonaje.CORRIENDO, carpetaBase + "correr.png");
        cargar(EstadoPersonaje.ATACANDO, carpetaBase + "atacar.png");
        cargar(EstadoPersonaje.RECIBIENDO_DANIO, carpetaBase + "recibirDanio.png");
        cargar(EstadoPersonaje.MURIENDO, carpetaBase + "muere.png");
    }

    private void cargar(EstadoPersonaje estado, String rutaRecurso) {
        try (InputStream is = getClass().getResourceAsStream(rutaRecurso)) {
            if (is == null) {
                System.out.println("No se encontro el recurso en: " + rutaRecurso);
                return;
            }
            spritesFijos.put(estado, ImageIO.read(is));
        } catch (IOException e) {
            System.out.println("Error al leer el sprite de " + estado + ": " + e.getMessage());
        }
    }

    public BufferedImage obtener(EstadoPersonaje estado) {
        return spritesFijos.get(estado);
    }

    // -- CARGA DE RECURSOS (antes le correspondia a NivelPanel) --

    public static Image cargarImage(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }
        return CACHE_IMAGENES.computeIfAbsent(ruta, GestorSprites::leerImagenDesdeOrigenString);
    }

    private static Image leerImagenDesdeOrigenString(String ruta) {
        try {
            URL url = GestorSprites.class.getResource(ruta);
            if (url != null) return ImageIO.read(url);

            if (!ruta.startsWith("/")) {
                url = GestorSprites.class.getResource("/" + ruta);
                if (url != null) return ImageIO.read(url);
            }

            File archivo = new File(ruta.startsWith("/") ? ruta.substring(1) : ruta);
            if (archivo.exists()) return ImageIO.read(archivo);

            System.err.println("[GestorSprites] Recurso no encontrado: " + ruta);
            return null;
        } catch (IOException e) {
            System.err.println("[GestorSprites] Error al leer imagen: " + e.getMessage());
            return null;
        }
    }

    // --- DIBUJADO DE SPRITESHEET (Absorbe el corte de NivelPanel) ---
    public void dibujarCuadro(Graphics2D g2d, EstadoPersonaje estado, Direccion direccion,
                              int frame, int x, int y, int anchoCuadro, int altoCuadro, double escala) {
        BufferedImage hoja = obtener(estado);
        if (hoja == null) return;

        int srcX1 = frame * anchoCuadro;
        int srcY1 = (direccion != null ? direccion.getFila() : 0) * altoCuadro;
        int srcX2 = srcX1 + anchoCuadro;
        int srcY2 = srcY1 + altoCuadro;

        int destAncho = (int) (anchoCuadro * escala);
        int destAlto = (int) (altoCuadro * escala);

        g2d.drawImage(hoja, x, y, x + destAncho, y + destAlto, srcX1, srcY1, srcX2, srcY2, null);
    }

    // Método público que llama NivelPanel
    public static java.awt.Image cargarImagen(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }
        return CACHE_IMAGENES.computeIfAbsent(ruta, GestorSprites::leerImagenDesdeOrigen);
    }

    private static java.awt.Image leerImagenDesdeOrigen(String ruta) {
        try {
            java.net.URL url = GestorSprites.class.getResource(ruta);
            if (url != null) return javax.imageio.ImageIO.read(url);

            if (!ruta.startsWith("/")) {
                url = GestorSprites.class.getResource("/" + ruta);
                if (url != null) return javax.imageio.ImageIO.read(url);
            }

            java.io.File archivo = new java.io.File(ruta.startsWith("/") ? ruta.substring(1) : ruta);
            if (archivo.exists()) return javax.imageio.ImageIO.read(archivo);

            System.err.println("[GestorSprites] Recurso no encontrado: " + ruta);
            return null;
        } catch (java.io.IOException e) {
            System.err.println("[GestorSprites] Error al leer imagen: " + e.getMessage());
            return null;
        }
    }

}


