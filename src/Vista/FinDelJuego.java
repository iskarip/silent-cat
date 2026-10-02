package Vista;

import Modelo.ObservadorPersonaje;
import java.awt.*;
import javax.swing.*;

public class FinDelJuego extends JPanel implements ObservadorPersonaje {

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
      this.imagenCalavera = GestorSprites.cargarImage("/Recursos/UI/Menu/Fondo/calaveraMuerte.png");
    }

    private void crearComponentes() {
        // Botón para volver al menú principal
        botonVolverMenu = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/MenuPrincipal.png",
                null,
                180, 60
        );

        add(botonVolverMenu);
    }

    // -- GESTION DEL OBSERVER --
    @Override
    public void vidaCambio(int vidaActual, int vidaMaxima) {
        if (vidaActual > 0 && isVisible()) {
            setVisible(false);
        }
    }

    @Override
    public void personajeMurio() {
        // el modelo es el que notifica la muerte.
        if (getParent() != null) {
            setBounds(0, 0, getParent().getWidth(), getParent().getHeight());
        }
        setVisible(true);
        repaint();
    }

    // -- CICLO DE DIBUJADO --
    @Override
    public void doLayout() {
        super.doLayout();

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        if (botonVolverMenu != null) {
            int anchoVolver = 210;
            int altoVolver = 70;
            botonVolverMenu.setBounds(centroX - (anchoVolver / 2), centroY + 110, anchoVolver, altoVolver);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Capa oscura sobre el nivel.
        g2d.setColor(new Color(0, 0, 0, 215));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        // Texto GAME OVER
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Serif", Font.BOLD, 54));
        FontMetrics fm = g2d.getFontMetrics();
        String texto = "GAME OVER";
        int textoX = centroX - (fm.stringWidth(texto) / 2);
        int textoY = centroY - 100;

        // Sombra doble roja oscura
        g2d.setColor(new Color(100, 0, 0));
        g2d.drawString(texto, textoX + 3, textoY + 3);
        g2d.setColor(new Color(180, 20, 20));
        g2d.drawString(texto, textoX + 1, textoY + 1);

        // Texto principal
        g2d.setColor(new Color(240, 235, 230));
        g2d.drawString(texto, textoX, textoY);

        // Calavera pixel art centrada entre el texto y el botón
        if (imagenCalavera != null) {
            int tamCalavera = 160 ;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(imagenCalavera, centroX - (tamCalavera / 2), centroY - 70, tamCalavera, tamCalavera, this);
        }
    }

    public BotonJuego getBotonVolverMenu() {
        return botonVolverMenu;
    }
}







