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
}
