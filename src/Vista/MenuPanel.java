package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.gridx = 0;

        JButton botonIniciar = crearBotonTematico("INICIAR");
        botonIniciar.addActionListener(e -> ventanaPrincipal.mostrarPantalla("seleccion"));

        JButton botonReiniciar = crearBotonTematico("REINICIAR");
        botonReiniciar.addActionListener(e-> {System.out.println("Reiniciar presionado");});

        JButton botonSalir = crearBotonTematico("SALIR DEL JUEGO");
        botonSalir.addActionListener((e -> System.exit(0)));

        gbc.gridy = 0;
        add(botonIniciar, gbc);
        gbc.gridy = 1;
        add(botonReiniciar, gbc);
        gbc.gridy = 2;
        add(botonSalir, gbc);
    }

    private JButton crearBotonTematico(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(240, 55));
        boton.setFont(new Font("Georgia", Font.BOLD, 20));

        // Paleta oscura, tipo madera vieja / sangre, acorde al fondo
        boton.setBackground(new Color(30, 10, 10));
        boton.setForeground(new Color(220, 60, 40)); // rojo apagado, como el título
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(90, 40, 20), 3));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto simple al pasar el mouse por encima
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(60, 15, 15));
                boton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(30, 10, 10));
                boton.setForeground(new Color(220, 60, 40));
            }
        });

        return boton;
    }

    private void cargarFondo() {
        try {
            imagenFondo = ImageIO.read(new File("imagen/imagenMenu.jpeg"));
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

