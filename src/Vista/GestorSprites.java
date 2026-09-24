package Vista;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public class GestorSprites {

    //EnumMap: un Map para cuando la clave es un enum.
    //Guarda una imagen para cada estado del personaje

    private final Map<EstadoPersonaje, BufferedImage> sprites = new EnumMap<>(EstadoPersonaje.class);
    private BufferedImage [] cuadrosIdle;

    public GestorSprites(String carpetaBase) {
        if (!carpetaBase.endsWith("/")) {
            carpetaBase += "/";
        }

        cargar(EstadoPersonaje.IDLE, carpetaBase + "idle.png");
        cargar(EstadoPersonaje.CAMINANDO, carpetaBase + "caminar.png");
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
            BufferedImage imagen = ImageIO.read(is);
            sprites.put(estado, imagen);
        } catch (IOException e) {
            System.out.println("Error al leer el sprite de " + estado + ": " + e.getMessage());
        }
    }

    public BufferedImage obtener(EstadoPersonaje estado) {
        return sprites.get(estado);
    }

    private void cargarSpritesheetIdle (String ruta) {
        try {
            BufferedImage hoja = Image.IO.read(getClass().getResourceAsStream(ruta));
            int columnas = 4, filas = 4;
            int anchoCuadro = hoja.getWidth() / columnas;
            int altoCuadro = hoja.getHeight() / filas;

            cuadrosIdle = new BufferedImage[columnas * filas];
            int indice = 0;
            for (int fila = 0; fila < filas; fila++) {
                for (int col = 0; col < columnas; col++) {
                    cuadrosIdle[indice] = hoja.getSubimage(
                            col * anchoCuadro, fila * altoCuadro, anchoCuadro, altoCuadro
                    );
                    indice++;
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar el spritesheet idle: " + e.getMessage());
            cuadrosIdle = null;
        }
    }

    // getSubImage --> metodo de BufferedImage que recorta un rectangulo especifico de una imagen mas grande
    // devolviendo una imagen mas chica.

    private void cargarFijo(EstadoPersonaje estado, String ruta) {
        try {
            spritesFijos.put(estado, ImageIO.read(getClass().getResourceAsStream(ruta)));
        } catch (Exception e) {
            System.out.println("No se pudo cargar " + estado + ": " + e.getMessage());
        }
    }

    // Devuelve el cuadro de idle correspondiente según el tiempo transcurrido
    public BufferedImage obtenerCuadroIdle(int indiceCuadro) {
        if (cuadrosIdle == null) return null;
        return cuadrosIdle[indiceCuadro % cuadrosIdle.length];
    }

    public int getCantidadCuadrosIdle() {
        return cuadrosIdle != null ? cuadrosIdle.length : 0;
    }

    public BufferedImage obtener(EstadoPersonaje estado) {
        return spritesFijos.get(estado);
    }
}
}
