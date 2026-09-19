package Vista;

import Modelo.Gato;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.Enemigo;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NivelPanel extends JPanel {

private Nivel nivelActual;
private boolean mostrandoFlashback = false;

private Image imagenFlashback1 = cargarImagenFlashback("/Recursos/imagenes_gato/1flashback.png");
private Image imagenFlashback2 = cargarImagenFlashback("/Recursos/imagenes_gato/2flashback.png");
private Image imagenFlashback3 = cargarImagenFlashback("/Recursos/imagenes_gato/3flashback.png");
private Image imagenFlashback4 = cargarImagenFlashback("/Recursos/imagenes_gato/4flashback.png");

private static Image cargarImagenFlashback(String ruta) {
    java.io.InputStream is = NivelPanel.class.getResourceAsStream(ruta);
    if (is == null) {
        System.out.println("No se encontro el recurso en: " + ruta);
        return null;
    }
    try {
        return javax.imageio.ImageIO.read(is);
    } catch (java.io.IOException e) {
        System.out.println("Error al leer " + ruta + ": " + e.getMessage());
        return null;
    }
}


private int poseActual = 0; // guarda cual de las 4 imágenes toca mostrar ahora
private java.util.Random random = new java.util.Random(); // para elegir al azar


    // --- VARIABLES DE ENTIDADES Y SPRITES ---
    private Personaje personaje;
    private GestorSprites gestorSprites;

    private List<Enemigo> enemigos = new ArrayList<>();
    private GestorSprites spritesEnemigo;

    // --- VARIABLES DE ESTADO Y ANIMACIÓN ---
    private EstadoPersonaje estadoActual = EstadoPersonaje.IDLE;
    private Direccion direccionActual = Direccion.ABAJO;

    private int cuadroAnimacion = 0;
    private int contadorTick = 0;

    // --- GAME OVER ---
    private JuegoFrame ventanaPrincipal;
    private JLabel etiquetaGameOver;
    private JButton botonVolverMenu;
    private int ticksMuerte = 0;
    private boolean gameOverMostrado = false;

    public static final double ESCALA = 2.5;
    public static final int ANCHO_CUADRO = 48;
    public static final int ALTO_CUADRO = 48;

    public NivelPanel() {
        setFocusable(true);
        setDoubleBuffered(true);
        setLayout(null); 

        etiquetaGameOver = new JLabel("GAME OVER", SwingConstants.CENTER);
        etiquetaGameOver.setFont(new Font("Arial", Font.BOLD, 48));
        etiquetaGameOver.setForeground(Color.RED);
        etiquetaGameOver.setBounds(250, 200, 300, 60);
        etiquetaGameOver.setVisible(false);
        add(etiquetaGameOver);

        botonVolverMenu = new JButton("Volver al menú");
        botonVolverMenu.setBounds(320, 280, 160, 40);
        botonVolverMenu.setVisible(false);
        botonVolverMenu.addActionListener(e -> volverAlMenu());
        add(botonVolverMenu);
    }

    // --- GETTERS Y SETTERS ---

    public Personaje getPersonaje() {
        return personaje;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
        repaint();
    }

    public void setGestorSprites(GestorSprites gestorSprites) {
        this.gestorSprites = gestorSprites;
        repaint();
    }

    public void setVentanaPrincipal(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }

    public void setEnemigos(List<Enemigo> enemigos, GestorSprites spritesEnemigo) {
        this.enemigos = enemigos;
        this.spritesEnemigo = spritesEnemigo;
        repaint();
    }

    public List<Enemigo> getEnemigos() {
        return this.enemigos;
    }

    public void setEstado(EstadoPersonaje estado) {
        this.estadoActual = estado;
        repaint();
    }

    public void setDireccion(Direccion direccion) {
        this.direccionActual = direccion;
    }

    // --- ANIMACIÓN ---

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
        if (ticksMuerte % 6 !=0) return;

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
        BufferedImage hoja = gestorSprites.obtener(EstadoPersonaje.MURIENDO);
        return (hoja == null) ? 1 : Math.max(1, hoja.getWidth() / ANCHO_CUADRO);
    }

    public int obtenerTotalFramesMuerteEnemigo() {
        return obtenerTotalFramesMuerte(spritesEnemigo);
    }


    public void mostrarGameOver() {
        etiquetaGameOver.setVisible(true);
        botonVolverMenu.setVisible(true);
    }

    private void volverAlMenu() {
        if (ventanaPrincipal != null) {
            ventanaPrincipal.mostrarPantalla("menu");
        }
    }

    public void reiniciarEstadoNivel() {
        ticksMuerte = 0;
        gameOverMostrado = false;
        cuadroAnimacion = 0;
        estadoActual = EstadoPersonaje.IDLE;
        etiquetaGameOver.setVisible(false);
        botonVolverMenu.setVisible(false);
    }

    // --- HITBOXES ---

    // Hitbox en los pies del personaje
    public Rectangle getHitbox(int x, int y) {
        int anchoHitbox = (int) (16 * ESCALA);
        int altoHitbox = (int) (10 * ESCALA);
        int offsetX = (int) (16 * ESCALA);
        int offsetY = (int) (38 * ESCALA);
        return new Rectangle(x + offsetX, y + offsetY, anchoHitbox, altoHitbox);
    }

    // Hitbox en los pies enemigo
    public Rectangle getHitboxEnemigo(Enemigo enemigo) {
        int anchoHitbox = (int) (16 * ESCALA);
        int altoHitbox = (int) (10 * ESCALA);
        int offsetX = (int) (16 * ESCALA);
        int offsetY = (int) (38 * ESCALA);
        return new Rectangle(enemigo.getPosicionX() + offsetX, enemigo.getPosicionY() + offsetY, anchoHitbox, altoHitbox);
    }

    // Hitbox de ataque frontal según hacia dónde mira el personaje
    public Rectangle getHitboxAtaque(int x, int y) {
        int alcance = (int) (18 * ESCALA);
        Rectangle base = getHitbox(x, y);

        switch (direccionActual) {
            case ARRIBA:
                return new Rectangle(base.x, base.y - alcance, base.width, alcance);
            case ABAJO:
                return new Rectangle(base.x, base.y + base.height, base.width, alcance);
            case IZQUIERDA:
                return new Rectangle(base.x - alcance, base.y, alcance, base.height);
            case DERECHA:
            default:
                return new Rectangle(base.x + base.width, base.y, alcance, base.height);
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (personaje == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

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
                g2d.drawImage(hoja,
                        posX + anchoPantalla, posY, posX, posY + altoPantalla,
                        srcX1, srcY1, srcX2, srcY2,
                        this
                );
            } else {
                g2d.drawImage(hoja,
                        posX, posY, posX + anchoPantalla, posY + altoPantalla,
                        srcX1, srcY1, srcX2, srcY2,
                        this
                );
            }


            Rectangle hb = getHitbox(posX, posY);
            g2d.setColor(Color.RED);
            g2d.drawRect(hb.x, hb.y, hb.width, hb.height);

        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(posX, posY, 40, 40);
        }


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
                        g2d.drawImage(hojaEnemigo,
                                ex + anchoDestino, ey, ex, ey + altoDestino,
                                srcX1, srcY1, srcX2, srcY2, this);
                    } else {
                        g2d.drawImage(hojaEnemigo,
                                ex, ey, ex + anchoDestino, ey + altoDestino,
                                srcX1, srcY1, srcX2, srcY2, this);
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
                            g2d.drawImage(hojaMuerte,
                                    ex + anchoDestino, ey, ex, ey + altoDestino,
                                    srcX1, srcY1, srcX2, srcY2, this);
                    } else {
                        g2d.drawImage(hojaMuerte, ex, ey, ex + anchoDestino, ey + altoDestino, srcX1, srcY1, srcX2, srcY2, this);
                    }
                }
            }
            }
        }
    }

    if (mostrandoFlashback) {
      Image imagenAMostrar;
        switch (poseActual) {
        case 0: imagenAMostrar = imagenFlashback1; break;
        case 1: imagenAMostrar = imagenFlashback2; break;
        case 2: imagenAMostrar = imagenFlashback3; break;
        default: imagenAMostrar = imagenFlashback4; break;
    }
    Graphics2D g2dFlashback = (Graphics2D) g;
    Composite composicionOriginal = g2dFlashback.getComposite(); // la guardamos para restaurarla después
    g2dFlashback.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 0.6f = 60% opaco
    g2dFlashback.drawImage(imagenAMostrar, 0, 0, getWidth(), getHeight(), this);
    g2dFlashback.setComposite(composicionOriginal); // restauramos para que no afecte lo que se dibuje después
}
}

    public void setNivelActual(Nivel nivel){
        this.nivelActual = nivel;
    }

