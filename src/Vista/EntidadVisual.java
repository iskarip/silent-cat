package Vista;

// Lo que NivelPanel necesita saber de una entidad para dibujarla.
// La define la Vista (desde su propia necesidad) y quien arma la pantalla, el Controlador,
// adapta las entidades del Modelo a este contrato. Así la Vista no importa nada de Modelo.
public interface EntidadVisual {

    int getX();                            // posición del sprite en píxeles del mapa
    int getY();
    int getBaseY();                        // "pies" de la entidad: sirve para ordenar por profundidad
    String getRutaSprites();               // carpeta de la hoja de sprites
    double getEscala();                    // escala del sprite (1.0 = tamaño normal)
    int getFila();                         // fila de la hoja según hacia dónde mira
    EstadoAnimacion getEstadoAnimacion();  // qué animación toca (caminando, sentado, etc.)
    boolean esTransicion();                // true si la animación es de una sola pasada
    double getProgresoTransicion();        // 0.0 a 1.0, solo importa si esTransicion() es true
    boolean mostrarBarraVida();
    int getPorcentajeVida();               // 0 a 100
}
 