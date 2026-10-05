package Vista;

import Modelo.Acertijo;

public interface VistaAcertijo {
    void mostrarEnunciado(Acertijo acertijo);
    void mostrarFeedback(String mensaje);
    String getRespuestaIngresada();
    void setAlResponder(Runnable accion);
    void setAlSalir(Runnable accion);
    void reiniciar();
}
