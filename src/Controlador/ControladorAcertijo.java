package Controlador;

import Modelo.Acertijo;
import Modelo.AcertijoNumerico;
import Modelo.EspacioBase;
import Modelo.ObservadorAcertijo;
import Modelo.ObservadorAmbiente;
import Modelo.Personaje;
import Vista.AcertijoPanel;
import Vista.NivelPanel;

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

    public void comprobarProximidad(EspacioBase espacioActual, Personaje personaje) {
        if (acertijoActivo || espacioActual == null || personaje == null) return;

        boolean alAlcance = espacioActual.acertijoAlAlcance(personaje);
        if (alAlcance && !avisoMostrado) {
            ambiente.mostrarMensaje("Presiona E para interactuar");
        }
        avisoMostrado = alAlcance;
    }

    public void intentarInteraccion(EspacioBase espacioActual, Personaje personaje, ControladorTeclado teclado) {
        if (acertijoActivo || espacioActual == null || personaje == null) return;

        if (espacioActual.acertijoAlAlcance(personaje)) {
            Acertijo acertijo = espacioActual.getAcertijo();
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