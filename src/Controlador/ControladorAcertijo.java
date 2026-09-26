package Controlador;

import Modelo.Acertijo;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.AcertijoPanel;
import Vista.JuegoFrame;

public class ControladorAcertijo {

// -- ATRIBUTOS --
    private static final int RADIO_ACTIVACION = 50; // distancia para activar el acetijo (prueba)

    private static final int INTENTOS_MAXIMOS = 3;
    private static final int DANIO_FALLO = 10;

    private final AcertijoPanel vista;
    private final JuegoFrame ventanaPrincipal;

    private boolean acertijoActivo = false;
    private Acertijo acertijoActual;
    private Personaje personajeActual;
    private int intentosRestantes;


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
            iniciarAcertijo(acertijo, personaje);
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

    private void iniciarAcertijo(Acertijo acertijo, Personaje personaje) {
        this.acertijoActivo = true;
        this.acertijoActual = acertijo;
        this.personajeActual = personaje;
        this.intentosRestantes = INTENTOS_MAXIMOS;


        vista.mostrarEnunciado(acertijo);
        ventanaPrincipal.mostrarPantalla("acertijo");
    }

    private void manejarRespuesta() {
        if (acertijoActual == null) return;

//aca la idea es que el propio acertijo decide como validarse
//Si comparando el texto ingresado por el usuario (acertijoNumerico owo)
//revisando el inventario (acertijoobjeto u.u)
//El controlador no necesita saber cual es cual

        boolean esCorrecta = acertijoActual.validarRespuesta(vista.getRespuestaIngresada(), personajeActual);

        if (esCorrecta) {
            acertijoActual.setResuelto(true); // Se marca como resuelto
            volverAlNivel();
        return;
        }
        
            intentosRestantes --; 
        
            if (intentosRestantes <=0){
                if(personajeActual != null){
                    personajeActual.recibirDanio(DANIO_FALLO);
                }
                vista.mostrarFeedback("Sin intentos restantes :(. " + acertijoActual.getDescripcion());
                volverAlNivel();
            } else {
                vista.mostrarFeedback("Respuesta incorrecta. Te quedan : " + intentosRestantes + " intentos.");
                }
        }
        
  
    private void volverAlNivel() {
        acertijoActivo = false;
        acertijoActual = null;
        personajeActual = null;
        ventanaPrincipal.mostrarPantalla("nivel");
    }

}
