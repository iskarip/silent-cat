package Vista;

import Modelo.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class NivelPanel extends JPanel {

    // --- MODELO Y ESTADO DEL NIVEL ---
    private Nivel nivelActual;
    private Image imagenFondoNivel; // imagen de planta baja

//___________________CAPAS VISUALES________________
private final List<InterfazVisual> capasVisuales = new ArrayList<>();
    

    // --- ENTIDADES Y SPRITES ---
    private Personaje personaje;
    private GestorSprites gestorSprites;
    private GestorSprites spritesEnemigo;

    // --- ESTADO VISUAL Y ANIMACIÓN ---
    private EstadoPersonaje estadoActual = EstadoPersonaje.IDLE;
    //public static final double ZOOM = 1.75; // Zoom del personaje
    private int cuadroAnimacion = 0;
    private int contadorTick = 0;

    // --- CÁMARA ---
    private int camaraX = 0;
    private int camaraY = 0;

    // --- ELEMENTOS DE INTERFAZ (GAME OVER) ---
    private JLabel etiquetaGameOver;
    private JButton botonVolverMenu;
    private int ticksMuerte = 0;
    private boolean gameOverMostrado = false;

    // -- BOTONES PARA PAUSAR PARTIDA, REANUDAR y VOLVER AL MENU --
    private JButton botonPausa;
    private JPanel panelPausa;
    private JButton botonReanudar;
    private JButton botonMenuPrincipal;

    //CONSTANTES DE ESCALADO Y ZOOM
    public static final double ZOOM = 1.85;
    private static final int ANCHO_MAPA_PX = 50 * 32;
    private static final int ALTO_MAPA_PX  = 24 * 32;


    public static final double ESCALA = 2.5;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;


    public NivelPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setLayout(null); // posicionamiento libre, para superponer botones al dibujo
        setBackground(Color.BLACK); // <-- Fondo negro
        
        crearBotonPausa();
        crearPanelPausa();
    

        // Componentes flotantes para la pantalla de Game Over
        etiquetaGameOver = new JLabel("GAME OVER", SwingConstants.CENTER);
        etiquetaGameOver.setFont(new Font("Arial", Font.BOLD, 48));
        etiquetaGameOver.setForeground(Color.RED);
        etiquetaGameOver.setBounds(250, 200, 300, 60);
        etiquetaGameOver.setVisible(false);
        add(etiquetaGameOver);

        botonVolverMenu = new JButton("Volver al menú");
        botonVolverMenu.setBounds(320, 280, 160, 40);
        botonVolverMenu.setVisible(false);
        add(botonVolverMenu);

        // Permite recuperar el foco del teclado al hacer clic sobre el canvas de juego
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    // --- MÉTODOS PARA GESTIÓN DE CAPAS VISUALES / OVERLAYS ---
    public void agregarCapaVisual(InterfazVisual capa) {
        if (!capasVisuales.contains(capa)) {
            capasVisuales.add(capa);
        }
    }//este metodo llena la lista de capas visuales

    public void removerCapaVisual(InterfazVisual capa) {
        capasVisuales.remove(capa);
    }

    // --- LÓGICA DE CÁMARA (CÁLCULO EXCLUSIVAMENTE VISUAL) ---
    public void actualizarCamara() {
        if (personaje == null) return;

        // 1. Centro exacto del personaje
        int centroPersonajeX = personaje.getPosicionX() + (int) ((ANCHO_CUADRO * ESCALA) / 2);
        int centroPersonajeY = personaje.getPosicionY() + (int) ((ALTO_CUADRO * ESCALA) / 2);

        // 2. Área visible en pantalla bajo este factor de zoom
        int anchoVisible = (int) (getWidth() / ZOOM);
        int altoVisible  = (int) (getHeight() / ZOOM);

        // 3. Posición ideal centrada
        int objetivoX = centroPersonajeX - (anchoVisible / 2);
        int objetivoY = centroPersonajeY - (altoVisible / 2);

        // 4. Clamping: la cámara se frena al llegar al borde de los 50x24 bloques[cite: 14]
        int maxCamX = Math.max(0, ANCHO_MAPA_PX - anchoVisible);
        int maxCamY = Math.max(0, ALTO_MAPA_PX - altoVisible);

        this.camaraX = Math.max(0, Math.min(objetivoX, maxCamX));
        this.camaraY = Math.max(0, Math.min(objetivoY, maxCamY));
    }

    // --- GETTERS Y SETTERS ---
    public Personaje getPersonaje() { return personaje; }
    public void setPersonaje(Personaje personaje) { this.personaje = personaje; repaint(); }

    public GestorSprites getGestorSprites() { return gestorSprites; }
    public void setGestorSprites(GestorSprites gestorSprites) { this.gestorSprites = gestorSprites; repaint(); }

    public GestorSprites getSpritesEnemigo() { return spritesEnemigo; }
    public void setSpritesEnemigo(GestorSprites spritesEnemigo) { this.spritesEnemigo = spritesEnemigo; }

    public void setEstado(EstadoPersonaje estado) { this.estadoActual = estado; repaint(); }

    public Nivel getNivelActual() { return nivelActual; }

    public void setNivelActual(Nivel nivel) {
        this.nivelActual = nivel;
        if (nivel != null) {
            //carga la imagen de fondo
            this.imagenFondoNivel = cargarImagenFondo(nivel.getRutaImagenFondo());

        }
        repaint();
    }
    public int getCamaraX() { 
        return camaraX; 
    }

    public int getCamaraY() { 
        return camaraY; 
    }



    public JButton getBotonPausa(){
        return botonPausa;
    }

    public JButton getBotonReanudar (){
        return botonReanudar;
    }

    public JButton getBotonMenuPrincipal() {
        return botonMenuPrincipal;
    }

    public JButton getBotonVolverMenu() {
        return botonVolverMenu;
    }

    public void mostrarPausa () {
        panelPausa.setVisible(true);
    }

    public void ocultarPausa () {
        panelPausa.setVisible(false);
    }

    // -- BOTONES PARA PAUSAR PARTIDA, REANUDAR y VOLVER AL MENU --

    private void crearBotonPausa () {
        botonPausa = new JButton("PAUSAR");
        botonPausa.setBounds(700, 10, 90, 35); // ubicacion del boton
        add (botonPausa);
    }

    // --- ANIMACIÓN ---

    // --- DELIMITACIÓN DE HITBOXES PARA RENDERIZADO / FÍSICA ---

    private void crearPanelPausa () {
        panelPausa = new JPanel();
        panelPausa.setLayout(new GridLayout(2, 1, 0, 10));
        panelPausa.setBounds(300, 200, 200, 100);
        panelPausa.setBackground(new Color(0, 0, 0, 180)); //negro semitransparente

        botonReanudar = new JButton("REANUDAR PARTIDA");
        botonMenuPrincipal = new JButton("MENU PRINCIPAL");

        panelPausa.add(botonReanudar);
        panelPausa.add(botonMenuPrincipal);

        panelPausa.setVisible(false);
        add(panelPausa);
    }


    // --- MANEJO DE ANIMACIONES DE SPRITES ---
    public void actualizarAnimacion(boolean moviendose) {
        if (moviendose) {
            contadorTick++;
            if (contadorTick % 6 == 0) {
                cuadroAnimacion = (cuadroAnimacion + 1) % 6;
            }
        } else {
            cuadroAnimacion = 0;
        }
    }

    public void avanzarAnimacionMuerte() {
        ticksMuerte++;
        if (ticksMuerte % 6 != 0) return;

        int totalFrames = obtenerTotalFramesMuerte(gestorSprites);
        if (cuadroAnimacion < totalFrames - 1) {
            cuadroAnimacion++;
        } else if (!gameOverMostrado) {
            gameOverMostrado = true;
            mostrarGameOver();
        }
    }

    private int obtenerTotalFramesMuerte(GestorSprites sprites) {
        if (sprites == null) return 1;
        BufferedImage hoja = sprites.obtener(EstadoPersonaje.MURIENDO);
        return (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);
    }

    public int obtenerTotalFramesMuerteEnemigo() {
        return obtenerTotalFramesMuerte(spritesEnemigo);
    }

    public void mostrarGameOver() {
        etiquetaGameOver.setVisible(true);
        botonVolverMenu.setVisible(true);
    }

    public void reiniciarEstadoNivel() {
        ticksMuerte = 0;
        gameOverMostrado = false;
        cuadroAnimacion = 0;
        estadoActual = EstadoPersonaje.IDLE;
        etiquetaGameOver.setVisible(false);
        botonVolverMenu.setVisible(false);
    }

    // --- CICLO DE DIBUJADO (PAINT COMPONENT) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (personaje == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // 0. PANTALLA EN NEGRO
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 1. TRANSFORMACIÓN DE CÁMARA
        actualizarCamara();
        AffineTransform transformOriginal = g2d.getTransform();

        // Se escala una sola vez y se traslada una sola vez
        g2d.scale(ZOOM, ZOOM);
        g2d.translate(-camaraX, -camaraY);

        // 2. DIBUJAR PISO Y OBSTÁCULOS (PALETA NEGRO Y AMARILLO)
        if (nivelActual != null && nivelActual.getMapaColision() != null) {
            MapaColision mapaColision = nivelActual.getMapaColision();
            int tileSize = 32;
            int totalFilas = 24;
            int totalColumnas = 50;

            // Colores temáticos:
            Color paredRelleno = new Color(15, 15, 18);
            Color paredBorde   = new Color(30, 30, 35);

            Color sueloRelleno = new Color(212, 175, 55);
            Color sueloBorde   = new Color(175, 140, 30);

            for (int f = 0; f < totalFilas; f++) {
                for (int c = 0; c < totalColumnas; c++) {
                    int x = c * tileSize;
                    int y = f * tileSize;

                    if (!mapaColision.esPosicionValida(x + 16, y + 16)) {
                        // PARED / OBSTÁCULO (1 en el txt) -> Negro
                        g2d.setColor(paredRelleno);
                        g2d.fillRect(x, y, tileSize, tileSize);
                        g2d.setColor(paredBorde);
                        g2d.drawRect(x, y, tileSize, tileSize);
                    } else {
                        // SUELO TRANSITABLE (0 en el txt) -> Amarillo
                        g2d.setColor(sueloRelleno);
                        g2d.fillRect(x, y, tileSize, tileSize);
                        g2d.setColor(sueloBorde);
                        g2d.drawRect(x, y, tileSize, tileSize);
                    }
                }
            }
        }
        // 3. DIBUJAR SPRITE DEL PROTAGONISTA
        BufferedImage hoja = (gestorSprites != null) ? gestorSprites.obtener(estadoActual) : null;
        int posX = personaje.getPosicionX();
        int posY = personaje.getPosicionY();

        if (hoja != null) {
            int frame = cuadroAnimacion % 6;
            dibujarSprite(g2d, gestorSprites, estadoActual, personaje.getDireccion(), frame, posX, posY);
            
            Rectangle hb = personaje.getHitbox();
            g2d.setColor(Color.RED);
            g2d.drawRect(hb.x, hb.y, hb.width, hb.height);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }

        // 4. DIBUJAR ENEMIGOS Y BARRAS DE VIDA
        
        if (spritesEnemigo != null && nivelActual != null && nivelActual.getListaEnemigos() != null) {
            for (Enemigo e : nivelActual.getListaEnemigos()) {
                int ex = e.getPosicionX();
                int ey = e.getPosicionY();
                
                    if (e.estaVivo()) {
                        EstadoPersonaje estadoEnemigo = e.estaMoviendose() ? EstadoPersonaje.CAMINANDO : EstadoPersonaje.IDLE;
                        int frame = (contadorTick / 6) % 6;
                        dibujarSprite(g2d, spritesEnemigo, estadoEnemigo, e.getDireccion(), frame, ex, ey);

                        // Barra de vida
                        int anchoBarra = (int) (20 * ESCALA);
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(ex + (int) (14 * ESCALA), ey - 6, anchoBarra, 4);
                        g2d.setColor(Color.RED);
                        int vidaActual = (int) (anchoBarra * (e.getPuntosVida() / 100.0));
                        g2d.fillRect(ex + (int) (14 * ESCALA), ey - 6, Math.max(0, vidaActual), 4);
                    }
            }
        }
    
        // RESTAURAR COORDENADAS ORIGINALES (PRE-CÁMARA)
        g2d.setTransform(transformOriginal);

        // 5. DIBUJAR CAPAS VIAUSLES EN ORDEN
        for (InterfazVisual capa : capasVisuales) {
         capa.renderizar(g2d, getWidth(), getHeight(), this);
        }
    }

    private void dibujarSprite(Graphics2D g2d, GestorSprites sprites, EstadoPersonaje estado,
                            Direccion direccion, int frame, int x, int y) {
        if (sprites == null) return;
        BufferedImage hoja = sprites.obtener(estado);
        if (hoja == null) return;

        int srcX1 = frame * ANCHO_CUADRO;
        int srcY1 = direccion.getFila() * ALTO_CUADRO;
        int srcX2 = srcX1 + ANCHO_CUADRO;
        int srcY2 = srcY1 + ALTO_CUADRO;

        int ancho = (int) (ANCHO_CUADRO * ESCALA);
        int alto = (int) (ALTO_CUADRO * ESCALA);

        g2d.drawImage(hoja, x, y, x + ancho, y + alto, srcX1, srcY1, srcX2, srcY2, this);
    }

    // -- METODO PARA LA IMAGEN DE PLANTA BAJA --
    private Image cargarImagenFondo(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            System.out.println("[NivelPanel] La ruta de la imagen es nula o vacia.");
            return null;
        }

        System.out.println("[NivelPanel] Intentando cargar fondo desde: " + ruta);

        try {
            // Intento 1: tal cual viene la ruta
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) {
                System.out.println("[NivelPanel] ¡Imagen cargada exitosamente via URL!");
                return javax.imageio.ImageIO.read(url);
            }

            // Intento 2: forzando la barra inicial si no la tenía
            if (!ruta.startsWith("/")) {
                url = getClass().getResource("/" + ruta);
                if (url != null) {
                    System.out.println("[NivelPanel] ¡Imagen cargada agregando '/' inicial!");
                    return javax.imageio.ImageIO.read(url);
                }
            }

            // Intento 3: si está en raíz del proyecto como File directo
            java.io.File archivo = new java.io.File(ruta.startsWith("/") ? ruta.substring(1) : ruta);
            if (archivo.exists()) {
                System.out.println("[NivelPanel] ¡Imagen cargada como File local!");
                return javax.imageio.ImageIO.read(archivo);
            }

            System.out.println("[NivelPanel] ERROR: No se encontro el archivo en ninguna ruta probada: " + ruta);
            return null;
        } catch (java.io.IOException e) {
            System.out.println("[NivelPanel] Excepcion al leer imagen: " + e.getMessage());
            return null;
        }
    }
}