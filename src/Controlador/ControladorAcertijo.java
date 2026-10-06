package Controlador;

import Modelo.Acertijo;
import Modelo.AcertijoNumerico;
import Modelo.Nivel;
import Modelo.ObservadorAcertijo;
import Modelo.ObservadorAmbiente;
import Modelo.Personaje;
import Vista.AcertijoPanel;
import Vista.NivelPanel;
import java.awt.event.ActionListener;

/**
 * Controla todo el flujo de los acertijos:
 *  1. Detecta cuando el personaje está cerca de uno y avisa "Presiona E".
 *  2. Al apretar E, le pide al acertijo que interactúe (cada acertijo decide qué hacer).
 *  3. Muestra/oculta la pantalla del acertijo y maneja los botones INTENTAR y SALIR.
 *
 * Implementa ObservadorAcertijo: el modelo le avisa qué pasó (pidió pantalla, se resolvió,
 * quedó bloqueado, se quedó sin intentos) y el controlador decide cómo mostrarlo.
 * Así el modelo nunca conoce a la Vista.
 */

public class ControladorAcertijo implements ObservadorAcertijo {

    // -- ATRIBUTOS --

    private final AcertijoPanel vista;
    private final NivelPanel nivelPanel;
    private final ObservadorAmbiente ambiente; // para mostrar mensajes en pantalla

    // Listeners guardados en atributos para poder sacarlos después en desconectar()
    private final ActionListener alIntentar = e -> manejarRespuesta();
    private final ActionListener alSalir = e -> volverAlNivel();

    private boolean acertijoActivo = false; // true mientras la pantalla del acertijo está abierta
    private boolean avisoMostrado = false;  // evita repetir "Presiona E" en cada tick mientras sigue cerca
    private AcertijoNumerico acertijoActual; // el acertijo que se está resolviendo ahora
    private Personaje personajeActual;       // el personaje que lo está resolviendo

    // -- CONSTRUCTOR --

    public ControladorAcertijo(NivelPanel nivelPanel, ObservadorAmbiente ambiente) {
        this.nivelPanel = nivelPanel;
        this.vista = nivelPanel.getAcertijoPanel();
        this.ambiente = ambiente;

        // La vista es "tonta": solo expone los botones, los listeners los pone el controlador
        this.vista.getBotonIntentar().addActionListener(alIntentar);
        this.vista.getBotonSalir().addActionListener(alSalir);
    }

    // -- PROXIMIDAD E INTERACCIÓN --

    // Se llama en cada tick del game loop.
    // Muestra "Presiona E" una sola vez al acercarse; al alejarse se vuelve a habilitar el aviso.
    public void comprobarProximidad(Nivel nivel, Personaje personaje) {
        if (acertijoActivo || nivel == null || personaje == null) return;

        boolean alAlcance = nivel.getAcertijoAlAlcance(personaje) != null;
        if (alAlcance && !avisoMostrado) {
            ambiente.mostrarMensaje("Presiona E para interactuar");
        }
        avisoMostrado = alAlcance;
    }

    // Se llama al apretar E. Si hay un acertijo al alcance, el personaje interactúa con él.
    public void intentarInteraccion(Nivel nivel, Personaje personaje, ControladorTeclado teclado) {
        if (acertijoActivo || nivel == null || personaje == null) return;

        Acertijo acertijo = nivel.getAcertijoAlAlcance(personaje);
        if (acertijo != null) {
            acertijo.setObservador(this); // el acertijo nos va a avisar el resultado a nosotros
            if (teclado != null) teclado.limpiarTeclas(); // evita que queden teclas "pegadas" al cambiar de pantalla
            personaje.interactuarCon(acertijo); // polimorfismo: cada tipo de acertijo decide qué hacer
        }
    }

    // El game loop lo consulta para pausar la actualización mientras el acertijo está abierto
    public boolean estaActivo() {
        return acertijoActivo;
    }

    // -- CALLBACKS DEL MODELO (ObservadorAcertijo) --

    // El acertijo numérico pide abrir su pantalla
    @Override
    public void acertijoSolicitado(AcertijoNumerico acertijo, Personaje personaje) {
        this.acertijoActivo = true;
        this.acertijoActual = acertijo;
        this.personajeActual = personaje;
        vista.mostrarEnunciado(acertijo.getDescripcion(), acertijo.getLargoRespuesta(),
                       acertijo.getRutaImagen());
        nivelPanel.mostrarAcertijo();
    }

    // Se agotaron los intentos (el modelo ya aplicó el daño al personaje): avisa y cierra la pantalla
    @Override
    public void acertijoSinIntentos(AcertijoNumerico acertijo) {
        ambiente.mostrarMensaje("Sin intentos restantes. Recibiste daño.");
        volverAlNivel();
    }

    // Acertijo resuelto sin pantalla (por ejemplo el de objeto)
    @Override
    public void acertijoResuelto(Acertijo acertijo) {
        ambiente.mostrarMensaje("Funcionó.");
    }

    // Falta algo para resolverlo (por ejemplo, no tiene el objeto necesario)
    @Override
    public void acertijoBloqueado(Acertijo acertijo) {
        ambiente.mostrarMensaje("Me falta algo para esto...");
    }

    // -- ACCIONES DE LOS BOTONES --

    // Listener del botón INTENTAR
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

    // Cierra la pantalla del acertijo y limpia el estado. También es el listener del botón SALIR
    public void volverAlNivel() {
        acertijoActivo = false;
        acertijoActual = null;
        personajeActual = null;
        nivelPanel.ocultarAcertijo();
    }

    // -- LIMPIEZA --

    // Saca los listeners de los botones. Hace falta porque el AcertijoPanel es el mismo
    // en toda la ejecución, pero cada partida nueva crea un controlador nuevo:
    // sin esto los listeners viejos se acumularían.
    public void desconectar() {
        vista.getBotonIntentar().removeActionListener(alIntentar);
        vista.getBotonSalir().removeActionListener(alSalir);
    }
}