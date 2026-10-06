package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class CandadoPanel extends JPanel implements VistaAcertijo {

    private BufferedImage imagenCandado;
    private JTextField[] casillasDigitos;
    private JButton botonIntentar;
    private JButton botonSalir;
    private JLabel etiquetaMensaje;

    private Runnable alResponder;
    private Runnable alSalir;

    private static final int[] POS_X_CASILLAS = {220, 280, 340, 400};
    private static final int POS_Y_CASILLAS = 220;
    private static final int ANCHO_CASILLA = 45;
    private static final int ALTO_CASILLA = 50;

    public CandadoPanel() {
        setLayout(null);
        cargarImagen();
        construirInterfaz();
    }

    private void cargarImagen() {
        try {
            imagenCandado = ImageIO.read(getClass().getResourceAsStream("/Recursos/UI/Acertijos/candado.png"));
        } catch (Exception e) {
            imagenCandado = null;
        }
    }

    private void construirInterfaz() {
        casillasDigitos = new JTextField[4];

        for (int i = 0; i < 4; i++) {
            JTextField campo = new JTextField("0");
            campo.setFont(new Font("Monospaced", Font.BOLD, 28));
            campo.setHorizontalAlignment(JTextField.CENTER);
            campo.setBounds(POS_X_CASILLAS[i], POS_Y_CASILLAS, ANCHO_CASILLA, ALTO_CASILLA);

            campo.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (campo.getText().length() >= 1 || !Character.isDigit(e.getKeyChar())) {
                        e.consume();
                    }
                }
            });

            casillasDigitos[i] = campo;
            add(campo);
        }

        botonIntentar = new JButton("INTENTAR");
        botonIntentar.setBounds(240, 310, 110, 35);
        botonIntentar.setFocusable(false);
        botonIntentar.addActionListener(e -> { if (alResponder != null) alResponder.run(); });
        add(botonIntentar);

        botonSalir = new JButton("SALIR");
        botonSalir.setBounds(360, 310, 100, 35);
        botonSalir.setFocusable(false);
        botonSalir.addActionListener(e -> { if (alSalir != null) alSalir.run(); });
        add(botonSalir);

        etiquetaMensaje = new JLabel("", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetaMensaje.setForeground(Color.YELLOW);
        etiquetaMensaje.setBounds(150, 160, 500, 30);
        add(etiquetaMensaje);
    }

    @Override public void setAlResponder(Runnable accion) { this.alResponder = accion; }
    @Override public void setAlSalir(Runnable accion) { this.alSalir = accion; }

    @Override
    public String getRespuestaIngresada() {
        StringBuilder codigo = new StringBuilder();
        for (JTextField campo : casillasDigitos) {
            codigo.append(campo.getText().trim());
        }
        return codigo.toString();
    }

    // Desacoplado: recibe String en vez de Acertijo
    public void mostrarEnunciado(String descripcion) {
        etiquetaMensaje.setText(descripcion);
        etiquetaMensaje.setForeground(Color.WHITE);
        reiniciar();
    }

    @Override
    public void mostrarFeedback(String mensaje) {
        etiquetaMensaje.setText(mensaje);
        etiquetaMensaje.setForeground(Color.RED);
    }

    @Override
    public void reiniciar() {
        for (JTextField campo : casillasDigitos) {
            campo.setText("0");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenCandado != null) {
            g.drawImage(imagenCandado, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}