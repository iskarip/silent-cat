package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPersonajePanel extends JPanel {

    // Record inmutable para modelar las opciones de personaje
    public record OpcionPersonaje(String genero, String carpetaSprites) {}

    private final List<OpcionPersonaje> opciones = List.of(
            new OpcionPersonaje("Chico", "/Recursos/Sprites/Personajes/Chico/"),
            new OpcionPersonaje("Chica", "/Recursos/Sprites/Personajes/Chica/")
    );

    private final List<GestorSprites> listaGestores = new ArrayList<>();

    private int indiceActual = 0;
    private Image imagenFondo;

    // Componentes de interfaz
    private JButton botonFlechaIzquierda;
    private JButton botonFlechaDerecha;
    private JButton botonIniciarPartida;
    private JButton botonVolverMenu;

    // Control de animación
    private Timer timerAnimacion;
    private int cuadroIdleActual = 0;

    // Dimensiones estándar del frame de animación
    private static final int ANCHO_FRAME = NivelPanel.ANCHO_CUADRO;
    private static final int ALTO_FRAME = NivelPanel.ALTO_CUADRO;
    private static final int TOTAL_FRAMES_IDLE = 4;

    // Factor para que el personaje encaje dentro del marco de la imagen.
    private static final double ESCALA_SPRITE = 4.8;

    public SeleccionPersonajePanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        cargarFondo();
        precargarGestores();
        crearComponentesUI();
        iniciarAnimacionIdle();
    }


    private void cargarFondo() {
        try (InputStream is = getClass().getResourceAsStream("/Recursos/UI/Menu/Fondo/imagenSeleccionPersonaje.png")) {
            if (is != null) {
                this.imagenFondo = ImageIO.read(is);
            } else {
                System.out.println("[SeleccionPersonajePanel] Imagen de fondo no encontrada.");
            }
        } catch (Exception e) {
            System.out.println("[SeleccionPersonajePanel] Error al cargar fondo: " + e.getMessage());
        }
    }


     // Precarga los sprites de Chico y Chica en memoria.
    private void precargarGestores() {
        for (OpcionPersonaje opcion : opciones) {
            listaGestores.add(new GestorSprites(opcion.carpetaSprites()));
        }
    }


     //Configura los botones de navegación (invisibles sobre las flechas) y de acción.

    private void crearComponentesUI() {
        // Flecha izquierda invisible
        botonFlechaIzquierda = new JButton();
        configurarBotonInvisible(botonFlechaIzquierda);
        botonFlechaIzquierda.addActionListener(e -> alternarSeleccion(-1));

        // Flecha derecha invisible
        botonFlechaDerecha = new JButton();
        configurarBotonInvisible(botonFlechaDerecha);
        botonFlechaDerecha.addActionListener(e -> alternarSeleccion(1));

        // Botón Iniciar Partida
        botonIniciarPartida = new JButton("INICIAR PARTIDA");
        botonIniciarPartida.setFont(new Font("Arial", Font.BOLD, 18));
        botonIniciarPartida.setFocusable(false);

        // Botón Volver
        botonVolverMenu = new JButton("VOLVER");
        botonVolverMenu.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolverMenu.setFocusable(false);

        add(botonFlechaIzquierda);
        add(botonFlechaDerecha);
        add(botonIniciarPartida);
        add(botonVolverMenu);
    }


     //Hace transparente un JButton para que actúe de hitbox interactiva sobre el arte.

    private void configurarBotonInvisible(JButton boton) {
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void alternarSeleccion(int paso) {
        indiceActual = (indiceActual + paso + opciones.size()) % opciones.size();
        cuadroIdleActual = 0;
        repaint();
    }


     // Cicla la animación Idle del personaje activo a ~140ms por cuadro.
    private void iniciarAnimacionIdle() {
        timerAnimacion = new Timer(140, e -> {
            cuadroIdleActual = (cuadroIdleActual + 1) % TOTAL_FRAMES_IDLE;
            repaint();
        });
        timerAnimacion.start();
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        // Botón Volver (esquina superior izquierda)
        botonVolverMenu.setBounds(40, 40, 110, 40);

        // Flechas invisibles calibradas sobre los adornos góticos laterales
        int tamFlecha = 75;
        int desfasajeY = -15; // Alineación vertical con las flechas del fondo
        botonFlechaIzquierda.setBounds(centroX - 195, centroY + desfasajeY, tamFlecha, tamFlecha);
        botonFlechaDerecha.setBounds(centroX + 120, centroY + desfasajeY, tamFlecha, tamFlecha);

        // Botón Iniciar Partida sobre la alfombra
        int anchoIniciar = 240;
        int altoIniciar = 50;
        botonIniciarPartida.setBounds(centroX - (anchoIniciar / 2), centroY + 220, anchoIniciar, altoIniciar);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Interpolación cercana para mantener el pixel art nítido
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // 1. Dibujar el fondo completo de la mansión
        if (imagenFondo != null) {
            g2.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        // 2. Dibujar el Sprite activo centrado adentro del marco tallado
        if (indiceActual < listaGestores.size()) {
            GestorSprites gestorActivo = listaGestores.get(indiceActual);
            BufferedImage hojaActiva = gestorActivo.obtener(EstadoPersonaje.IDLE);

            if (hojaActiva != null) {
                int spriteAncho = (int) (ANCHO_FRAME * ESCALA_SPRITE);
                int spriteAlto  = (int) (ALTO_FRAME * ESCALA_SPRITE);

                // Corte del cuadro de animación correspondiente (fila 0 de frente)
                int srcX1 = cuadroIdleActual * ANCHO_FRAME;
                int srcY1 = 0;
                int srcX2 = srcX1 + ANCHO_FRAME;
                int srcY2 = srcY1 + ALTO_FRAME;

                // Coordenadas centradas con leve ajuste vertical (-15) para el hueco del marco
                int destX = centroX - (spriteAncho / 2);
                int destY = (centroY - 15) - (spriteAlto / 2);

                g2.drawImage(hojaActiva,
                        destX, destY, destX + spriteAncho, destY + spriteAlto,
                        srcX1, srcY1, srcX2, srcY2, this);
            }
        }
    }

    // --- MÉTODOS DE ACCESO PARA EL CONTROLADOR (MVC) ---

    public JButton getBotonIniciarPartida() {
        return botonIniciarPartida;
    }

    public JButton getBotonVolverMenu() {
        return botonVolverMenu;
    }

    public String getGeneroSeleccionado() {
        return opciones.get(indiceActual).genero();
    }
}