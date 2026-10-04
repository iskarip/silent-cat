package Controlador;

import Modelo.Acertijo;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.ObservadorAcertijo;
import Vista.AcertijoPanel;
import Vista.JuegoFrame;

public class ControladorAcertijo implements ObservadorAcertijo {

    // -- ATRIBUTOS --
    private final AcertijoPanel vista;
    private final JuegoFrame ventanaPrincipal;

    private boolean acertijoActivo = false;
    private Acertijo acertijoActual;
    private Personaje personajeActual;

    // -- CONSTRUCTOR --
    public ControladorAcertijo(AcertijoPanel vista, JuegoFrame ventanaPrincipal) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;

        // el controlador decide que pasa al responder, la vista solo avisa que se apreto el boton
        this.vista.getBotonResponder().addActionListener(e -> manejarRespuesta());
        this.vista.getCampoRespuesta().addActionListener(e -> manejarRespuesta());
    }

    // -- METODOS --
    public void comprobarActivacion(Nivel nivelActual, Personaje personaje) {
        if (acertijoActivo || nivelActual == null || personaje == null) return;

        if (nivelActual.acertijoAlAlcance(personaje)) {
            Acertijo acertijo = nivelActual.getAcertijo();
            acertijo.setObservador(this);
            personaje.interactuarCon(acertijo);
        }
    }

    public boolean estaActivo() {
        return acertijoActivo;
    }

    @Override
    public void acertijoSolicitado(Acertijo acertijo, Personaje personaje) {
        iniciarAcertijo(acertijo, personaje);
    }

    private void iniciarAcertijo(Acertijo acertijo, Personaje personaje) {
        this.acertijoActivo = true;
        this.acertijoActual = acertijo;
        this.personajeActual = personaje;
        acertijo.reiniciarIntentos();

        vista.mostrarEnunciado(acertijo);
        ventanaPrincipal.mostrarPantalla("acertijo");
    }

    private void manejarRespuesta() {
        if (acertijoActual == null) return;

        // El acertijo decide cómo validarse y que pasa con los intentos
        // el controlador solo traduce el resultado a lo que ve el jugador
        boolean acerto = acertijoActual.responder(vista.getRespuestaIngresada(), personajeActual);

        if (acerto) {
            volverAlNivel();
        } else if (acertijoActual.getIntentosRestantes() <= 0) {
            vista.mostrarFeedback("Sin intentos restantes :(. " + acertijoActual.getDescripcion());
            volverAlNivel();
        } else {
            vista.mostrarFeedback("Respuesta incorrecta. Te quedan : "
                    + acertijoActual.getIntentosRestantes() + " intentos.");
        }
    }

    private void volverAlNivel() {
        acertijoActivo = false;
        acertijoActual = null;
        personajeActual = null;
        ventanaPrincipal.mostrarPantalla("nivel");
    }
}
