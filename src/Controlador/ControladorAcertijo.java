package Controlador;

import Modelo.Acertijo;
import Modelo.AcertijoNumerico;
import Modelo.ObservadorAcertijo;
import Modelo.ObservadorAmbiente;
import Modelo.Personaje;
import Vista.AcertijoPanel;
import Vista.NivelPanel;
import Modelo.Nivel;

public class ControladorAcertijo implements ObservadorAcertijo {

    private final AcertijoPanel vista;
    private final NivelPanel nivelPanel;
    private final ObservadorAmbiente ambiente;

    private boolean acertijoActivo = false;
    private boolean avisoMostrado = false;
    private AcertijoNumerico acertijoActual;
    private Personaje personajeActual;

    public ControladorAcertijo(NivelPanel nivelPanel, ObservadorAmbiente ambiente) {
        this.nivelPanel = nivelPanel;
        this.vista = nivelPanel.getAcertijoPanel();
        this.ambiente = ambiente;

        this.vista.setAlResponder(this::manejarRespuesta);
        this.vista.setAlSalir(this::volverAlNivel);
    }

    public void comprobarProximidad(Nivel nivel, Personaje personaje) {
        if (acertijoActivo || nivel == null || personaje == null) return;

        boolean alAlcance = nivel.getAcertijoAlAlcance(personaje) != null;
        if (alAlcance && !avisoMostrado) {
            ambiente.mostrarMensaje("Presiona E para interactuar");
        }
        avisoMostrado = alAlcance;
    }

    public void intentarInteraccion(Nivel nivel, Personaje personaje, ControladorTeclado teclado) {
        if (acertijoActivo || nivel == null || personaje == null) return;

        Acertijo acertijo = nivel.getAcertijoAlAlcance(personaje);
        if (acertijo != null) {
            acertijo.setObservador(this);
            if (teclado != null) teclado.limpiarTeclas();
            personaje.interactuarCon(acertijo);
        }
    }

    public boolean estaActivo() {
        return acertijoActivo;
    }

    @Override
    public void acertijoSolicitado(AcertijoNumerico acertijo, Personaje personaje) {
        this.acertijoActivo = true;
        this.acertijoActual = acertijo;
        this.personajeActual = personaje;

        vista.mostrarEnunciado(acertijo);
        nivelPanel.mostrarAcertijo();
    }

    private void manejarRespuesta() {
        if (acertijoActual == null) return;

        AcertijoNumerico acertijo = acertijoActual; // volverAlNivel() deja el atributo en null
        boolean acerto = acertijo.responder(vista.getRespuestaIngresada(), personajeActual);

        if (acerto) {
            volverAlNivel();
        } else if (acertijoActivo) { // si se agotaron los intentos, el callback ya cerró la pantalla
            vista.mostrarFeedback("Incorrecto. Intentos restantes: " + acertijo.getIntentosRestantes());
        }
    }

    @Override
    public void acertijoSinIntentos(AcertijoNumerico acertijo) {
        ambiente.mostrarMensaje("Sin intentos restantes. Recibiste daño.");
        volverAlNivel();
    }

    public void volverAlNivel() {
        acertijoActivo = false;
        acertijoActual = null;
        personajeActual = null;
        nivelPanel.ocultarAcertijo();
    }

    @Override
    public void acertijoResuelto(Acertijo acertijo) {
        ambiente.mostrarMensaje("Funcionó.");
    }

    @Override
    public void acertijoBloqueado(Acertijo acertijo) {
        ambiente.mostrarMensaje("Me falta algo para esto...");
    }
}