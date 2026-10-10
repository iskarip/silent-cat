package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPersonajePanel extends JPanel {

    public record OpcionPersonaje(String genero, String carpetaSprites) {}

    private final List<OpcionPersonaje> opciones = List.of(
            new OpcionPersonaje("Chico", "/Recursos/Sprites/Personajes/Chico/"),
            new OpcionPersonaje("Chica", "/Recursos/Sprites/Personajes/Chica/")
    );

    private final List<GestorSprites> listaGestores = new ArrayList<>();
    private int indiceActual = 0;
    private Image imagenFondo;

    // Partículas de ambiente
    private ParticulasAmbiente particulasAmbiente;

    // Componentes de interfaz
    private JButton botonFlechaIzquierda;
    private JButton botonFlechaDerecha;
    private JButton botonIniciarPartida;
    private JButton botonVolverMenu;

    // Animación y loop visual
    private Timer timerLoop;
    private int cuadroIdleActual = 0;
    private int contadorTicks = 0;

    // Constantes de dimensiones
    private static final int ANCHO_FRAME = NivelPanel.ANCHO_CUADRO;
    private static final int ALTO_FRAME = NivelPanel.ALTO_CUADRO;
    private static final int TOTAL_FRAMES_IDLE = 4;

    private static final double ESCALA_ACTIVO = 7.5;
    private static final int ANCHO_FLECHA = 140;
    private static final int ALTO_FLECHA  = 95;

    public SeleccionPersonajePanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        // Inicializamos las partículas con una cantidad acorde (ej: 45 partículas)
        particulasAmbiente = new ParticulasAmbiente(45, 1280, 720);

        // Actualizar dimensiones si la ventana cambia de tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    particulasAmbiente.setTamanio(getWidth(), getHeight());
                }
            }
        });

        cargarFondo();
        precargarGestores();
        crearComponentesUI();
        iniciarLoop();
    }

    private void cargarFondo() {
        try (InputStream is = getClass().getResourceAsStream("/Recursos/UI/Menu/Fondo/seleccionPersonaje.png")) {
            if (is != null) {
                this.imagenFondo = ImageIO.read(is);
            } else {
                System.out.println("[SeleccionPersonajePanel] Fondo no encontrado.");
            }
        } catch (Exception e) {
            System.out.println("[SeleccionPersonajePanel] Error al leer fondo: " + e.getMessage());
        }
    }

    private void precargarGestores() {
        for (OpcionPersonaje opcion : opciones) {
            listaGestores.add(new GestorSprites(opcion.carpetaSprites()));
        }
    }

    private JButton crearBotonPNG(String rutaRecurso, String textoAlternativo, int ancho, int alto) {
        JButton boton = new JButton();
        ImageIcon icono = null;

        try {
            java.net.URL url = getClass().getResource(rutaRecurso);
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
                Image escalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                icono = new ImageIcon(escalada);
            } else {
                System.out.println("[SeleccionPersonajePanel] No se encontró el recurso: " + rutaRecurso);
            }
        } catch (Exception ignored) {}

        if (icono != null) {
            boton.setIcon(icono);
        } else {
            boton.setText(textoAlternativo);
            boton.setFont(new Font("Arial", Font.BOLD, 18));
            boton.setForeground(new Color(220, 180, 70));
        }

        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setBorder(null);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void crearComponentesUI() {
        botonFlechaIzquierda = crearBotonPNG("/Recursos/UI/Menu/Botones/FlechaIzquierda.png", "<", ANCHO_FLECHA, ALTO_FLECHA);
        botonFlechaDerecha   = crearBotonPNG("/Recursos/UI/Menu/Botones/FlechaDerecha.png", ">", ANCHO_FLECHA, ALTO_FLECHA);
        botonIniciarPartida  = crearBotonPNG("/Recursos/UI/Menu/Botones/botonIniciarPartida.png", "INICIAR PARTIDA", 330, 115);
        botonVolverMenu      = crearBotonPNG("/Recursos/UI/Menu/Botones/botonVolver.png", "VOLVER", 210, 75);

        botonFlechaIzquierda.addActionListener(e -> alternarSeleccion(-1));
        botonFlechaDerecha.addActionListener(e -> alternarSeleccion(1));

        add(botonFlechaIzquierda);
        add(botonFlechaDerecha);
        add(botonIniciarPartida);
        add(botonVolverMenu);
    }

    private void alternarSeleccion(int paso) {
        indiceActual = (indiceActual + paso + opciones.size()) % opciones.size();
        cuadroIdleActual = 0;
        repaint();
    }

    private void iniciarLoop() {
        // Corre a ~60 FPS (16 ms) para que el polvo flote suavemente
        timerLoop = new Timer(16, e -> {
            if (particulasAmbiente != null) {
                particulasAmbiente.actualizar();
            }

            // Cada ~144 ms (9 ticks de 16 ms) avanza el frame del sprite del personaje
            contadorTicks++;
            if (contadorTicks >= 9) {
                cuadroIdleActual = (cuadroIdleActual + 1) % TOTAL_FRAMES_IDLE;
                contadorTicks = 0;
            }

            repaint();
        });
        timerLoop.start();
    }

    @Override
    public void doLayout() {
        super.doLayout();

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int altoPanel = getHeight();

        // 1. BOTÓN VOLVER
        int anchoVolver = 210;
        int altoVolver = 75;
        if (botonVolverMenu != null) {
            botonVolverMenu.setBounds(35, altoPanel - altoVolver - 30, anchoVolver, altoVolver);
        }

        // 2. FLECHAS LATERALES
        int desfasajeX = 265;
        int desfasajeY = 30;

        if (botonFlechaIzquierda != null) {
            botonFlechaIzquierda.setBounds(
                    centroX - desfasajeX - (ANCHO_FLECHA / 2),
                    (centroY + desfasajeY) - (ALTO_FLECHA / 2),
                    ANCHO_FLECHA,
                    ALTO_FLECHA
            );
        }
        if (botonFlechaDerecha != null) {
            botonFlechaDerecha.setBounds(
                    centroX + desfasajeX - (ANCHO_FLECHA / 2),
                    (centroY + desfasajeY) - (ALTO_FLECHA / 2),
                    ANCHO_FLECHA,
                    ALTO_FLECHA
            );
        }

        // 3. BOTÓN INICIAR PARTIDA
        if (botonIniciarPartida != null) {
            int anchoIniciar = 330;
            int altoIniciar = 115;
            botonIniciarPartida.setBounds(centroX - (anchoIniciar / 2), centroY + 330, anchoIniciar, altoIniciar);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. DIBUJAR FONDO COMPLETO
        if (imagenFondo != null) {
            g2.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        // 2. DIBUJAR PARTÍCULAS DE AMBIENTE (Polvo flotando en el escenario)
        if (particulasAmbiente != null) {
            particulasAmbiente.dibujar(g2);
        }

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int marcoOffsetY = 30;

        // 3. SPRITE ACTIVO DEL PERSONAJE DENTRO DEL RECUADRO
        if (indiceActual < listaGestores.size()) {
            GestorSprites gestorActivo = listaGestores.get(indiceActual);
            BufferedImage hojaActiva = gestorActivo.obtener(EstadoPersonaje.IDLE);

            if (hojaActiva != null) {
                int spriteAncho = (int) (ANCHO_FRAME * ESCALA_ACTIVO);
                int spriteAlto  = (int) (ALTO_FRAME * ESCALA_ACTIVO);

                int srcX1 = cuadroIdleActual * ANCHO_FRAME;
                int srcY1 = 0;
                int srcX2 = srcX1 + ANCHO_FRAME;
                int srcY2 = srcY1 + ALTO_FRAME;

                int destX = centroX - (spriteAncho / 2);
                int destY = (centroY + marcoOffsetY) - (spriteAlto / 2);

                g2.drawImage(hojaActiva,
                        destX, destY, destX + spriteAncho, destY + spriteAlto,
                        srcX1, srcY1, srcX2, srcY2, this);
            }
        }
    }

    public JButton getBotonIniciarPartida() { return botonIniciarPartida; }
    public JButton getBotonVolverMenu() { return botonVolverMenu; }
    public String getGeneroSeleccionado() { return opciones.get(indiceActual).genero(); }
}