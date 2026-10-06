package Vista;

import Modelo.AcertijoNumerico;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

// Panel que se muestra cuando el jugador interactúa con un acertijo numérico.

public class AcertijoPanel extends JPanel {

    private Image imagenFondo;
    private final JPanel contenedorCasillas;
    private final List<JTextField> casillasDigitos;
    private final JLabel etiquetaMensaje;
    private final BotonJuego botonIntentar;
    private final BotonJuego botonSalir;

    private Runnable alResponder;
    private Runnable alSalir;

    public AcertijoPanel() {
        setLayout(null);

        casillasDigitos = new ArrayList<>();

        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Serif", Font.ITALIC, 40));
        etiquetaMensaje.setForeground(new Color(180, 20, 20));
        setOpaque(false);
        add(etiquetaMensaje);


        contenedorCasillas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        contenedorCasillas.setOpaque(false);
        add(contenedorCasillas);

        botonIntentar = new BotonJuego("/Recursos/UI/Acertijos/Candado/Botones/Intentar.png",
                "/Recursos/UI/Acertijos/Candado/Botones/IntentarHover.png",
                "Recursos/Sonidos/sonido3.wav", 0, 0);
        botonIntentar.setFocusable(false);
        botonIntentar.addActionListener(e -> { if (alResponder != null) alResponder.run(); });
        add(botonIntentar);

        botonSalir = new BotonJuego("/Recursos/UI/Acertijos/Candado/Botones/Salir.png",
                "/Recursos/UI/Acertijos/Candado/Botones/SalirHover.png",
                "Recursos/Sonidos/sonido3.wav", 0, 0);
        
        botonSalir.setFocusable(false);
        botonSalir.addActionListener(e -> { if (alSalir != null) alSalir.run(); });
        add(botonSalir);
    }

    public void setAlResponder(Runnable accion) { this.alResponder = accion; }
    public void setAlSalir(Runnable accion) { this.alSalir = accion; }

    public void mostrarEnunciado(AcertijoNumerico acertijo) {
        imagenFondo = GestorSprites.cargarImagen(acertijo.getRutaImagen());

        etiquetaMensaje.setText(acertijo.getDescripcion());
        contenedorCasillas.removeAll();
        casillasDigitos.clear();

        for (int i = 0; i < acertijo.getLargoRespuesta(); i++) {
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

    public String getRespuestaIngresada() {
        StringBuilder codigo = new StringBuilder();
        for (JTextField campo : casillasDigitos) {
            codigo.append(campo.getText().trim());
        }
        return codigo.toString();
    }

    public void mostrarFeedback(String mensaje) {
        etiquetaMensaje.setText(mensaje);
        etiquetaMensaje.setForeground(Color.RED);
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

    // Posiciones como proporciones del panel, para que sirvan con cualquier tamaño de ventana
    @Override
    public void doLayout() {
        super.doLayout();
        int w = getWidth();
        int h = getHeight();

        contenedorCasillas.setBounds((int) (w * 0.25), (int) (h * 0.40), (int) (w * 0.50), (int) (h * 0.12));
        botonIntentar.setBounds(w / 2 - 120, (int) (h * 0.65), 110, 35);
        botonSalir.setBounds(w / 2 + 10, (int) (h * 0.65), 100, 35);
        etiquetaMensaje.setBounds(0, (int) (h * 0.85), w, 35);
    }

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