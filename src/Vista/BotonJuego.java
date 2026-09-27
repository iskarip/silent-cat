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

    // Constructor CON sonido de hover y recibe ancho y alto.
    public BotonJuego(String rutaNormal, String rutaHover, String rutaSonidoHover, int ancho, int alto) {
        this.iconoNormal = cargarIcono(rutaNormal, ancho, alto);
        this.iconoHover = cargarIcono(rutaHover, ancho, alto);
        this.rutaSonidoHover = rutaSonidoHover;

        if (iconoNormal != null) {
            setIcon(iconoNormal);
        }
        aplicarEstiloBase();
        configurarHover();
    }


    //Sobrecarga de constructores --> sirven para que no se rompa nada del
    // codigo anterior.
    // Constructor SIN sonido, para cuando no haga falta.
    public BotonJuego(String rutaNormal, String rutaHover, int ancho, int alto) {
        this(rutaNormal, rutaHover, null, ancho, alto);
    }

    public BotonJuego(String rutaNormal, String rutaHover, String rutaSonidoHover) {
        this(rutaNormal, rutaHover, rutaSonidoHover, -1, -1);
    }

    public BotonJuego(String rutaNormal, String rutaHover) {
        this(rutaNormal, rutaHover, null, -1, -1);
    }


    // modifique un poco este cargarIcono para que reciba alto y ancho para escalar las imagenes.
    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        if (ruta == null) return null;
        java.net.URL recurso = getClass().getResource(ruta);
        if (recurso == null) {
            System.out.println("No se encontro el recurso en: " + ruta);
            return null;
        }
        ImageIcon original = new ImageIcon(recurso);
        if (ancho > 0 && alto > 0) {
            Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(escalada);
        }
        return original;
    }


    private void aplicarEstiloBase() {
        setContentAreaFilled(false); // sin fondo gris
        setBorderPainted(false);     // sin borde
        setFocusPainted(false);      // sin rectangulo de foco
        setBorder(null);
        setFocusable(false);
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