package Vista;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class PausaPanel extends JPanel {

    private BotonJuego botonReanudar;
    private BotonJuego botonMenuPrincipal;

    public PausaPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setOpaque(false); // Permite ver la transparencia

        // 1. Ocupar TODA la pantalla de 800x600 (o la resolución de tu ventana)
        setBounds(0, 0, 800, 600); 

        // 2. Instanciación de los botones
        botonReanudar = new BotonJuego(
            "/Recursos/UI/Pausa/Botones/ReanudarPartida.png",
            "/Recursos/UI/Pausa/Botones/ReanudarPartidaHover.png", 
            "Recursos/Sonidos/UI/sonido3.wav"
        );
        
        botonMenuPrincipal = new BotonJuego(
            "/Recursos/UI/Pausa/Botones/MenuPrincipal.png",
            "/Recursos/UI/Pausa/Botones/MenuPrincipalHover.png", 
            "Recursos/Sonidos/UI/sonido3.wav"
        );

        // 3. GridBagLayout centrará los botones automáticamente en el medio de la pantalla
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(botonReanudar, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(botonMenuPrincipal, gbc);

        setVisible(false);
    }

    // --- DIBUJADO DE LA SOMBRA EN TODA LA PANTALLA ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;

        // Rojo semitransparente (Rojo: 255, Verde: 0, Azul: 0, Transparencia: 150)
        Color fondoRojoTransparente = new Color(255, 0, 0, 50); 

        g2d.setColor(fondoRojoTransparente);
        
        // Pinta desde (0, 0) hasta todo el ancho y alto del panel (toda la pantalla)
        g2d.fillRect(0, 0, getWidth(), getHeight()); 
    }


    public JButton getBotonReanudar() {
        return botonReanudar;
    }

    public JButton getBotonMenuPrincipal() {
        return botonMenuPrincipal;
    }
}