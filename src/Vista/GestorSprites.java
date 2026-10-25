package Vista;

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

    private final Map<EstadoAnimacion, BufferedImage> spritesFijos = new EnumMap<>(EstadoAnimacion.class);
    private static final Map<String, Image> CACHE_IMAGENES = new HashMap<>();

    public GestorSprites(String carpetaBase) {
        if (!carpetaBase.endsWith("/")) {
            carpetaBase += "/";
        }

        cargar(EstadoAnimacion.IDLE, carpetaBase + "idle.png");
        cargar(EstadoAnimacion.CAMINANDO, carpetaBase + "caminar.png");
        cargar(EstadoAnimacion.CORRIENDO, carpetaBase + "correr.png");
        cargar(EstadoAnimacion.ATACANDO, carpetaBase + "atacar.png");
        cargar(EstadoAnimacion.RECIBIENDO_DANIO, carpetaBase + "recibirDanio.png");
        cargar(EstadoAnimacion.MURIENDO, carpetaBase + "muere.png");
        cargar(EstadoAnimacion.SENTADO, carpetaBase + "sentado.png");
        cargar(EstadoAnimacion.LEVANTANDOSE, carpetaBase + "levantarse.png");
    }

    private void cargar(EstadoAnimacion estado, String rutaRecurso) {
        try (InputStream is = getClass().getResourceAsStream(rutaRecurso)) {
            if (is == null) {
                System.err.println("[GestorSprites] No se encontró el recurso en: " + rutaRecurso);
                return;
            }
            spritesFijos.put(estado, ImageIO.read(is));
        } catch (IOException e) {
            System.err.println("[GestorSprites] Error al leer sprite de " + estado + ": " + e.getMessage());
        }
    }

    public BufferedImage obtener(EstadoAnimacion estado) {
        BufferedImage hoja = spritesFijos.get(estado);
        return (hoja != null) ? hoja : spritesFijos.get(EstadoAnimacion.CAMINANDO);
    }

    public static Image cargarImagen(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }
        return CACHE_IMAGENES.computeIfAbsent(ruta, GestorSprites::leerImagenDesdeOrigen);
    }

    private static Image leerImagenDesdeOrigen(String ruta) {
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

 
    public void dibujarCuadro(Graphics2D g2d, EstadoAnimacion estado, int fila,
                              int frame, int x, int y, int anchoCuadro, int altoCuadro, double escala) {
        BufferedImage hoja = obtener(estado);
        if (hoja == null) return;

        int srcX1 = frame * anchoCuadro;
        int srcY1 = Math.max(0, fila) * altoCuadro;
        int srcX2 = srcX1 + anchoCuadro;
        int srcY2 = srcY1 + altoCuadro;

        int anchoBase = anchoCuadro;
        int altoBase  = altoCuadro;
        int destAncho = (int) (anchoBase * escala);
        int destAlto  = (int) (altoBase * escala);

        int dx = x + (anchoBase - destAncho) / 2;
        int dy = y + (altoBase - destAlto);

        g2d.drawImage(hoja, dx, dy, dx + destAncho, dy + destAlto, srcX1, srcY1, srcX2, srcY2, null);
    }
}