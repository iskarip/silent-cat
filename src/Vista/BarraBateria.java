package Vista;

import Modelo.Personaje;
import Modelo.Linterna;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;


//Esta nueva clase solo va a dibujar la barra de la bateria de la linterna en la pantalla

public class BarraBateria implements InterfazVisual {

    private static final int X= 20;
    private static final int Y= 20;
    private static final int ANCHO = 150;
    private static final int ALTO = 18;
    private static final int RADIO_BORDE = 8;

    @Override
    public void renderizar(Graphics2D g2d, int ancho, int alto, NivelPanel panel){
        Personaje personaje = panel.getPersonaje();
            if (personaje == null || personaje.getLinterna() == null){
                return;
            } 

        Linterna linterna = personaje.getLinterna();
        int bateria = linterna.getBateria();

        //detalles esteticos de la barrita de bateria uwu
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 12)); 
        g2d.setColor(Color.WHITE);
        g2d.drawString("LINTERNA", X, Y - 4);

        
        //fondo de la barra
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE);

        //el relleno de la bateria
        int anchoRelleno = (int) (ANCHO * (bateria / 100.0));
        if (anchoRelleno > 0) {
            g2d.setColor(linterna.bateriaBaja() ? Color.RED : new Color(255, 210, 80));
            g2d.fillRoundRect(X, Y, anchoRelleno, ALTO, RADIO_BORDE, RADIO_BORDE);
        }
        //borde de la barra
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(X, Y, ANCHO, ALTO, RADIO_BORDE, RADIO_BORDE); 

/*//para poder ver el porcentaje de la bateria
// (no me gusto pero si les gusta q sepan q se puede poner)
        
        String texto = bateria + "%";
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int anchoTexto = g2d.getFontMetrics().stringWidth(texto);
        int xTexto = X + (ANCHO - anchoTexto) / 2;
        int yTexto = Y + ALTO - 5;
        g2d.setColor(Color.BLACK);
        g2d.drawString(texto, xTexto, yTexto);
        g2d.setColor(Color.WHITE);
        g2d.drawString(texto, xTexto - 1, yTexto - 1);
*/
    }

}