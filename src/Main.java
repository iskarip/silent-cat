import javax.swing.SwingUtilities;

import Controlador.ControladorPrincipal;
import Vista.JuegoFrame; 

public class Main {
    public static void main (String [] args) {
        SwingUtilities.invokeLater(() -> {
            JuegoFrame ventana = new JuegoFrame();
            new ControladorPrincipal(ventana);
        });
    }
}
        //crea el objeto JuegoFrame y como su constructor ya hace setVisible(true),
        //la ventana del juego se abre automatico.
