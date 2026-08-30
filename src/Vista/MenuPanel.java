package Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuPanel extends JPanel {

    private JuegoFrame ventanaPrincipal;

    public MenuPanel(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        JButton botonIniciar = new JButton("Iniciar");
        botonIniciar.addActionListener(e -> {
            ventanaPrincipal.mostrarPantalla("seleccion");
        });

        JButton botonSalir = new JButton("Salir");
        botonSalir.addActionListener(e -> System.exit(0));

        add(botonIniciar);
        add(botonSalir);
    }
}

