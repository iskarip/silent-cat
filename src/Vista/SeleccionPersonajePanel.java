package Vista;

import javax.swing.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;


public class SeleccionPersonajePanel extends JPanel {

    private JButton botonHombre;
    private JButton botonMujer;
    private Image imagenFondo;

    public SeleccionPersonajePanel() { // Sin JuegoFrame

        try {
            java.net.URL url = getClass().getResource("/Recursos/UIMenu/Fondo/imagenSeleccionPersonaje.jpg");
            if (url != null) {
                this.imagenFondo = javax.imageio.ImageIO.read(url);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar fondo de selección: " + e.getMessage());
        }

        botonHombre = new JButton("Hombre");
        botonMujer = new JButton("Mujer");

        add(botonHombre);
        add(botonMujer);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Dibuja la imagen cubriendo todo el tamaño de la ventana
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fondo oscuro de respaldo por si no cargó
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public JButton getBotonHombre() { return botonHombre; }
    public JButton getBotonMujer() { return botonMujer; }
}



