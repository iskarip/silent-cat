package Vista;

import Modelo.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

public class NivelPanel extends JPanel {

    // CONSTANTES DE ESCALADO Y ZOOM
    public static final double ZOOM = 5.0; // Zoom del personaje
    public static final double ESCALA = 1.0;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;

    // --- MODELO Y ESTADO DEL NIVEL ---
    private Nivel nivelActual;
    private EspacioJugable espacioActual;
    private Image imagenFondoNivel; // imagen de planta baja

    // --- CAPAS VISUALES ---
    private final List<InterfazVisual> capasVisuales = new ArrayList<>();

    // --- ENTIDADES Y SPRITES ---
    private Personaje personaje;
    private GestorSprites gestorSprites;
    private final Map<String, GestorSprites> cacheSprites = new HashMap<>();

    // --- ESTADO VISUAL Y ANIMACIÓN ---
    private EstadoPersonaje estadoActual = EstadoPersonaje.IDLE;
    private int cuadroAnimacion = 0;
    private int contadorTick = 0;
    private int ticksMuerte = 0;
    private boolean finDelJuegoMostrado = false;
    private javax.swing.Timer timerDanio;

    // --- CÁMARA ---
    private int camaraX = 0;
    private int camaraY = 0;

    // --- COMPONENTES DE INTERFAZ (UI / OVERLAYS) ---
    private final FinDelJuego finDelJuego;
    private JButton botonPausa;
    private final PausaPanel pausaPanel;
    private final AcertijoPanel acertijoPanel;

    // CONSTRUCTOR

    public NivelPanel() {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false); // para que Tab llegue al KeyListener
        setDoubleBuffered(true);
        setLayout(null); // posicionamiento libre, para superponer botones al dibujo
        setBackground(Color.BLACK); // <-- Fondo negro

        // Creacion del boton Pausa
        crearBotonPausa();
        this.pausaPanel = new PausaPanel();
        add(pausaPanel);

        this.finDelJuego = new FinDelJuego();
        add(finDelJuego);

        this.acertijoPanel = new AcertijoPanel();
        acertijoPanel.setVisible(false);
        add(acertijoPanel);

        // Permite recuperar el foco del teclado al hacer clic sobre el canvas de juego
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    // METODOS PARA CONSULTAR Y DISPARAR EL ESTADO DE DAÑO
    public boolean estaRecibiendoDanio() {
        return estadoActual == EstadoPersonaje.RECIBIENDO_DANIO;
    }

    public void activarDanioRecibido() {
        if (estadoActual == EstadoPersonaje.MURIENDO) return;

        setEstado(EstadoPersonaje.RECIBIENDO_DANIO);

        if (timerDanio != null && timerDanio.isRunning()) {
            timerDanio.stop();
        }

        timerDanio = new javax.swing.Timer(250, e -> {
            if (estadoActual == EstadoPersonaje.RECIBIENDO_DANIO) {
                setEstado(EstadoPersonaje.IDLE);
            }
        });
        timerDanio.setRepeats(false);
        timerDanio.start();
    }

    // GESTIÓN DE INTERFAZ Y COMPONENTES SWING

    private void crearBotonPausa() {
        int ancho = 120;
        int alto = 120;

        // Instanciado usando BotonJuego.
        botonPausa = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/BotonPausa.png",
                "/Recursos/UI/Pausa/Botones/BotonPausaHover.png", // pasá null si no tenés versión hover
                ancho,
                alto
        );

