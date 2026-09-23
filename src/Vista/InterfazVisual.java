package Vista;
import java.awt.Graphics2D;


public interface InterfazVisual {
    void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel);
}
//esta interfaz asegura que cualquier capa visual extra, como la linterna, gato, niebla quizas o hasta lluvia, tenga la misma estructura y podamos
//renderizarla
