package Vista;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;


public class NivelPanel extends JPanel {

    public static final double ZOOM = 5.0;
    public static final double ESCALA = 1.0;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;

//lo que la vista sabe del juego (Solo interfaces de la propia vista)
    private EscenaVisual escena;
    private EntidadVisual jugador;
    private Image imagenFondoNivel;

    // --- CAPAS VISUALES ---
    private final List<InterfazVisual> capasVisuales = new ArrayList<>();

    // --- ENTIDADES Y RECURSOS VISUALES ---
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
        if (jugador == null) return;

        int centroPersonajeX = jugador.getX() + (int) ((ANCHO_CUADRO * ESCALA) / 2);
        int centroPersonajeY = jugador.getY() + (int) ((ALTO_CUADRO * ESCALA) / 2);

        int anchoVisible = (int) (getWidth() / ZOOM);
        int altoVisible  = (int) (getHeight() / ZOOM);

        int objetivoX = centroPersonajeX - (anchoVisible / 2);
        int objetivoY = centroPersonajeY - (altoVisible / 2);

        int anchoMapaPx = (escena != null) ? escena.getAnchoPx() : 0;
        int altoMapaPx = (escena != null) ? escena.getAltoPx() : 0;

        int maxCamX = Math.max(0, anchoMapaPx - anchoVisible);
        int maxCamY = Math.max(0, altoMapaPx - altoVisible);
        camaraX = Math.max(0, Math.min(objetivoX, maxCamX));
        camaraY = Math.max(0, Math.min(objetivoY, maxCamY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (jugador == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        actualizarCamara();
        AffineTransform transformOriginal = g2d.getTransform();

        g2d.scale(ZOOM, ZOOM);
        g2d.translate(-camaraX, -camaraY);

        if (imagenFondoNivel != null && escena != null) {
            g2d.drawImage(imagenFondoNivel, 0, 0, escena.getAnchoPx(), escena.getAltoPx(), this);
        }
        if (escena != null) {
            int tile = escena.getTamanioTile();
            // la escena ya entrega solo los items que siguen en el piso
            for (ItemVisual item : escena.getItems()) {
                Image imagenItem = GestorSprites.cargarImagen(item.getRutaImagen());
                int lado = (int) (tile * item.getEscala());

                if (imagenItem != null) {
                    int anchoOrig = imagenItem.getWidth(this);
                    int altoOrig = imagenItem.getHeight(this);
                    double proporcion = Math.min((double) lado / anchoOrig, (double) lado / altoOrig);
                    int w = Math.max(1, (int) (anchoOrig * proporcion));
                    int h = Math.max(1, (int) (altoOrig * proporcion));

                    int ix = item.getX() + (tile - w) / 2;
                    int iy = item.getY() + (tile - h) / 2;
                    g2d.drawImage(imagenItem, ix, iy, w, h, this);
                } else {
                    int ix = item.getX() + (tile- lado) / 2;
                    int iy = item.getY() + (tile - lado) / 2;
                    g2d.setColor(Color.MAGENTA);
                    g2d.fillRect(ix, iy, lado, lado);
                }
            }
        }
        // el jugador y las entidades de la escena se ordenan juntos por profundidad
        List<EntidadVisual> porProfundidad = new ArrayList<>();
        porProfundidad.add(jugador);
        if (escena != null) {
            porProfundidad.addAll(escena.getEntidades());   // la escena ya entrega solo las vivas
        }
        porProfundidad.sort(Comparator.comparingInt(EntidadVisual::getBaseY));

        for (EntidadVisual ent : porProfundidad) {
            if (ent == jugador) {
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
        int posX = jugador.getX();
        int posY = jugador.getY();

        if (hoja != null) {
            int frame = cuadroAnimacion % 6;
            int fila = jugador.getFila();
            gestorSprites.dibujarCuadro(g2d, estadoActual, fila, frame,
                    posX, posY, ANCHO_CUADRO, ALTO_CUADRO, ESCALA);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }
    }

    private void dibujarEntidad(Graphics2D g2d, EntidadVisual e) {
        GestorSprites sprites = obtenerSprites(e.getRutaSprites());
        if (sprites == null) return;


        //que animacion toca lo que decide la propia entidad visual: aca solo se dibuja
        EstadoAnimacion estado = e.getEstadoAnimacion();
        int total = obtenerTotalFrames(sprites, estado);
        int frame = e.esTransicion()
                ? Math.min(total - 1, (int) (e.getProgresoTransicion() * total))
                : (contadorTick / 6) % total;

        sprites.dibujarCuadro(g2d, estado, e.getFila(), frame, e.getX(), e.getY(),
                ANCHO_CUADRO, ALTO_CUADRO, e.getEscala());

        if (e.mostrarBarraVida()) {
            double escE = e.getEscala();
            int ajusteY = (int) (ALTO_CUADRO * ESCALA * (1 - escE));
            int anchoBarra = (int) (20 * ESCALA);
            int ex = e.getX();
            int ey = e.getY();

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

    public void ocultarPausa() { pausaPanel.setVisible(false); 
    }


    public void setJugador(EntidadVisual jugador) { this.jugador = jugador; repaint(); 
    }

    public GestorSprites getGestorSprites() { return gestorSprites; }
    public void setGestorSprites(GestorSprites gestorSprites) { this.gestorSprites = gestorSprites; repaint(); }

//el controlador le entrega lo que hay que mostrar (nivel o habitacion) ya traducido a escenaVisual
    public void setEscena(EscenaVisual escena) {
        this.escena = escena;
        this.imagenFondoNivel = (escena != null) ? GestorSprites.cargarImagen(escena.getRutaImagenFondo()) : null;
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