        // Ubicación en la esquina superior derecha (X=730, Y=15)
        botonPausa.setBounds(getWidth() - ancho - 25, 20, ancho, alto);
        add(botonPausa);
    }

    // Agrego del doLayout para que el boton de pausa siempre se ubique en el mismo lugar
    // sin importar el tamaño de la pantalla
    @Override
    public void doLayout() {
        super.doLayout();

        acertijoPanel.setBounds(0, 0, getWidth(), getHeight());

        if (botonPausa != null) {
            int ancho = 65;
            int alto = 65;
            int margenDerecho = 25;
            int margenSuperior = 20;

            botonPausa.setBounds(getWidth() - ancho - margenDerecho, margenSuperior, ancho, alto);
        }
    }
   // GESTIÓN DE CAPAS VISUALES / OVERLAYS (POLIMORFISMO)

    public void agregarCapaVisual(InterfazVisual capa) {
        if (!capasVisuales.contains(capa)) {
            capasVisuales.add(capa);
        }
    } // este metodo llena la lista de capas visuales

    public void removerCapaVisual(InterfazVisual capa) {
        capasVisuales.remove(capa);
    }

    public void limpiarCapasVisuales() {
        capasVisuales.clear();
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

        int anchoMapaPx = 0;
        int altoMapaPx = 0;
        if (espacioActual != null && espacioActual.getMapaColision() != null) {
            MapaColision mc = espacioActual.getMapaColision();
            anchoMapaPx = mc.getColumnas() * MapaColision.TILE;
            altoMapaPx = mc.getFilas() * MapaColision.TILE;
        }

        int maxCamX = Math.max(0, anchoMapaPx - anchoVisible);
        int maxCamY = Math.max(0, altoMapaPx - altoVisible);
        camaraX = Math.max(0, Math.min(objetivoX, maxCamX));
        camaraY = Math.max(0, Math.min(objetivoY, maxCamY));
    }

    // CICLO DE DIBUJADO PRINCIPAL (PAINT COMPONENT)
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

        // 2. Fondo del nivel, escalado al tamaño del mapa
        if (imagenFondoNivel != null && espacioActual != null && espacioActual.getMapaColision() != null) {
            MapaColision mc = espacioActual.getMapaColision();
            g2d.drawImage(imagenFondoNivel, 0, 0,
                    mc.getColumnas() * MapaColision.TILE, mc.getFilas() * MapaColision.TILE, this);
        }

        // DIBUJAR ITEMS DEL NIVEL
        if (espacioActual != null && espacioActual.getListaItems() != null) {
            for (Item item : espacioActual.getListaItems()) {
                if (item.isRecogido()) continue;

                Image imagenItem = GestorSprites.cargarImagen(item.getRutaImagen());
                int lado = (int) (MapaColision.TILE * item.getEscala()); // lado máximo en píxeles de mundo

                if (imagenItem != null) {
                    int anchoOrig = imagenItem.getWidth(this);
                    int altoOrig = imagenItem.getHeight(this);

                    // encaja la imagen en un cuadrado de "lado" manteniendo la proporción
                    double proporcion = Math.min((double) lado / anchoOrig, (double) lado / altoOrig);
                    int w = Math.max(1, (int) (anchoOrig * proporcion));
                    int h = Math.max(1, (int) (altoOrig * proporcion));

                    // centrado en su tile
                    int ix = item.getPosicionX() + (MapaColision.TILE - w) / 2;
                    int iy = item.getPosicionY() + (MapaColision.TILE - h) / 2;
                    g2d.drawImage(imagenItem, ix, iy, w, h, this);
                } else {
                    int ix = item.getPosicionX() + (MapaColision.TILE - lado) / 2;
                    int iy = item.getPosicionY() + (MapaColision.TILE - lado) / 2;
                    g2d.setColor(Color.MAGENTA);
                    g2d.fillRect(ix, iy, lado, lado);
                }
            }
        }

        // 3. DIBUJAR SPRITE DEL PROTAGONISTA
        BufferedImage hoja = (gestorSprites != null) ? gestorSprites.obtener(estadoActual) : null;
        int posX = personaje.getPosicionX();
        int posY = personaje.getPosicionY();

        if (hoja != null) {
            int frame = cuadroAnimacion % 6;
            // Se delega el recorte exacto a GestorSprites
            int fila = (personaje.getDireccion() != null) ? personaje.getDireccion().getFila() : 0;
            gestorSprites.dibujarCuadro(g2d, estadoActual, fila, frame,
                    posX, posY, ANCHO_CUADRO, ALTO_CUADRO, ESCALA);

            Rectangle hb = personaje.getHitbox();
            g2d.setColor(Color.RED);
            g2d.drawRect(hb.x, hb.y, hb.width, hb.height);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }

        // 4. DIBUJAR ENTIDADES (enemigos, ratas, etc.)
        if (espacioActual != null && espacioActual.getListaEntidades() != null) {
            for (Entidad e : espacioActual.getListaEntidades()) {
                if (!e.estaVivo()) continue;

                    GestorSprites sprites = obtenerSprites(e.getRutaSprites());
                    EstadoPersonaje estado = e.estaAtacando() ? EstadoPersonaje.ATACANDO :
                        (e.estaMoviendose() ? EstadoPersonaje.CAMINANDO : EstadoPersonaje.IDLE);
                    int frame = (contadorTick / 6) % obtenerTotalFrames(sprites, estado);
                    double escE = e.getEscalaSprite();
                    int ex = e.getPosicionX();
                    int ey = e.getPosicionY();

                    dibujarSprite(g2d, sprites, estado, e.getDireccion(), frame, ex, ey, escE);

                    if (e.mostrarBarraVida()) {
                        // el tope del sprite bajó al achicarlo, la barra lo acompaña
                        int ajusteY = (int) (ALTO_CUADRO * ESCALA * (1 - escE));
                        int anchoBarra = (int) (20 * ESCALA);
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(ex + (int) (14 * ESCALA), ey + ajusteY - 6, anchoBarra, 4);
                        g2d.setColor(Color.RED);
                        int anchoVida = (int) (anchoBarra * (e.getPorcentajeVida() / 100.0));
                        g2d.fillRect(ex + (int) (14 * ESCALA), ey + ajusteY - 6, Math.max(0, anchoVida), 4);
                    }
            }
        }

        // RESTAURAR COORDENADAS ORIGINALES (PRE-CÁMARA)
        g2d.setTransform(transformOriginal);

        // 5. DIBUJAR CAPAS VIAUSLES EN ORDEN
        for (InterfazVisual capa : capasVisuales) {
            capa.renderizar(g2d, getWidth(), getHeight(), this);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    // --- MANEJO DE ANIMACIONES DE SPRITES --
    public void actualizarAnimacion(boolean moviendose) {
        contadorTick++;

        // Si se mueve va más rápido (% 6), si está quieto (IDLE) va más lento (% 12 o % 15)
        int velocidadAnimacion = moviendose ? 6 : 12;

        if (contadorTick % velocidadAnimacion == 0) {
            // IDLE tiene 4 frames, los demás estados tienen 6
            int totalFrames = obtenerTotalFrames(gestorSprites, estadoActual);

            cuadroAnimacion = (cuadroAnimacion + 1) % totalFrames;
        }
    }

    private int obtenerTotalFrames(GestorSprites sprites, EstadoPersonaje estado) {
        if (sprites == null) return 1;
        BufferedImage hoja = sprites.obtener(estado);
        return (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);
    }

    public void reiniciarEstadoNivel() {
        ticksMuerte = 0;
        finDelJuegoMostrado = false;
        cuadroAnimacion = 0;
        estadoActual = EstadoPersonaje.IDLE;
        ocultarFindelJuego();
    }

    private GestorSprites obtenerSprites(String ruta) {
        if (ruta == null) return null;
        return cacheSprites.computeIfAbsent(ruta, GestorSprites::new);
    }

    private void dibujarSprite(Graphics2D g2d, GestorSprites sprites, EstadoPersonaje estado,
                               Direccion direccion, int frame, int x, int y, double escalaSprite) {
        if (sprites == null) return;
        BufferedImage hoja = sprites.obtener(estado);
        if (hoja == null) return;

        int srcX1 = frame * ANCHO_CUADRO;
        int srcY1 = direccion.getFila() * ALTO_CUADRO;
        int srcX2 = srcX1 + ANCHO_CUADRO;
        int srcY2 = srcY1 + ALTO_CUADRO;

        int anchoBase = (int) (ANCHO_CUADRO * ESCALA);
        int altoBase  = (int) (ALTO_CUADRO * ESCALA);
        int ancho = (int) (anchoBase * escalaSprite);
        int alto  = (int) (altoBase * escalaSprite);

        int dx = x + (anchoBase - ancho) / 2;
        int dy = y + (altoBase - alto);

        g2d.drawImage(hoja, dx, dy, dx + ancho, dy + alto, srcX1, srcY1, srcX2, srcY2, this);
    }

    public void avanzarAnimacionMuerte() {
        ticksMuerte++;
        if (ticksMuerte % 15 != 0) return;

        BufferedImage hoja = (gestorSprites == null) ? null : gestorSprites.obtener(EstadoPersonaje.MURIENDO);
        int totalFrames = (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);

        if (cuadroAnimacion < totalFrames - 1) {
            cuadroAnimacion++;
        } else if (!finDelJuegoMostrado) {
            finDelJuegoMostrado = true;
            mostrarFindelJuego();
        }
    }

    public void mostrarFindelJuego() {
        if (finDelJuego != null) {
            finDelJuego.setBounds(0, 0, getWidth(), getHeight());
            finDelJuego.setVisible(true);
        }
        if (botonPausa != null) botonPausa.setVisible(false);
    }

    public void ocultarFindelJuego() {
        if (finDelJuego != null) finDelJuego.setVisible(false);
        if (botonPausa != null) botonPausa.setVisible(true);
    }

    public void mostrarPausa() {
        pausaPanel.setBounds(0, 0, getWidth(), getHeight());
        pausaPanel.setVisible(true);
    }

    public void ocultarPausa() { pausaPanel.setVisible(false); }

    // GETTERS Y SETTERS
    public Personaje getPersonaje() { return personaje; }
    public void setPersonaje(Personaje personaje) { this.personaje = personaje; repaint(); }

    public GestorSprites getGestorSprites() { return gestorSprites; }
    public void setGestorSprites(GestorSprites gestorSprites) { this.gestorSprites = gestorSprites; repaint(); }

    public EspacioJugable getEspacioActual() { return espacioActual; }

    public void setEspacioActual(EspacioJugable espacio) {
        this.espacioActual = espacio;
        this.imagenFondoNivel = GestorSprites.cargarImagen(espacio.getRutaImagenFondo());
        repaint();
    }

    public void setEstado(EstadoPersonaje nuevoEstado) {
        // Solo si el estado cambia, reiniciamos los contadores de animación para evitar saltos o parpadeos
        if (this.estadoActual != nuevoEstado) {
            this.estadoActual = nuevoEstado;
            this.cuadroAnimacion = 0;
            this.contadorTick = 0;
            repaint();
        }
    }

    public boolean estaAtacando() {
        return estadoActual == EstadoPersonaje.ATACANDO;
    }

    public Nivel getNivelActual() { return nivelActual; }
    public void setNivelActual(Nivel nivel) {
        this.nivelActual = nivel;
        if (nivel != null) {
            setEspacioActual(nivel);
        }
        repaint();
    }

    public int getCamaraX() {
        return camaraX;
    }

    public int getCamaraY() {
        return camaraY;
    }

    public FinDelJuego getFinDelJuego() {
        return finDelJuego;
    }

    public JButton getBotonPausa() {
        return botonPausa;
    }

    public PausaPanel getPausaPanel() {
        return pausaPanel;
    }

    public AcertijoPanel getAcertijoPanel() { return acertijoPanel; }

    public void mostrarAcertijo() {
        acertijoPanel.setVisible(true);
        if (botonPausa != null) botonPausa.setVisible(false);
    }

    public void ocultarAcertijo() {
        acertijoPanel.setVisible(false);
        if (botonPausa != null) botonPausa.setVisible(true);
        requestFocusInWindow();
    }
}