//Solo para probar es este metodo. Lo voy a usar solo hasta poder conectar Nivel/Gato
//en juegoFrame.iniciarNivelConPersonaje(); despues, lo voy a borrar y voy a usar activar flashback gato normal
public void probarFlashbackGato() {
Gato gatoDePrueba = new Gato();
gatoDePrueba.reproducirSonidoFlashback();
personaje.aplicarLentitud();
mostrandoFlashback = true;

Timer parpadeo = new Timer(200, null);
int[] contador = {0};
parpadeo.addActionListener(e -> {
    mostrandoFlashback = !mostrandoFlashback;
    if (mostrandoFlashback) {
        poseActual = random.nextInt(4);
    }
    repaint();
    contador[0]++;
    if (contador[0] >= 10) { // 10 parpadeos = 2 segundos totales
        ((Timer) e.getSource()).stop();
        mostrandoFlashback = false;
        personaje.quitarLentitud();
        repaint();
    }

});
parpadeo.start();
}




    public void activarFlashbackGato(){
        if (nivelActual == null || nivelActual.getGato() == null) return; //significaria q este nivel no tiene gato
    
    
    Gato gato = nivelActual.getGato();
    gato.reproducirSonidoFlashback();
    personaje.aplicarLentitud();
    mostrandoFlashback = true;

    //necesitaba un timer para el parpadeo de la imagen 
    Timer parpadeo = new Timer(200, null);
    int[] contador = {0};
    parpadeo.addActionListener(e -> {
        mostrandoFlashback = !mostrandoFlashback;
                if (mostrandoFlashback) {
                    poseActual = random.nextInt(4);
                }
        repaint();
        contador[0]++;
        if (contador[0] >= 10) { // 10 parpadeos = 2 segundos totales
            ((Timer) e.getSource()).stop();
            mostrandoFlashback = false;
            personaje.quitarLentitud();
            repaint();
        }
    });
    parpadeo.start();
    }

}


