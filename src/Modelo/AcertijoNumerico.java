package Modelo;

public class AcertijoNumerico extends Acertijo {

    private static final int INTENTOS_MAXIMOS = 3;
    private static final int DANIO_FALLO = 10;

    private final int respuesta;
    private final String rutaImagen;
    private int intentosRestantes = INTENTOS_MAXIMOS;

    public AcertijoNumerico(int id, String descripcion, int columna, int fila, int respuesta, String rutaImagen) {
        super(id, descripcion, columna, fila);
        this.respuesta = respuesta;
        this.rutaImagen = rutaImagen    ;
    }

    public int getLargoRespuesta() { return String.valueOf(respuesta).length(); }
    public String getRutaImagen() { return rutaImagen; }
    public int getIntentosRestantes() { return intentosRestantes; }


    public boolean responder(String intento, Personaje personaje) {
        if (getResuelto()) return true;

        if (esCorrecta(intento)) {
            setResuelto(true);
            if (getObservador() != null) getObservador().acertijoResuelto(this);
            return true;
        }

        intentosRestantes--;
        if (intentosRestantes <= 0) {
            personaje.recibirDanio(DANIO_FALLO);
            intentosRestantes = INTENTOS_MAXIMOS;
        }
        return false;
    }

    private boolean esCorrecta(String intento) {
        try {
            return Integer.parseInt(intento) == respuesta;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void interactuar(Personaje p) {
        if (getResuelto() || getObservador() == null) return;
        getObservador().acertijoSolicitado(this, p);
    }
}

