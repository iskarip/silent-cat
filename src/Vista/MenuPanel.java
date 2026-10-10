package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private final ParticulasAmbiente particulas;
    private final Timer temporizador;
    private BufferedImage imagenFondo;

    private BotonJuego botonNuevaPartida;
    private BotonJuego botonContinuar;
    private BotonJuego botonAjustes;
    private BotonJuego botonSalir;

    public MenuPanel() {
        
        cargarFondo();

        particulas = new ParticulasAmbiente(80, 1280, 720);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                particulas.setTamanio(getWidth(), getHeight());
            }
        });

        temporizador = new Timer(16, e -> { // ~60 FPS
            particulas.actualizar();
            repaint();
        });
        temporizador.start();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 1. Alineación a la IZQUIERDA
        gbc.anchor = GridBagConstraints.WEST; 
        
        // 2. Empuja los botones hacia la izquierda para que no queden pegados al centro
        gbc.weightx = 1.0; 

        // 3. Botones personalizados
        botonNuevaPartida = new BotonJuego("/Recursos/UI/Menu/Botones/NuevaPartida.png", "/Recursos/UI/Menu/Botones/NuevaPartidaHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        botonContinuar    = new BotonJuego("/Recursos/UI/Menu/Botones/Continuar.png", "/Recursos/UI/Menu/Botones/ContinuarHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        botonContinuar.setVisible(false); // arranca oculto, el controlador lo muestra si hay partida guardada
        botonAjustes      = new BotonJuego("/Recursos/UI/Menu/Botones/Ajustes.png", "/Recursos/UI/Menu/Botones/AjustesHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        botonSalir        = new BotonJuego("/Recursos/UI/Menu/Botones/Salir.png", "/Recursos/UI/Menu/Botones/SalirHover.png", "Recursos/Sonidos/UI/sonido3.wav");

        // --- BOTÓN 1: NUEVA PARTIDA ---
        gbc.gridy = 0;
        // Insets(arriba, izquierda, abajo, derecha) -> Margen superior de 250px y 80px desde el borde izquierdo
        gbc.insets = new Insets(300, 80, 15, 0); 
        add(botonNuevaPartida, gbc);

        // --- BOTON 2: CONTINUAR ---
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 80, 15, 0);
        add(botonContinuar, gbc);

        // --- BOTÓN 3: AJUSTES ---
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 80, 15, 0); 
        add(botonAjustes, gbc);

        // --- BOTÓN 4: SALIR ---
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 80, 0, 0); 
        add(botonSalir, gbc);
    }

    public JButton getBotonNuevaPartida() { return botonNuevaPartida; }
    public JButton getBotonContinuar() { return botonContinuar; }
    public JButton getBotonAjustes() { return botonAjustes; }
    public JButton getBotonSalir() { return botonSalir; }

    // sin partida guardada el menu queda como antes (sin boton de continuar)
    public void setContinuarVisible(boolean visible) {
        botonContinuar.setVisible(visible);
        revalidate();
        repaint();
    }

    private void cargarFondo() {
        try {
            imagenFondo = ImageIO.read(new File("src/Recursos/UI/Menu/Fondo/imagenMenu2.png"));
        } catch (IOException e) {
            System.out.println("No se pudo cargar el fondo del menu: " + e.getMessage());
            imagenFondo = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        particulas.dibujar(g2); // encima del fondo, debajo de botones/titulo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            particulas.dibujar(g2); // encima del fondo, debajo de botones/titulo
        }


}
}