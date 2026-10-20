package Vista;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;

public class VictoriaPanel extends JPanel {
    private final Image imagenFondo;
    private Runnable alContinuar;

    public VictoriaPanel() {
        imagenFondo = new ImageIcon(getClass().getResource("/Recursos/UI/Victoria/pantalla_victoria.jpg")).getImage();
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { continuar(); }
        });
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { continuar(); }
        });
    }

    public void setAlContinuar(Runnable r) { this.alContinuar = r; }
    private void continuar() { if (alContinuar != null) alContinuar.run(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}