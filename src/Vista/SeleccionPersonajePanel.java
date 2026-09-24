package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SeleccionPersonajePanel extends JPanel {

    // -- ATRIBUTOS --

    public record OpcionPersonaje(String nombreMostrado, String genero, String carpetaSprites) {}

    private final List<OpcionPersonaje> opciones = List.of(
            new OpcionPersonaje("NOMBRE", "Chico", "/Recursos/Sprites/Personajes/Chico/"),
            new OpcionPersonaje("NOMBRE", "Chica", "/Recursos/Sprites/Personajes/Chica/")
    );

    private int indiceActual = 0;
    private GestorSprites spritesActual;

    private BotonJuego botonFlechaIzquierda;
    private BotonJuego botonFlechaDerecha;
    private BotonJuego botonIniciarPartida;

    private Timer timerIdle;
    private double tiempoIdle = 0;
    private int offsetY = 0;

    // -- CONSTRUCTOR --

    public SeleccionPersonajePanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        cargarSpritesOpcion(indiceActual);
        crearBotones();
        iniciarAnimacionIdle();
    }

    //-- METODOS--

    private void cargarSpritesOpcion(int indice) {
        String carpeta = opciones.get(indice).carpetaSprites();
        spritesActual = new GestorSprites(carpeta);
    }

    private void crearBotones() {
        botonFlechaIzquierda = new BotonJuego(
                "/Recursos/UI/Menu/Botones/flechaIzquierda.png",
                "/Recursos/UI/Menu/Botones/flechaIzquierdaHover.png"
        );
        botonFlechaIzquierda.setBounds(150, 320, 60, 60);
        botonFlechaIzquierda.addActionListener(e -> cambiarPersonaje(-1));

        botonFlechaDerecha = new BotonJuego(
                "/Recursos/UI/Menu/Botones/flechaDerecha.png",
                "/Recursos/UI/Menu/Botones/flechaDerechaHover.png"
        );
        botonFlechaDerecha.setBounds(590, 320, 60, 60);
        botonFlechaDerecha.addActionListener(e -> cambiarPersonaje(1));

        botonIniciarPartida = new BotonJuego(
                "/Recursos/UI/Menu/Botones/iniciarPartida.png",
                "/Recursos/UI/Menu/Botones/iniciarPartidaHover.png"
        );
        botonIniciarPartida.setBounds(280, 560, 200, 70);
        // Este sí es una decisión real (arrancar el juego), así que su
        // ActionListener lo va a registrar ControladorPrincipal, no acá.

        add(botonFlechaIzquierda);
        add(botonFlechaDerecha);
        add(botonIniciarPartida);
    }

    private void cambiarPersonaje(int direccion) {
        indiceActual = (indiceActual + direccion + opciones.size()) % opciones.size();
        cargarSpritesOpcion(indiceActual);
        repaint();
    }

    private void iniciarAnimacionIdle() {
        // 10 cuadros por segundo. (tipico de idle)
        timerIdle = new Timer(100, e -> {
            cuadroIdleActual++;
            repaint();
        });
        timerIdle.start();
    }

    // -- METODOS PARA EL CONTROLADO --

    public JButton getBotonIniciarPartida() { return botonIniciarPartida; }

    public String getGeneroSeleccionado() {
        return opciones.get(indiceActual).genero();
    }

    public String getNombreSeleccionado() {
        return opciones.get(indiceActual).nombreMostrado();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(230, 160, 60));
        g2.setFont(new Font("Arial", Font.BOLD, 42));
        g2.drawString("Character Select", getWidth() / 2 - 190, 80);

        int cartaAncho = 260, cartaAlto = 340;
        int cartaX = getWidth() / 2 - cartaAncho / 2;
        int cartaY = 180;
        g2.setColor(new Color(235, 190, 70));
        g2.fillRoundRect(cartaX, cartaY, cartaAncho, cartaAlto, 20, 20);
        g2.setColor(new Color(160, 100, 40));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(cartaX, cartaY, cartaAncho, cartaAlto, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        String nombre = getNombreSeleccionado();
        g2.drawString(nombre, getWidth() / 2 - nombre.length() * 6, cartaY + 40);

        if (spritesActual != null) {
            var sprite = spritesActual.obtenerCuadroIdle(cuadroIdleActual);
            if (sprite != null) {
                int spriteAncho = 120, spriteAlto = 120;
                int spriteX = getWidth() / 2 - spriteAncho / 2;
                int spriteY = cartaY + 90;
                g2.drawImage(sprite, spriteX, spriteY, spriteAncho, spriteAlto, this);
            }
        }
    }
}