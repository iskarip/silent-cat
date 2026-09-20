package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private JuegoFrame ventanaPrincipal;
    private BufferedImage imagenFondo; // imagen para el menu principal

    public MenuPanel(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        cargarFondo();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        BotonJuego botonNuevaPartida = new BotonJuego("/Recursos/UI/Menu/Botones/NuevaPartida.png","/Recursos/UI/Menu/Botones/NuevaPartidaHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        botonNuevaPartida.addActionListener((e -> ventanaPrincipal.mostrarPantalla("seleccion")));

        BotonJuego botonAjustes= new BotonJuego("/Recursos/UI/Menu/Botones/Ajustes.png", "/Recursos/UI/Menu/Botones/AjustesHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        
        BotonJuego botonSalir = new BotonJuego("/Recursos/UI/Menu/Botones/Salir.png", "/Recursos/UI/Menu/Botones/SalirHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        botonSalir.addActionListener((e -> System.exit(0)));

        gbc.gridy = 0;
        gbc.insets = new Insets(350, 0, 0, 0);
        add(botonNuevaPartida, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(botonAjustes, gbc);
        gbc.gridy = 2;
        add(botonSalir, gbc);
    }

    private void cargarFondo() {
        try {
            imagenFondo = ImageIO.read(new File("src/Recursos/UI/Menu/Fondo/imagenMenu1.png"));
        } catch (IOException e) {
            System.out.println("No se pudo cargar el fondo del menu: " + e.getMessage());
            imagenFondo = null;
        }
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // dibuja la imagen completa para cubrir todo el panel

            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

