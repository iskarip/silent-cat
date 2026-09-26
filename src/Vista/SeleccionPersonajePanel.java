package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class SeleccionPersonajePanel extends JPanel {

    // -- ATRIBUTOS --

    public record OpcionPersonaje(String nombreMostrado, String genero, String carpetaSprites) {
    }

    private final List<OpcionPersonaje> opciones = List.of(
            new OpcionPersonaje("NOMBRE1", "Chico", "/Recursos/Sprites/Personajes/Chico/"),
            new OpcionPersonaje("NOMBRE2", "Chica", "/Recursos/Sprites/Personajes/Chica/")
    );

    private int indiceActual = 0;
    private GestorSprites spritesActual;
    private Image imagenFondo;

    private JButton botonFlechaIzquierda;
    private JButton botonFlechaDerecha;
    private JButton botonIniciarPartida;

    private Timer timerIdle;
    private int cuadroIdleActual = 0;

    // -- CONSTRUCTOR --

    public SeleccionPersonajePanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        cargarFondo();
        cargarSpritesOpcion(indiceActual);
        crearBotones();
        iniciarAnimacionIdle();
    }

    // -- METODOS --

    private void cargarFondo() {
        try (var is = getClass().getResourceAsStream("/Recursos/UI/Menu/Fondo/imagenSeleccionPersonaje.png")) {
            if (is != null) {
                imagenFondo = ImageIO.read(is);
            } else {
                System.out.println("No se encontró la imagen de fondo de selección.");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar fondo de selección: " + e.getMessage());
        }
    }

    private void cargarSpritesOpcion(int indice) {
        String carpeta = opciones.get(indice).carpetaSprites();
        spritesActual = new GestorSprites(carpeta);
    }

    private void crearBotones() {
        botonFlechaIzquierda = new JButton("<");
        botonFlechaIzquierda.setBounds(150, 320, 60, 60);
        botonFlechaIzquierda.setFont(new Font("Arial", Font.BOLD, 24));
        botonFlechaIzquierda.addActionListener(e -> cambiarPersonaje(-1));

        botonFlechaDerecha = new JButton(">");
        botonFlechaDerecha.setBounds(590, 320, 60, 60);
        botonFlechaDerecha.setFont(new Font("Arial", Font.BOLD, 24));
        botonFlechaDerecha.addActionListener(e -> cambiarPersonaje(1));

        botonIniciarPartida = new JButton("Iniciar Partida");
        botonIniciarPartida.setBounds(280, 560, 200, 70);
        botonIniciarPartida.setFont(new Font("Arial", Font.BOLD, 18));

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
        timerIdle = new Timer(150, e -> {
            cuadroIdleActual = (cuadroIdleActual + 1) % 4; // 4 columnas de frames
            repaint();
        });
        timerIdle.start();
    }

    // -- METODOS PARA EL CONTROLADOR --

    public JButton getBotonIniciarPartida() {
        return botonIniciarPartida;
    }

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

        if (imagenFondo != null) {
            g2.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        /*

        g2.setColor(new Color(230, 160, 60));
        g2.setFont(new Font("Arial", Font.BOLD, 42));
        g2.drawString("Seleccione su personaje", getWidth() / 2 - 230, 80);

                 */

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
            BufferedImage hoja = spritesActual.obtener(EstadoPersonaje.IDLE);
            if (hoja != null) {
                int anchoFrame = NivelPanel.ANCHO_CUADRO;
                int altoFrame = NivelPanel.ALTO_CUADRO;
                int srcX = cuadroIdleActual * anchoFrame;
                int srcY = 0; // fila 0 = mirando hacia abajo/frente, ajustar según su convención

                int spriteAncho = 120, spriteAlto = 120;
                int spriteX = getWidth() / 2 - spriteAncho / 2;
                int spriteY = cartaY + 90;

                g2.drawImage(hoja,
                        spriteX, spriteY, spriteX + spriteAncho, spriteY + spriteAlto,
                        srcX, srcY, srcX + anchoFrame, srcY + altoFrame,
                        this);
            }
        }
    }

}