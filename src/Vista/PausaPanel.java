package Vista;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

public class PausaPanel extends JPanel {

    private BotonJuego botonReanudar;
    private BotonJuego botonMenuPrincipal;

    public PausaPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setOpaque(false); // Sin fondo blanco para respetar la transparencia de los botones

        // 1. Obtener la resolución de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int anchoPantalla = screenSize.width;
        int altoPantalla = screenSize.height;

        // 2. Definir el tamaño del contenedor del menú
        int anchoPanel = 400; 
        int altoPanel = 300;

        // 3. Calcular la posición (X, Y) exacta para que quede justo en el centro
        int xCentro = (anchoPantalla - anchoPanel) / 2;
        int yCentro = (altoPantalla - altoPanel) / 2;

        setBounds(xCentro, yCentro, anchoPanel, altoPanel);

        // 4. Instanciación de los botones
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

        // 5. Ubicación vertical con GridBagLayout
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 0, 15, 0); // Espaciado entre botones
        add(botonReanudar, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        add(botonMenuPrincipal, gbc);

        setVisible(false);
    }

    public JButton getBotonReanudar() {
        return botonReanudar;
    }

    public JButton getBotonMenuPrincipal() {
        return botonMenuPrincipal;
    }
}