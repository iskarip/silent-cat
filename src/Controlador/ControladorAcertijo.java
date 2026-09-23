package Controlador;

import Modelo.Acertijo;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.AcertijoPanel;
import Vista.JuegoFrame;

public class ControladorAcertijo {

// -- ATRIBUTOS --
    private static final int RADIO_ACTIVACION = 50; // distancia para activar el acetijo (prueba)

    private final AcertijoPanel vista;
    private final JuegoFrame ventanaPrincipal;

    private boolean acertijoActivo = false;
    private Acertijo acertijoActual;

// -- CONSTRUCTOR --
    public ControladorAcertijo(AcertijoPanel vista, JuegoFrame ventanaPrincipal) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // el controlador decide que pasa al responder, la vista solo avisa q se apreto el boton
        this.vista.getBotonResponder().addActionListener(e -> manejarRespuesta());
        this.vista.getCampoRespuesta().addActionListener(e -> manejarRespuesta());
    }

// -- METODOS --
    public void comprobarActivacion(Nivel nivelActual, Personaje personaje) {
        if (acertijoActivo || nivelActual == null || personaje == null) return;

        Acertijo acertijo = nivelActual.getAcertijo();
        if (acertijo == null || acertijo.getResuelto()) return;

        double distancia = distanciaAlTrigger(nivelActual, personaje);
        
        if (distancia <= RADIO_ACTIVACION) {
            iniciarAcertijo(acertijo);
        }
    }

    public boolean estaActivo() {
        return acertijoActivo;
    }

    private double distanciaAlTrigger(Nivel nivelActual, Personaje personaje) {
        int dx = personaje.getPosicionX() - nivelActual.getAcertijoX();
        int dy = personaje.getPosicionY() - nivelActual.getAcertijoY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void iniciarAcertijo(Acertijo acertijo) {
        this.acertijoActivo = true;
        this.acertijoActual = acertijo;

        vista.mostrarEnunciado(acertijo);
        ventanaPrincipal.mostrarPantalla("acertijo");
    }

    private void manejarRespuesta() {
        if (acertijoActual == null) return;

        boolean esCorrecta = acertijoActual.validarRespuesta(vista.getRespuestaIngresada());

        if (esCorrecta) {
            acertijoActual.setResuelto(true); // Se marca como resuelto
            volverAlNivel();
        } else {
            // le avisa que fallo y puede volver a intentar
            vista.mostrarFeedback("Respuesta incorrecta. Intentalo de nuevo.");
        }
    }

    private void volverAlNivel() {
        acertijoActivo = false;
        acertijoActual = null;
        ventanaPrincipal.mostrarPantalla("nivel");
    }

}
