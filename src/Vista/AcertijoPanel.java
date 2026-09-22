package Vista;

import javax.swing.*;
import Modelo.Acertijo;
import Modelo.Personaje;

public class AcertijoPanel extends JPanel {

    // ATRIBUTOS
    private JuegoFrame ventanaPrincipal;
    private JLabel labelEnunciado;
    private JTextField campoRespuesta;

    private Acertijo acertijoActual;
    private Personaje personajeActual;

    private static final int INTENTOS_MAXIMOS= 3;
    private static final int DANIO_FALLO = 10;
    private int intentosRestantes;


    // CONSTRUCTOR
    public AcertijoPanel(JuegoFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        labelEnunciado = new JLabel("..."); 
        campoRespuesta = new JTextField(15);

        JButton botonResponder = new JButton( "Responder");
        botonResponder.addActionListener((e -> validarRespuesta()));

//eso nos permite responder presionando Enter dentro del campo de texto, sin necesidad de hacer clic en el boton.

        add(labelEnunciado);
        add(campoRespuesta);
        add(botonResponder);

    }

    // METODOS

    // cuando se llama desde el controlador y el personaje se encuentra 
    // con un acertijo, le pasa q acertijo hay q mostrar en pantalla.
    public void mostrarAcertijo(Acertijo acertijo, Personaje personaje) {
        this.acertijoActual = acertijo;
        this.personajeActual = personaje;
        this.intentosRestantes = INTENTOS_MAXIMOS;

        labelEnunciado.setText(acertijo.getDescripcion());
        campoRespuesta.setText("");
    }

    private void validarRespuesta() {
        if (acertijoActual == null) return;
//si el acertijo ya estaba resuelto, va a devolver true sin comparar nada


        boolean esCorrecta = acertijoActual.validarRespuesta(campoRespuesta.getText());
       
        if(esCorrecta) {
            System.out.println("¡Correcto!");
            ventanaPrincipal.mostrarPantalla("nivel"); //vuelve al nivel
        } else {
            intentosRestantes--;
                if (intentosRestantes <= 0) {
                    if(personajeActual != null){
                        personajeActual.recibirDanio(DANIO_FALLO);
                    }
                System.out.println("Sin intentos. Pista: " + acertijoActual.getDescripcion());
                ventanaPrincipal.mostrarPantalla("nivel"); //vuelve al nivel
                } else {
                    System.out.println("Incorrecto. Te quedan " + intentosRestantes + " intentos.");
                
            campoRespuesta.setText("");
            campoRespuesta.requestFocus();
            }
        }
    }
}

    /* TODO: 1. agregar un elemento para validar la respuesta (boton "responder", tecla enter, etc)
       2. si el jugador falla, intentos libres, limitados, con pista, con castigo (restar vida)?
       3. tener en cuenta el caso de un acertijo ya resuelto para no dejar que se vuelva a responder sin sentido.
       4. Cuando este el Controlador, enlazar con:
       acertijoPanel.mostrarAcertijo(acertijoActual);
       ventanaPrincipal.mostrarPantalla("acertijo");   */
