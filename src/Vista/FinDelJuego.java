package Vista;

import java.awt.*;
import javax.swing.*;

public class FinDelJuego extends JPanel {

    private Image imagenGameOver;
    private Image imagenCalavera;
    private BotonJuego botonVolverMenu;
    private BotonJuego botonReintentar;

    public FinDelJuego() {
        // Fondo oscurecido
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
        botonReintentar = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/ReanudarPartida.png",
                "/Recursos/UI/Pausa/Botones/ReanudarPartidaHover.png",
                180, 55
        );

        botonVolverMenu = new BotonJuego(
                "/Recursos/UI/GameOver/Botones/VolverMenu.png",
                "/Recursos/UI/GameOver/Botones/VolverMenuHover.png",
                "Recursos/Sonidos/UI/sonido3.wav"
        );

        add(botonReintentar);
        add(botonVolverMenu);
    }

    // -- CICLO DE DIBUJADO Y POSICIONAMIENTO --
    @Override
    public void doLayout() {
        super.doLayout();

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int anchoBoton = 180;
        int altoBoton = 55;

        // 1. Botón Reanudar / Reintentar: debajo de la calavera
        if (botonReintentar != null) {
            botonReintentar.setBounds(centroX - (anchoBoton / 2), centroY + 105, anchoBoton, altoBoton);
        }

        // 2. Botón Volver al Menú: debajo de Reanudar
        if (botonVolverMenu != null) {
            Dimension tamVolver = botonVolverMenu.getPreferredSize();
            int w = (tamVolver != null && tamVolver.width > 0) ? tamVolver.width : anchoBoton;
            int h = (tamVolver != null && tamVolver.height > 0) ? tamVolver.height : altoBoton;
            botonVolverMenu.setBounds(centroX - (w / 2), centroY + 175, w, h);
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

            if (anchoOrig > 0 && altoOrig > 0) {
                int anchoMax = (int) (getWidth() * 0.5);
                int altoMax = 200;
                double escala = Math.min((double) anchoMax / anchoOrig, (double) altoMax / altoOrig);

                int ancho = (int) (anchoOrig * escala);
                int alto = (int) (altoOrig * escala);

                // Centrado arriba de la calavera
                g2d.drawImage(imagenGameOver, centroX - (ancho / 2), centroY - 80 - alto, ancho, alto, this);
            }
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

    public BotonJuego getBotonReintentar() {
        return botonReintentar;
    }
}