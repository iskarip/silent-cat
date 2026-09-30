package Vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
 
//va a mostrar en el centro de la pantalla un mensaje
//y se va desvaneciendo solo con el tiempo.
//la opaciodad se calcula en el renderizar

public class MensajeAmbiental implements InterfazVisual {
    
    private static final long DURACION_MS = 4000; //el tiempo de duracion visible
    private static final long DESVANECIDO_MS= 1500; //durante estos ultimos ms, se desvaneceria

    private String mensajeActual = null;
    private long momentoMostrado = 0;

    public void mostrarMensaje(String mensaje){
        this.mensajeActual= mensaje;
        this.momentoMostrado = System.currentTimeMillis();//en lugar de crear otro timer para no sobrecargarloo
    }

    @Override
    public void renderizar (Graphics2D g2d, int ancho, int alto, NivelPanel panel){
        if (mensajeActual == null) return;
 
        long transcurrido = System.currentTimeMillis() - momentoMostrado;
        if (transcurrido >= DURACION_MS) {
            mensajeActual = null;
            //ya terminaria de mostrarse asiq no dibuja nada mas
            return;
        }

        //defino la opacidad, 1.0 es visible ya, y va a bajar hasta 0
        float opacidad = 1f;
        long restante = DURACION_MS - transcurrido;
        if (restante < DESVANECIDO_MS) {
            opacidad = restante / (float) DESVANECIDO_MS;
        }
        Composite original = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));
 
        g2d.setFont(new Font("Serif", Font.ITALIC, 40));
        g2d.setColor(new Color(180, 20, 20)); // rojizo, tipo Silent Hill
        int anchoTexto = g2d.getFontMetrics().stringWidth(mensajeActual);
        int x = (ancho - anchoTexto) / 2;
        int y = alto - 80;
        g2d.drawString(mensajeActual, x, y);
 
        g2d.setComposite(original);
    }
}