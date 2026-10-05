package Modelo;

public abstract class Acertijo implements Interactuable, Posicionable {

    private static final int RADIO_INTERACCION = 40;

    private final int id;
    private final String descripcion;
    private boolean resuelto;
    private int posicionX;
    private int posicionY;
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
}