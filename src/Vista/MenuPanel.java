package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPanel extends JPanel {

    private JuegoFrame ventanaPrincipal;

    public MenuPanel(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // espacio entre los botones
        gbc.gridx = 0;

        JButton botonIniciar = new JButton("Iniciar");
        botonIniciar.setPreferredSize(new Dimension(200, 50));
        botonIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        botonIniciar.addActionListener(e -> ventanaPrincipal.mostrarPantalla("seleccion"));

        JButton botonSalir = new JButton("Salir");
        botonSalir.setPreferredSize(new Dimension(200, 50));
        botonSalir.setFont(new Font("Arial", Font.BOLD, 18));
        botonSalir.addActionListener((e -> System.exit(0)));

        gbc.gridy = 0;
        add (botonIniciar, gbc);

        gbc.gridy = 1;
        add (botonSalir, gbc);

    }
}

