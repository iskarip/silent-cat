package Vista;

import Modelo.ReproductorSonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonJuego extends JButton {

    private final ImageIcon iconoNormal;
    private final ImageIcon iconoHover;
    private final String rutaSonidoHover; // puede ser null si no querés sonido

    // Constructor CON sonido de hover
    public BotonJuego(String rutaNormal, String rutaHover, String rutaSonidoHover) {
        this.iconoNormal = cargarIcono(rutaNormal);
        this.iconoHover = cargarIcono(rutaHover);
        this.rutaSonidoHover = rutaSonidoHover;

        setIcon(iconoNormal);
        aplicarEstiloBase();
        configurarHover();
    }

    // Constructor SIN sonido, para cuando no haga falta
    public BotonJuego(String rutaNormal, String rutaHover) {
        this(rutaNormal, rutaHover, null);
    }

    private ImageIcon cargarIcono(String ruta) {
        java.net.URL recurso = getClass().getResource(ruta);
        if (recurso == null) {
            System.out.println("No se encontro el recurso en: " + ruta);
            return null;
        }
        return new ImageIcon(recurso);
    }

    private void aplicarEstiloBase() {
        setContentAreaFilled(false); // sin fondo gris
        setBorderPainted(false);     // sin borde
        setFocusPainted(false);      // sin rectangulo de foco
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarHover() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (iconoHover != null) {
                    setIcon(iconoHover);
                }
                if (rutaSonidoHover != null) {
                    ReproductorSonido.reproducir(rutaSonidoHover);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(iconoNormal);
            }
        });
    }
}