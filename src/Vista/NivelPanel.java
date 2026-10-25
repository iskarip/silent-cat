package Vista;

import Modelo.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class NivelPanel extends JPanel {

    public static final double ZOOM = 5.0;
    public static final double ESCALA = 1.0;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;

    private static final Map<PoseEntidadCombatible, EstadoAnimacion> ANIMACION_POR_POSTURA = new EnumMap<>(PoseEntidadCombatible.class);
    static {
        ANIMACION_POR_POSTURA.put(PoseEntidadCombatible.SENTADO, EstadoAnimacion.SENTADO);
        ANIMACION_POR_POSTURA.put(PoseEntidadCombatible.LEVANTANDOSE, EstadoAnimacion.LEVANTANDOSE);
    }

    // --- MODELO ASOCIADO ---
    private Nivel nivelActual;
    private EspacioJugable espacioActual;
    private Image imagenFondoNivel;

    // --- CAPAS VISUALES ---
    private final List<InterfazVisual> capasVisuales = new ArrayList<>();

    // --- ENTIDADES Y RECURSOS VISUALES ---
    private Personaje personaje;
    private GestorSprites gestorSprites;
    private final Map<String, GestorSprites> cacheSprites = new HashMap<>();

    // --- ESTADO EXCLUSIVAMENTE VISUAL ---
    private EstadoAnimacion estadoActual = EstadoAnimacion.IDLE;
    private int cuadroAnimacion = 0;
    private int contadorTick = 0;
    private int ticksMuerte = 0;

    // --- CÁMARA ---
    private int camaraX = 0;
    private int camaraY = 0;

    // --- SUBPANELES Y CONTROLES UI ---
    private final FinDelJuego finDelJuego;
    private JButton botonPausa;
    private final PausaPanel pausaPanel;
    private final AcertijoPanel acertijoPanel;

    public NivelPanel() {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setDoubleBuffered(true);
        setLayout(null);
        setBackground(Color.BLACK);

        crearBotonPausa();
        this.pausaPanel = new PausaPanel();
        add(pausaPanel);

        this.finDelJuego = new FinDelJuego();
        add(finDelJuego);

        this.acertijoPanel = new AcertijoPanel();
        acertijoPanel.setVisible(false);
        add(acertijoPanel);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    private void crearBotonPausa() {
        int ancho = 120;
        int alto = 120;
        botonPausa = new BotonJuego(
                "/Recursos/UI/Pausa/Botones/BotonPausa.png",
                "/Recursos/UI/Pausa/Botones/BotonPausaHover.png",
                ancho,
                alto
        );
        botonPausa.setBounds(getWidth() - ancho - 25, 20, ancho, alto);
        add(botonPausa);
    }

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

    public void agregarCapaVisual(InterfazVisual capa) {
        if (!capasVisuales.contains(capa)) capasVisuales.add(capa);
    }

    public void removerCapaVisual(InterfazVisual capa) {
        capasVisuales.remove(capa);
    }

    public void limpiarCapasVisuales() {
        capasVisuales.clear();
    }

    public void actualizarCamara() {
        if (personaje == null) return;

        int centroPersonajeX = personaje.getPosicionX() + (int) ((ANCHO_CUADRO * ESCALA) / 2);
        int centroPersonajeY = personaje.getPosicionY() + (int) ((ALTO_CUADRO * ESCALA) / 2);

        int anchoVisible = (int) (getWidth() / ZOOM);
        int altoVisible  = (int) (getHeight() / ZOOM);

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (personaje == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        actualizarCamara();
        AffineTransform transformOriginal = g2d.getTransform();

        g2d.scale(ZOOM, ZOOM);
        g2d.translate(-camaraX, -camaraY);

        if (imagenFondoNivel != null && espacioActual != null && espacioActual.getMapaColision() != null) {
            MapaColision mc = espacioActual.getMapaColision();
            g2d.drawImage(imagenFondoNivel, 0, 0,
                    mc.getColumnas() * MapaColision.TILE, mc.getFilas() * MapaColision.TILE, this);
        }

        if (espacioActual != null && espacioActual.getListaItems() != null) {
            for (Item item : espacioActual.getListaItems()) {
                if (item.isRecogido()) continue;

                Image imagenItem = GestorSprites.cargarImagen(item.getRutaImagen());
                int lado = (int) (MapaColision.TILE * item.getEscala());

                if (imagenItem != null) {
                    int anchoOrig = imagenItem.getWidth(this);
                    int altoOrig = imagenItem.getHeight(this);
                    double proporcion = Math.min((double) lado / anchoOrig, (double) lado / altoOrig);
                    int w = Math.max(1, (int) (anchoOrig * proporcion));
                    int h = Math.max(1, (int) (altoOrig * proporcion));

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

        List<Entidad> porProfundidad = new ArrayList<>();
        porProfundidad.add(personaje);
        if (espacioActual != null && espacioActual.getListaEntidades() != null) {
            for (Entidad ent : espacioActual.getListaEntidades()) {
                if (ent.estaVivo()) porProfundidad.add(ent);
            }
        }
        porProfundidad.sort(Comparator.comparingInt(ent -> ent.getHitbox().y + ent.getHitbox().height));

        for (Entidad ent : porProfundidad) {
            if (ent == personaje) {
                dibujarPersonaje(g2d);
            } else {
                dibujarEntidad(g2d, ent);
            }
        }

        g2d.setTransform(transformOriginal);

        for (InterfazVisual capa : capasVisuales) {
            capa.renderizar(g2d, getWidth(), getHeight(), this);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void actualizarAnimacion(boolean moviendose) {
        contadorTick++;
        int velocidadAnimacion = moviendose ? 6 : 12;

        if (contadorTick % velocidadAnimacion == 0) {
            int totalFrames = obtenerTotalFrames(gestorSprites, estadoActual);
            cuadroAnimacion = (cuadroAnimacion + 1) % totalFrames;
        }
    }

    private void dibujarPersonaje(Graphics2D g2d) {
        BufferedImage hoja = (gestorSprites != null) ? gestorSprites.obtener(estadoActual) : null;
        int posX = personaje.getPosicionX();
        int posY = personaje.getPosicionY();

        if (hoja != null) {
            int frame = cuadroAnimacion % 6;
            int fila = (personaje.getDireccion() != null) ? personaje.getDireccion().getFila() : 0;
            gestorSprites.dibujarCuadro(g2d, estadoActual, fila, frame,
                    posX, posY, ANCHO_CUADRO, ALTO_CUADRO, ESCALA);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }
    }

    private void dibujarEntidad(Graphics2D g2d, Entidad e) {
        GestorSprites sprites = obtenerSprites(e.getRutaSprites());
        if (sprites == null) return;

        EstadoAnimacion estado = estadoDe(e);
        int total = obtenerTotalFrames(sprites, estado);
        int frame = e.getPostura().esTransicion()
                ? Math.min(total - 1, (int) (e.getProgresoPostura() * total))
                : (contadorTick / 6) % total;

        int fila = (e.getDireccion() != null) ? e.getDireccion().getFila() : 0;
        sprites.dibujarCuadro(g2d, estado, fila, frame, e.getPosicionX(), e.getPosicionY(),
                ANCHO_CUADRO, ALTO_CUADRO, e.getEscalaSprite());

        if (e.mostrarBarraVida()) {
            double escE = e.getEscalaSprite();
            int ajusteY = (int) (ALTO_CUADRO * ESCALA * (1 - escE));
            int anchoBarra = (int) (20 * ESCALA);
            int ex = e.getPosicionX();
            int ey = e.getPosicionY();

            g2d.setColor(Color.BLACK);
            g2d.fillRect(ex + (int) (14 * ESCALA), ey + ajusteY - 6, anchoBarra, 4);

            g2d.setColor(Color.RED);
            int anchoVida = (int) (anchoBarra * (e.getPorcentajeVida() / 100.0));
            g2d.fillRect(ex + (int) (14 * ESCALA), ey + ajusteY - 6, Math.max(0, anchoVida), 4);
        }
    }

    private int obtenerTotalFrames(GestorSprites sprites, EstadoAnimacion estado) {
        if (sprites == null) return 1;
        BufferedImage hoja = sprites.obtener(estado);
        return (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);
    }

    public void reiniciarEstadoNivel() {
        ticksMuerte = 0;
        cuadroAnimacion = 0;
        estadoActual = EstadoAnimacion.IDLE;
        ocultarFindelJuego();
    }

    private EstadoAnimacion estadoDe(Entidad e) {
        EstadoAnimacion porPostura = ANIMACION_POR_POSTURA.get(e.getPostura());
        if (porPostura != null) return porPostura;
        if (e.estaAtacando()) return EstadoAnimacion.ATACANDO;
        return e.estaMoviendose() ? EstadoAnimacion.CAMINANDO : EstadoAnimacion.IDLE;
    }

    private GestorSprites obtenerSprites(String ruta) {
        if (ruta == null) return null;
        return cacheSprites.computeIfAbsent(ruta, GestorSprites::new);
    }

    /**
     * Hace avanzar un frame de la muerte. Devuelve true únicamente cuando la animación
     * concluyó su último frame, permitiendo al Controlador decidir si termina la partida.
     */
    public boolean avanzarAnimacionMuerte() {
        ticksMuerte++;
        if (ticksMuerte % 15 != 0) return false;

        BufferedImage hoja = (gestorSprites == null) ? null : gestorSprites.obtener(EstadoAnimacion.MURIENDO);
        int totalFrames = (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);

        if (cuadroAnimacion < totalFrames - 1) {
            cuadroAnimacion++;
            return false;
        }
        return true;
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

    public void setEstado(EstadoAnimacion nuevoEstado) {
        if (this.estadoActual != nuevoEstado) {
            this.estadoActual = nuevoEstado;
            this.cuadroAnimacion = 0;
            this.contadorTick = 0;
            repaint();
        }
    }

    public boolean estaAtacando() { return estadoActual == EstadoAnimacion.ATACANDO; }
    public boolean estaRecibiendoDanio() { return estadoActual == EstadoAnimacion.RECIBIENDO_DANIO; }

    public Nivel getNivelActual() { return nivelActual; }
    public void setNivelActual(Nivel nivel) {
        this.nivelActual = nivel;
        if (nivel != null) setEspacioActual(nivel);
        repaint();
    }

    public int getCamaraX() { return camaraX; }
    public int getCamaraY() { return camaraY; }
    public FinDelJuego getFinDelJuego() { return finDelJuego; }
    public JButton getBotonPausa() { return botonPausa; }
    public PausaPanel getPausaPanel() { return pausaPanel; }
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