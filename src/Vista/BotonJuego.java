package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonJuego extends JButton {

    private final ImageIcon iconoNormal;
    private final ImageIcon iconoHover;
    private Runnable accionHover;

    public BotonJuego(String rutaNormal, String rutaHover, Runnable accionHover, int ancho, int alto) {
        this.iconoNormal = cargarIcono(rutaNormal, ancho, alto);
        this.iconoHover = cargarIcono(rutaHover, ancho, alto);
        this.accionHover = accionHover;

        if (iconoNormal != null) {
            setIcon(iconoNormal);
        }
        aplicarEstiloBase();
        configurarHover();
    }

    // Sobrecargas de compatibilidad
    public BotonJuego(String rutaNormal, String rutaHover, int ancho, int alto) {
        this(rutaNormal, rutaHover, (Runnable) null, ancho, alto);
    }

    public BotonJuego(String rutaNormal, String rutaHover, Runnable accionHover) {
        this(rutaNormal, rutaHover, accionHover, -1, -1);
    }

    public BotonJuego(String rutaNormal, String rutaHover) {
        this(rutaNormal, rutaHover, (Runnable) null, -1, -1);
    }

    // Sobrecarga para mantener compatibilidad con las llamadas que pasaban la ruta String del sonido
    public BotonJuego(String rutaNormal, String rutaHover, String rutaSonidoHover, int ancho, int alto) {
        this(rutaNormal, rutaHover, (Runnable) null, ancho, alto);
        // Si se provee un manejador global de audio en Vista o vía setter, se asigna aquí
    }

    public BotonJuego(String rutaNormal, String rutaHover, String rutaSonidoHover) {
        this(rutaNormal, rutaHover, rutaSonidoHover, -1, -1);
    }

    public void setAccionHover(Runnable accionHover) {
        this.accionHover = accionHover;
    }

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
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
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
                if (accionHover != null) {
                    accionHover.run();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(iconoNormal);
            }
        });
    }
}