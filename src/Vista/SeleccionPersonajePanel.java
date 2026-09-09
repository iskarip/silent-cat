package Vista;


import Modelo.Arma;
import Modelo.Linterna;
import Modelo.Personaje;

import javax.swing.*;
import java.awt.*;

/*La pantalla que definimos donde el usuario va a elegir
con que personaje (hombre o mujer) va a ir a buscar al gato. */


public class SeleccionPersonajePanel extends JPanel {

    private JuegoFrame ventanaPrincipal;

    public SeleccionPersonajePanel (JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        JButton botonHombre = new JButton("Hombre");
        JButton botonMujer = new JButton("Mujer");

        botonHombre.addActionListener(e -> elegirPersonaje("Protagonista", Color.GREEN));
        botonMujer.addActionListener(e -> elegirPersonaje ("Protagonista", Color.BLUE));

        add(botonHombre);
        add(botonMujer);
    }

    private void elegirPersonaje(String nombre, Color color) {
        //TODO: aca se agrega el personaje real

        Arma arma = new Arma();
        Linterna linterna = new Linterna();
        Personaje personaje = new Personaje(nombre, arma, linterna);

        //TODO: falta decidir como este Personaje llega hasta NivelPanel.
        // Ahora mismo NivelPanel no tiene forma de recibirlo

        ventanaPrincipal.iniciarNivelConPersonaje(personaje, color);
    }


}
