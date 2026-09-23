package Vista;

import javax.swing.*;
import Modelo.Acertijo;

public class AcertijoPanel extends JPanel {

    // ATRIBUTOS
    private JLabel labelEnunciado;
    private JTextField campoRespuesta;
    private JLabel labelFeedback;
    private JButton botonResponder;

    // CONSTRUCTOR
    public AcertijoPanel() {

        labelEnunciado = new JLabel("..."); 
        labelFeedback = new JLabel("");
        campoRespuesta = new JTextField(15);
        botonResponder = new JButton("Responder");

//eso nos permite responder presionando Enter dentro del campo de texto, sin necesidad de hacer clic en el boton.

        add(labelEnunciado);
        add(campoRespuesta);
        add(botonResponder);
        add(labelFeedback);

    }

// -- GETTERS --

    public String getRespuestaIngresada() {
        return campoRespuesta.getText();
    }

    public JButton getBotonResponder() {
        return botonResponder;
    }

    public JTextField getCampoRespuesta() {
        return campoRespuesta;
    }

// -- METODOS --

    // Prepara la pantalla para un acertijo nuevo. Ya no recibe Personaje
    // eso va en el controlado, esta clase solo muestra texto

    public void mostrarEnunciado(Acertijo acertijo) {
        labelEnunciado.setText(acertijo.getDescripcion());
        labelFeedback.setText(" ");
        campoRespuesta.setText("");
        campoRespuesta.requestFocusInWindow();
    }

    // Para avisps tipo "incorrecto" o pistas
    public void mostrarFeedback(String mensaje) {
        labelFeedback.setText(mensaje);
        campoRespuesta.setText("");
        campoRespuesta.requestFocusInWindow();
    }


}
