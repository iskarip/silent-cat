package Vista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPersonajePanel extends JPanel {

    // -- ATRIBUTOS --

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

    // Animación idle del personaje
    private Timer timerAnimacion;
    private int cuadroIdleActual = 0;

    // Constantes de dimensiones
    private static final int ANCHO_FRAME = NivelPanel.ANCHO_CUADRO;
    private static final int ALTO_FRAME = NivelPanel.ALTO_CUADRO;
    private static final int TOTAL_FRAMES_IDLE = 4;

    // Escala del personaje principal y del de fondo
    private static final double ESCALA_ACTIVO = 6.8;
    private static final double ESCALA_FONDO  = 4.0;

    public SeleccionPersonajePanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        cargarFondo();
        precargarGestores();
        crearComponentesUI();
        iniciarAnimacion();
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
        // Nombres exactos de tus archivos en /Recursos/UI/Menu/Botones/
        botonFlechaIzquierda = crearBotonPNG("/Recursos/UI/Menu/Botones/FlechaIzquierda.png", "<", 85, 70);
        botonFlechaDerecha   = crearBotonPNG("/Recursos/UI/Menu/Botones/FlechaDerecha.png", ">", 85, 70);
        botonIniciarPartida  = crearBotonPNG("/Recursos/UI/Menu/Botones/botonIniciarPartida.png", "INICIAR PARTIDA", 330, 115);
        botonVolverMenu      = crearBotonPNG("/Recursos/UI/Menu/Botones/botonVolver.png", "VOLVER", 170, 60);

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

    private void iniciarAnimacion() {
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
        int altoPanel = getHeight();

        // 1. BOTÓN VOLVER
        int anchoVolver = 210;
        int altoVolver = 75;
        if (botonVolverMenu != null) {
            botonVolverMenu.setBounds(35, altoPanel - altoVolver - 30, anchoVolver, altoVolver);
        }

        // 2. FLECHAS LATERALES
        int anchoFlecha = 85;
        int altoFlecha = 70;
        int desfasajeX = 220;
        int desfasajeY = 75;

        if (botonFlechaIzquierda != null) {
            botonFlechaIzquierda.setBounds(centroX - desfasajeX - (anchoFlecha / 2), (centroY + desfasajeY) - (altoFlecha / 2), anchoFlecha, altoFlecha);
        }
        if (botonFlechaDerecha != null) {
            botonFlechaDerecha.setBounds(centroX + desfasajeX - (anchoFlecha / 2), (centroY + desfasajeY) - (altoFlecha / 2), anchoFlecha, altoFlecha);
        }

        // 3. BOTÓN INICIAR PARTIDA
        if (botonIniciarPartida != null) {
            int anchoIniciar = 330;
            int altoIniciar = 115;

            botonIniciarPartida.setBounds(centroX - (anchoIniciar / 2), centroY + 350, anchoIniciar, altoIniciar);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // 1. DIBUJAR FONDO COMPLETO
        if (imagenFondo != null) {
            g2.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;


        int marcoOffsetY = 75;

        // 2. DIBUJAR PERSONAJE INACTIVO TRANSPARENTE EN EL FONDO (30% OPACIDAD)
        int indiceInactivo = (indiceActual + 1) % opciones.size();
        if (indiceInactivo < listaGestores.size()) {
            GestorSprites gestorInactivo = listaGestores.get(indiceInactivo);
            BufferedImage hojaInactiva = gestorInactivo.obtener(EstadoPersonaje.IDLE);

            if (hojaInactiva != null) {
                Composite compOriginal = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));

                int anchoInactivo = (int) (ANCHO_FRAME * ESCALA_FONDO);
                int altoInactivo  = (int) (ALTO_FRAME * ESCALA_FONDO);

                // Se coloca a la derecha o izquierda en la penumbra
                int fondoX = (indiceActual == 0) ? (centroX + 235) : (centroX - 235 - anchoInactivo);
                int fondoY = (centroY + marcoOffsetY) - (altoInactivo / 2);

                g2.drawImage(hojaInactiva,
                        fondoX, fondoY, fondoX + anchoInactivo, fondoY + altoInactivo,
                        0, 0, ANCHO_FRAME, ALTO_FRAME, this);

                g2.setComposite(compOriginal); // Restablecer opacidad completa
            }
        }

        // 3. DIBUJAR SPRITE ACTIVO CENTRADO ADENTRO DEL MARCO GÓTICO
        if (indiceActual < listaGestores.size()) {
            GestorSprites gestorActivo = listaGestores.get(indiceActual);
            BufferedImage hojaActiva = gestorActivo.obtener(EstadoPersonaje.IDLE);

            if (hojaActiva != null) {
                int spriteAncho = (int) (ANCHO_FRAME * ESCALA_ACTIVO);
                int spriteAlto  = (int) (ALTO_FRAME * ESCALA_ACTIVO);

                // Cuadro actual de la animación (fila 0 de frente)
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

    // --- MÉTODOS DE CONSULTA MVC ---

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