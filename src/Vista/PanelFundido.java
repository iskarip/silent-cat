package Vista;

import javax.swing.*;
import java.awt.*;

public class PanelFundido  extends JPanel {

    private float opacidad = 0f;
    private Timer timerFundido;

    public PanelFundido() {
        setOpaque(false);
    }

    public void fundirYCorrer(Runnable alMedioDelFundido) {
        setVisible(true);
        opacidad = 0f;

        timerFundido = new Timer(10, null);
        timerFundido.addActionListener(e -> {
            opacidad += 0.05f;
            if (opacidad >= 1f) {
                opacidad = 1f;
                timerFundido.stop();
                repaint();
                alMedioDelFundido.run();
                iniciarAparicion();
            } else {
                repaint();
            }
        });
        timerFundido.start();
    }

    private void iniciarAparicion() {
        timerFundido = new Timer(10, null);
        timerFundido.addActionListener(e -> {
            opacidad -= 0.05f;
            if (opacidad <= 0f) {
                opacidad = 0f;
                timerFundido.stop();
                setVisible(false);
            }
            repaint();
        });
        timerFundido.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

}
