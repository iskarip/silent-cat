package Modelo;

public abstract class Acertijo implements Interactuable, Posicionable {

    private static final int RADIO_INTERACCION = 40;
    private static final int INTENTOS_MAXIMOS = 3;
    private static final int DANIO_FALLO = 10;

    private final int id;
    private final String descripcion;
    private boolean resuelto;
    private int posicionX;
    private int posicionY;
    private int intentosRestantes = INTENTOS_MAXIMOS;
    private ObservadorAcertijo observador;

    public Acertijo(int id, String descripcion, int columna, int fila) {
        this.id = id;
        this.descripcion = descripcion;
        this.resuelto = false;
        colocarEnTile(columna, fila);
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public boolean getResuelto() { return resuelto; }
    protected void setResuelto(boolean resuelto) { this.resuelto = resuelto; }
    public int getIntentosRestantes() { return intentosRestantes; }

    public void setObservador(ObservadorAcertijo observador) { this.observador = observador; }
    protected ObservadorAcertijo getObservador() { return observador; }

    @Override public int getPosicionX() { return posicionX; }
    @Override public void setPosicionX(int x) { this.posicionX = x; }
    @Override public int getPosicionY() { return posicionY; }
    @Override public void setPosicionY(int y) { this.posicionY = y; }

    public boolean estaAlAlcance(Personaje personaje) {
        if (resuelto || personaje == null) return false;

        int centroX = posicionX + MapaColision.TILE / 2;
        int centroY = posicionY + MapaColision.TILE / 2;

        return Math.hypot(personaje.getPosicionX() - centroX,
                personaje.getPosicionY() - centroY) <= RADIO_INTERACCION;
    }

    public void reiniciarIntentos() {
        this.intentosRestantes = INTENTOS_MAXIMOS;
    }

    public boolean validarRespuesta(String respuesta) {
        if (this.resuelto) return true;
        boolean esCorrecta = verificarRespuesta(respuesta);
        if (esCorrecta) {
            this.resuelto = true;
        }
        return esCorrecta;
    }

    public boolean validarRespuesta(String respuesta, Personaje personaje) {
        return validarRespuesta(respuesta);
    }

    public boolean responder(String respuesta, Personaje personaje) {
        if (validarRespuesta(respuesta, personaje)) {
            return true;
        }

        intentosRestantes--;
        if (intentosRestantes <= 0 && personaje != null) {
            personaje.recibirDanio(DANIO_FALLO);
        }
        return false;
    }

    protected abstract boolean verificarRespuesta(String respuesta);

    @Override
    public void interactuar(Personaje p) {
        System.out.println(this.descripcion);
    }
}

