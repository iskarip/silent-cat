package Vista;

import java.awt.*;
import javax.swing.*;

public class FinDelJuego extends JPanel {

    private Image imagenGameOver;
    private Image imagenCalavera;
    private BotonJuego botonVolverMenu;

    public FinDelJuego() {
        //fondo oscurecido
        setOpaque(false);
        setLayout(null);
        setVisible(false);

        cargarRecursos();
        crearComponentes();

    }

    private void cargarRecursos() {
      this.imagenCalavera = GestorSprites.cargarImage("/Recursos/UI/GameOver/Img/calaveraMuerte.png");
      this.imagenGameOver = GestorSprites.cargarImage("/Recursos/UI/GameOver/Img/GameOver.png");
    }

    private void crearComponentes() {
        // Botón para volver al menú principal
        botonVolverMenu = new BotonJuego(
            "/Recursos/UI/GameOver/Botones/VolverMenu.png",
            "/Recursos/UI/GameOver/Botones/VolverMenuHover.png", 
            "Recursos/Sonidos/UI/sonido3.wav"
        );

        add(botonVolverMenu);
    }

    // -- CICLO DE DIBUJADO --
    @Override
    public void doLayout() {
        super.doLayout();

        if (botonVolverMenu != null) {
            int centroX = getWidth() / 2;
            int centroY = getHeight() / 2;

            // usa el tamaño real del PNG del botón (ya que no le pasás ancho y alto)
            Dimension tam = botonVolverMenu.getPreferredSize();
            botonVolverMenu.setBounds(centroX - tam.width / 2, centroY + 110, tam.width, tam.height);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Capa oscura sobre el nivel
        g2d.setColor(new Color(0, 0, 0, 215));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Cartel GAME OVER (imagen), manteniendo proporción
        if (imagenGameOver != null) {
            int anchoOrig = imagenGameOver.getWidth(this);
            int altoOrig = imagenGameOver.getHeight(this);

            int anchoMax = (int) (getWidth() * 0.5);   // hasta el 50% del ancho
            int altoMax = 200;                         // y hasta 200 px de alto
            double escala = Math.min((double) anchoMax / anchoOrig, (double) altoMax / altoOrig);

            int ancho = (int) (anchoOrig * escala);
            int alto = (int) (altoOrig * escala);

            // centrado, con su borde inferior apenas arriba de la calavera
            g2d.drawImage(imagenGameOver, centroX - (ancho / 2), centroY - 80 - alto, ancho, alto, this);
        }

        // Calavera pixel art centrada entre el cartel y el botón
        if (imagenCalavera != null) {
            int tamCalavera = 160;
            g2d.drawImage(imagenCalavera, centroX - (tamCalavera / 2), centroY - 70, tamCalavera, tamCalavera, this);
        }
    }

    public BotonJuego getBotonVolverMenu() {
        return botonVolverMenu;
    }
}







