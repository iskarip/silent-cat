package Vista;

import javax.swing.*;

public class SeleccionPersonajePanel extends JPanel {

    private JButton botonHombre;
    private JButton botonMujer;

    public SeleccionPersonajePanel() { // Sin JuegoFrame
        botonHombre = new JButton("Hombre");
        botonMujer = new JButton("Mujer");

        add(botonHombre);
        add(botonMujer);
    }

    public JButton getBotonHombre() { return botonHombre; }
    public JButton getBotonMujer() { return botonMujer; }
}
