package Vista;

import Modelo.Gato;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Enemigo;
import Modelo.MapaColision;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NivelPanel extends JPanel {

    // --- MODELO Y ESTADO DEL NIVEL ---
    private Nivel nivelActual;
    private MapaColision mapaColision;

    // --- OVERLAY DE FLASHBACK (Lógica encapsulada de tu compañera) ---
    private FlashbackGato flashbackGato = new FlashbackGato();

    // --- ENTIDADES Y SPRITES ---
    private Personaje personaje;
    private GestorSprites gestorSprites;

    private List<Enemigo> enemigos = new ArrayList<>();
    private GestorSprites spritesEnemigo;

    // --- ESTADO VISUAL Y ANIMACIÓN ---
    private EstadoPersonaje estadoActual = EstadoPersonaje.IDLE;
    private Direccion direccionActual = Direccion.ABAJO;

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

    // --- CONSTANTES DE RENDERIZADO ---
    public static final double ESCALA = 2.5;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;

    public NivelPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setLayout(null); 

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

    // --- LÓGICA DE CÁMARA (CÁLCULO EXCLUSIVAMENTE VISUAL) ---
    public void actualizarCamara() {
        if (personaje == null) return;

        int centroPersonajeX = personaje.getPosicionX() + (int) ((ANCHO_CUADRO * ESCALA) / 2);
        int centroPersonajeY = personaje.getPosicionY() + (int) ((ANCHO_CUADRO * ESCALA) / 2);

        this.camaraX = centroPersonajeX - (getWidth() / 2);
        this.camaraY = centroPersonajeY - (getHeight() / 2);
    }

    // --- GETTERS Y SETTERS ---
    public Personaje getPersonaje() { return personaje; }
    public void setPersonaje(Personaje personaje) { this.personaje = personaje; repaint(); }

    public GestorSprites getGestorSprites() { return gestorSprites; }
    public void setGestorSprites(GestorSprites gestorSprites) { this.gestorSprites = gestorSprites; repaint(); }

    public GestorSprites getSpritesEnemigo() { return spritesEnemigo; }
    public void setSpritesEnemigo(GestorSprites spritesEnemigo) { this.spritesEnemigo = spritesEnemigo; }

    public void setEnemigos(List<Enemigo> enemigos, GestorSprites spritesEnemigo) {
        this.enemigos = enemigos;
        this.spritesEnemigo = spritesEnemigo;
        repaint();
    }
    public List<Enemigo> getEnemigos() { return this.enemigos; }

    public void setEstado(EstadoPersonaje estado) { this.estadoActual = estado; repaint(); }
    public void setDireccion(Direccion direccion) { this.direccionActual = direccion; }

    public void setMapaColision(MapaColision mapaColision) { this.mapaColision = mapaColision; repaint(); }
    public MapaColision getMapaColision() { return this.mapaColision; }

    public Nivel getNivelActual() { return nivelActual; }
    public void setNivelActual(Nivel nivel) {
        this.nivelActual = nivel;
        if (nivel != null) {
            setMapaColision(nivel.getMapaColision());
            if (nivel.getListaEnemigos() != null) {
                setEnemigos(nivel.getListaEnemigos(), spritesEnemigo);
            }
        }
    }

    public JButton getBotonVolverMenu() { return botonVolverMenu; }

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

    // --- DELIMITACIÓN DE HITBOXES PARA RENDERIZADO / FÍSICA ---
    public Rectangle getHitbox(int x, int y) {
        int anchoHitbox = (int) (16 * ESCALA);
        int altoHitbox = (int) (10 * ESCALA);
        int offsetX = (int) (16 * ESCALA);
        int offsetY = (int) (38 * ESCALA);
        return new Rectangle(x + offsetX, y + offsetY, anchoHitbox, altoHitbox);
    }

    public Rectangle getHitboxEnemigo(Enemigo enemigo) {
        int anchoHitbox = (int) (16 * ESCALA);
        int altoHitbox = (int) (10 * ESCALA);
        int offsetX = (int) (16 * ESCALA);
        int offsetY = (int) (38 * ESCALA);
        return new Rectangle(enemigo.getPosicionX() + offsetX, enemigo.getPosicionY() + offsetY, anchoHitbox, altoHitbox);
    }

    public Rectangle getHitboxAtaque(int x, int y) {
        int alcance = (int) (18 * ESCALA);
        Rectangle base = getHitbox(x, y);

        switch (direccionActual) {
            case ARRIBA:    return new Rectangle(base.x, base.y - alcance, base.width, alcance);
            case ABAJO:     return new Rectangle(base.x, base.y + base.height, base.width, alcance);
            case IZQUIERDA: return new Rectangle(base.x - alcance, base.y, alcance, base.height);
            case DERECHA:
            default:        return new Rectangle(base.x + base.width, base.y, alcance, base.height);
        }
    }

    // --- CICLO DE DIBUJADO (PAINT COMPONENT) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (personaje == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // 1. APLICAR TRANSFORMACIÓN DE CÁMARA
        actualizarCamara();
        AffineTransform transformOriginal = g2d.getTransform();
        g2d.translate(-camaraX, -camaraY);

        // 2. DIBUJAR PISO Y OBSTÁCULOS DEL MAPA DE COLISIÓN
        if (mapaColision != null) {
            int tileSize = 32;
            int totalFilas = 26;
            int totalColumnas = 48;

            for (int f = 0; f < totalFilas; f++) {
                for (int c = 0; c < totalColumnas; c++) {
                    int x = c * tileSize;
                    int y = f * tileSize;

                    if (!mapaColision.esPosicionValida(x + 16, y + 16)) {
                        g2d.setColor(new Color(40, 40, 50));
                        g2d.fillRect(x, y, tileSize, tileSize);
                        g2d.setColor(new Color(25, 25, 30));
                        g2d.drawRect(x, y, tileSize, tileSize);
                    } else {
                        g2d.setColor(new Color(180, 160, 140));
                        g2d.fillRect(x, y, tileSize, tileSize);
                        g2d.setColor(new Color(160, 140, 120));
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
            int fila = direccionActual.getFila();

            int srcX1 = frame * ANCHO_CUADRO;
            int srcY1 = fila * ALTO_CUADRO;
            int srcX2 = srcX1 + ANCHO_CUADRO;
            int srcY2 = srcY1 + ALTO_CUADRO;

            int anchoPantalla = (int) (ANCHO_CUADRO * ESCALA);
            int altoPantalla = (int) (ALTO_CUADRO * ESCALA);

            if (direccionActual == Direccion.DERECHA && fila == 1) {
                g2d.drawImage(hoja, posX + anchoPantalla, posY, posX, posY + altoPantalla, srcX1, srcY1, srcX2, srcY2, this);
            } else {
                g2d.drawImage(hoja, posX, posY, posX + anchoPantalla, posY + altoPantalla, srcX1, srcY1, srcX2, srcY2, this);
            }

            Rectangle hb = getHitbox(posX, posY);
            g2d.setColor(Color.RED);
            g2d.drawRect(hb.x, hb.y, hb.width, hb.height);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }

        // 4. DIBUJAR ENEMIGOS Y BARRAS DE VIDA
        if (spritesEnemigo != null && enemigos != null) {
            for (Enemigo e : enemigos) {
                int ex = e.getPosicionX();
                int ey = e.getPosicionY();

                if (e.estaVivo()) {
                    EstadoPersonaje estadoEnemigo = e.estaMoviendose() ? EstadoPersonaje.CAMINANDO : EstadoPersonaje.IDLE;
                    BufferedImage hojaEnemigo = spritesEnemigo.obtener(estadoEnemigo);

                    if (hojaEnemigo != null) {
                        int frame = e.getCuadroAnimacion() % 6;
                        int fila = e.getDireccion().getFila();

                        int srcX1 = frame * ANCHO_CUADRO;
                        int srcY1 = fila * ALTO_CUADRO;
                        int srcX2 = srcX1 + ANCHO_CUADRO;
                        int srcY2 = srcY1 + ALTO_CUADRO;

                        int anchoDestino = (int) (ANCHO_CUADRO * ESCALA);
                        int altoDestino = (int) (ALTO_CUADRO * ESCALA);

                        if (e.getDireccion() == Direccion.DERECHA && fila == 1) {
                            g2d.drawImage(hojaEnemigo, ex + anchoDestino, ey, ex, ey + altoDestino, srcX1, srcY1, srcX2, srcY2, this);
                        } else {
                            g2d.drawImage(hojaEnemigo, ex, ey, ex + anchoDestino, ey + altoDestino, srcX1, srcY1, srcX2, srcY2, this);
                        }
                    }

                    int anchoBarra = (int) (20 * ESCALA);
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(ex + (int) (14 * ESCALA), ey - 6, anchoBarra, 4);
                    g2d.setColor(Color.RED);
                    int vidaActual = (int) (anchoBarra * (e.getPuntosVida() / 100.0));
                    g2d.fillRect(ex + (int) (14 * ESCALA), ey - 6, Math.max(0, vidaActual), 4);
                } else {
                    int totalFramesMuerte = obtenerTotalFramesMuerte(spritesEnemigo);
                    if (!e.animacionMuerteTerminada(totalFramesMuerte)) {
                        BufferedImage hojaMuerte = spritesEnemigo.obtener(EstadoPersonaje.MURIENDO);
                        if (hojaMuerte != null) {
                            int frame = e.getCuadroAnimacionMuerte();
                            int fila = e.getDireccion().getFila();

                            int srcX1 = frame * ANCHO_CUADRO;
                            int srcY1 = fila * ALTO_CUADRO;
                            int srcX2 = srcX1 + ANCHO_CUADRO;
                            int srcY2 = srcY1 + ALTO_CUADRO;

                            int anchoDestino = (int) (ANCHO_CUADRO * ESCALA);
                            int altoDestino = (int) (ALTO_CUADRO * ESCALA);

                            if (e.getDireccion() == Direccion.DERECHA && fila == 1) {
                                g2d.drawImage(hojaMuerte, ex + anchoDestino, ey, ex, ey + altoDestino, srcX1, srcY1, srcX2, srcY2, this);
                            } else {
                                g2d.drawImage(hojaMuerte, ex, ey, ex + anchoDestino, ey + altoDestino, srcX1, srcY1, srcX2, srcY2, this);
                            }
                        }
                    }
                }
            }
        }

        // RESTAURAR COORDENADAS ORIGINALES (PRE-CÁMARA)
        g2d.setTransform(transformOriginal);

        // 5. DIBUJAR OVERLAY TRANSPARENTE DE FLASHBACK
        flashbackGato.renderizar(g2d, getWidth(), getHeight(), this);
    }

    // --- MÉTODOS PARA DISPARAR EL FLASHBACK ---
    public void activarFlashbackGato() {
        if (nivelActual == null || nivelActual.getGato() == null) return;
        flashbackGato.activar(nivelActual.getGato(), personaje, this);
    }

    public void activarFlashbackPrueba() {
        Gato gatoPrueba = new Gato();
        flashbackGato.activar(gatoPrueba, personaje, this);
    }
}