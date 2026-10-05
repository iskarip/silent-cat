package Vista;

import java.awt.*;
import javax.swing.*;

public class FinDelJuego extends JPanel {

    private Image imagenCalavera;
    private BotonJuego botonVolverMenu;
    private BotonJuego botonReintentar;

    public FinDelJuego() {
        setOpaque(false);
        setLayout(null);
        setVisible(false);

        cargarRecursos();
        crearComponentes();
    }

    private void cargarRecursos() {
        this.imagenCalavera = GestorSprites.cargarImage("/Recursos/UI/Menu/Fondo/calaveraMuerte.png");
    }

    private void crearComponentes() {
        botonReintentar = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/ReanudarPartida.png",
                "/Recursos/UI/Pausa/Botones/ReanudarPartidaHover.png",
                180, 55
        );

        botonVolverMenu = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/MenuPrincipal.png",
                "/Recursos/UI/Pausa/Botones/MenuPrincipalHover.png",
                180, 55
        );

        add(botonReintentar);
        add(botonVolverMenu);
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int anchoBoton = 180;
        int altoBoton = 55;

        // Botón Reintentar debajo de la calavera
        if (botonReintentar != null) {
            botonReintentar.setBounds(centroX - (anchoBoton / 2), centroY + 95, anchoBoton, altoBoton);
        }

        // Botón Volver al menú debajo de Reintentar
        if (botonVolverMenu != null) {
            botonVolverMenu.setBounds(centroX - (anchoBoton / 2), centroY + 160, anchoBoton, altoBoton);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 0, 0, 215));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Serif", Font.BOLD, 54));
        FontMetrics fm = g2d.getFontMetrics();
        String texto = "GAME OVER";
        int textoX = centroX - (fm.stringWidth(texto) / 2);
        int textoY = centroY - 90;

        g2d.setColor(new Color(100, 0, 0));
        g2d.drawString(texto, textoX + 3, textoY + 3);
        g2d.setColor(new Color(180, 20, 20));
        g2d.drawString(texto, textoX + 1, textoY + 1);

        g2d.setColor(new Color(240, 235, 230));
        g2d.drawString(texto, textoX, textoY);

        if (imagenCalavera != null) {
            int tamCalavera = 150;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(imagenCalavera, centroX - (tamCalavera / 2), centroY - 65, tamCalavera, tamCalavera, this);
        }
    }

    public BotonJuego getBotonVolverMenu() {
        return botonVolverMenu;
    }

    public BotonJuego getBotonReintentar() {
        return botonReintentar;
    }
}