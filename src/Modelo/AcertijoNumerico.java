package Modelo;

public class AcertijoNumerico extends Acertijo {

    private final int respuesta;
    private final String rutaImagen;
    private double distanciaCasilla = 0.32; // proporción vertical del panel donde se dibujan las casillas

    public AcertijoNumerico(int id, String descripcion, int columna, int fila, int respuesta, String rutaImagen, double distanciaCasilla) {
        super(id, descripcion, columna, fila);
        this.respuesta = respuesta;
        this.rutaImagen = rutaImagen;
        this.distanciaCasilla = distanciaCasilla;
    }

    public AcertijoNumerico(int id, String descripcion, int columna, int fila, int respuesta, String rutaImagen) {
        super(id, descripcion, columna, fila);
        this.respuesta = respuesta;
        this.rutaImagen = rutaImagen;
    }

    public double getDistanciaCasilla() {
        return distanciaCasilla;
    }

    public int getLargoRespuesta() {
        return String.valueOf(respuesta).length();
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public int getRespuesta() {
        return respuesta;
    }

    @Override
    protected boolean verificarRespuesta(String intento) {
        try {
            return Integer.parseInt(intento.trim()) == this.respuesta;
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

