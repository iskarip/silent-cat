package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel que se muestra cuando el jugador interactúa con un acertijo numérico.
 *
 * Es una vista "tonta": no conoce al Modelo. Solo recibe texto y cantidad de dígitos
 * desde el controlador, expone sus botones y devuelve lo que el jugador escribió.
 * Los listeners de INTENTAR y SALIR los pone el ControladorAcertijo.
 */
public class AcertijoPanel extends JPanel {

    // -- ATRIBUTOS --

    private double distanciaCasilla = 0.32; // proporción vertical del panel donde se dibujan las casillas
    private Image imagenFondo;                    // imagen (centrada, sin deformarse)
    private final JPanel contenedorCasillas;      // agrupa las casillas de los dígitos
    private final List<JTextField> casillasDigitos; // una casilla por dígito de la respuesta
    private final JLabel etiquetaMensaje;         // muestra el enunciado y el feedback
    private final BotonJuego botonIntentar;
    private final BotonJuego botonSalir;

    // -- CONSTRUCTOR --

    public AcertijoPanel() {
        setLayout(null); // posiciones manuales, se calculan en doLayout()
        setOpaque(false); // deja ver el nivel de fondo; la sombra se dibuja en paintComponent

        casillasDigitos = new ArrayList<>();

        // Texto del enunciado / feedback (el color cambia según el caso)
        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Serif", Font.ITALIC, 40));
        etiquetaMensaje.setForeground(new Color(180, 20, 20));
        add(etiquetaMensaje);

        // Contenedor de las casillas (se llena en mostrarEnunciado)
        contenedorCasillas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        contenedorCasillas.setOpaque(false);
        add(contenedorCasillas);

        // Botones: sin listeners acá, los agrega el controlador.
        // setFocusable(false) para que no le roben el foco a las casillas.
        botonIntentar = new BotonJuego("/Recursos/UI/Acertijos/Candado/Botones/Intentar.png",
                "/Recursos/UI/Acertijos/Candado/Botones/IntentarHover.png",
                "Recursos/Sonidos/UI/sonido3.wav", 0, 0);
        botonIntentar.setFocusable(false);
        add(botonIntentar);

        botonSalir = new BotonJuego("/Recursos/UI/Acertijos/Candado/Botones/Salir.png",
                "/Recursos/UI/Acertijos/Candado/Botones/SalirHover.png",
                "Recursos/Sonidos/UI/sonido3.wav", 0, 0);
        botonSalir.setFocusable(false);
        add(botonSalir);

    }

    // -- ACCESO A LOS BOTONES (para que el controlador les agregue listeners) --

    public BotonJuego getBotonIntentar() { return botonIntentar; }
    public BotonJuego getBotonSalir() { return botonSalir; }

    // -- API PARA EL CONTROLADOR --

    // Arma la pantalla para un acertijo: pone el enunciado y crea una casilla por dígito.
    // Se recrean las casillas cada vez, así siempre están vacías al abrir el acertijo.
    public void mostrarEnunciado(String descripcion, int cantidadDigitos, String rutaImagen) {
        
        imagenFondo = GestorSprites.cargarImagen(rutaImagen);

        etiquetaMensaje.setText(descripcion);
        etiquetaMensaje.setForeground(Color.WHITE);

        contenedorCasillas.removeAll();
        casillasDigitos.clear();

        for (int i = 0; i < cantidadDigitos; i++) {
            casillasDigitos.add(crearCasilla(i));
            contenedorCasillas.add(casillasDigitos.get(i));
        }

        contenedorCasillas.revalidate();
        contenedorCasillas.repaint();
        repaint();

        if (!casillasDigitos.isEmpty()) {
            // invokeLater: este método se llama antes de que la pantalla sea visible
            SwingUtilities.invokeLater(() -> casillasDigitos.get(0).requestFocusInWindow());
        }
    }

    // Muestra un mensaje de error en rojo (por ejemplo "Incorrecto. Intentos restantes: 2")
    public void mostrarFeedback(String mensaje) {
        etiquetaMensaje.setText(mensaje);
        etiquetaMensaje.setForeground(Color.RED);
    }

    // Junta los dígitos de todas las casillas en un solo String (las vacías suman "")
    public String getRespuestaIngresada() {
        StringBuilder codigo = new StringBuilder();
        for (JTextField campo : casillasDigitos) {
            codigo.append(campo.getText().trim());
        }
        return codigo.toString();
    }

    // -- CREACIÓN DE CASILLAS --

    // Crea una casilla con su comportamiento de teclado:
    //  - solo acepta dígitos, y un dígito nuevo reemplaza al anterior
    //  - al escribir un dígito, el foco pasa a la casilla siguiente
    //  - con BORRAR vacía la casilla; si ya estaba vacía, borra la anterior y vuelve a ella
    private JTextField crearCasilla(int indice) {
        JTextField campo = new JTextField("");
        campo.setFont(new Font("Monospaced", Font.BOLD, 28));
        campo.setForeground(Color.WHITE);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setPreferredSize(new Dimension(48, 52));
        campo.setOpaque(false);
        campo.setBorder(null);

        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                e.consume(); // el texto lo manejamos nosotros, no el JTextField
                if (!Character.isDigit(e.getKeyChar())) return;

                campo.setText(String.valueOf(e.getKeyChar())); // reemplaza, no agrega
                if (indice < casillasDigitos.size() - 1) {
                    casillasDigitos.get(indice + 1).requestFocusInWindow();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) return;

                if (campo.getText().isEmpty() && indice > 0) {
                    JTextField anterior = casillasDigitos.get(indice - 1);
                    anterior.setText("");
                    anterior.requestFocusInWindow();
                } else {
                    campo.setText("");
                }
            }
        });
        return campo;
    }

    // -- DISEÑO Y DIBUJADO --

    // Posiciones como proporciones del panel, para que sirvan con cualquier tamaño de ventana
    @Override
    public void doLayout() {
        super.doLayout();
        int w = getWidth();
        int h = getHeight();

        contenedorCasillas.setBounds((int) (w * 0.25), (int) (h * distanciaCasilla ), (int) (w * 0.50), (int) (h * 0.12));
        Dimension tamIntentar = botonIntentar.getPreferredSize();
        Dimension tamSalir = botonSalir.getPreferredSize();
        botonIntentar.setBounds(w / 2 - tamIntentar.width - 10, (int) (h * 0.80), tamIntentar.width, tamIntentar.height);
        botonSalir.setBounds(w / 2 + 10, (int) (h * 0.80), tamSalir.width, tamSalir.height);
        etiquetaMensaje.setBounds(0, (int) (h * 0.04), w, 60);
    }

    // Rectángulo donde se dibuja la imagen: centrada y sin deformarse
    private Rectangle calcularRectImagen() {
        if (imagenFondo == null) return new Rectangle(0, 0, getWidth(), getHeight());

        int anchoImg = imagenFondo.getWidth(this);
        int altoImg = imagenFondo.getHeight(this);
        if (anchoImg <= 0 || altoImg <= 0) return new Rectangle(0, 0, getWidth(), getHeight());

        double escala = Math.min((double) getWidth() / anchoImg, (double) getHeight() / altoImg);
        int w = (int) (anchoImg * escala);
        int h = (int) (altoImg * escala);
        return new Rectangle((getWidth() - w) / 2, (getHeight() - h) / 2, w, h);
    }

    // Dibuja primero una sombra oscura sobre el nivel y encima la imagen del candado
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (imagenFondo != null) {
            Rectangle r = calcularRectImagen();
            g.drawImage(imagenFondo, r.x, r.y, r.width, r.height, this);
        }
    }
}