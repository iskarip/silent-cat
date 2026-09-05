package Vista;

import javax.swing.*;
import Modelo.Acertijo;

public class AcertijoPanel extends JPanel {

    // ATRIBUTOS
    private JuegoFrame ventanaPrincipal;
    private JLabel labelEnunciado;
    private JTextField campoRespuesta;

    private Acertijo acertijoActual;

    // CONSTRUCTOR
    public AcertijoPanel(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        labelEnunciado = new JLabel("..."); 
        campoRespuesta = new JTextField(15);

        add(labelEnunciado);
        add(campoRespuesta);

    }

    // METODOS

    // cuando se llama desde el controlador y el personaje se encuentra 
    // con un acertijo, le pasa q acertijo hay q mostrar en pantalla.
    public void mostrarAcertijo(Acertijo acertijo) {
        this.acertijoActual = acertijo;
        labelEnunciado.setText(acertijo.getDescripcion());
        campoRespuesta.setText("");
    }

    /* TODO: 1. agregar un elemento para validar la respuesta (boton "responder", tecla enter, etc)
       2. si el jugador falla, intentos libres, limitados, con pista, con castigo (restar vida)?
       3. tener en cuenta el caso de un acertijo ya resuelto para no dejar que se vuelva a responder sin sentido.
       4. Cuando este el Controlador, enlazar con:
       acertijoPanel.mostrarAcertijo(acertijoActual);
       ventanaPrincipal.mostrarPantalla("acertijo");   */
}