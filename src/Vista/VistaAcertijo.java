package Vista;

public interface VistaAcertijo {
    void setAlResponder(Runnable accion);
    void setAlSalir(Runnable accion);
    String getRespuestaIngresada();
    void mostrarEnunciado(String descripcion);
    void mostrarFeedback(String mensaje);
    void reiniciar();
}


