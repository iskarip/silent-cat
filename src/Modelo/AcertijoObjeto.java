package Modelo;

public class AcertijoObjeto extends Acertijo {

    // -- ATRIBUTOS --
    private final String objetoRequerido;

    // -- CONSTRUCTOR --
    public AcertijoObjeto(int id, String descripcion, int columna, int fila, String objetoRequerido) {
        super(id, descripcion, columna, fila);
        this.objetoRequerido = objetoRequerido;
    }

    public String getObjetoRequerido() {
        return objetoRequerido;
    }

    @Override
    protected boolean verificarRespuesta(String respuesta) {
        if (respuesta == null || objetoRequerido == null) return false;
        return objetoRequerido.equalsIgnoreCase(respuesta.trim());
    }

    @Override
    public boolean validarRespuesta(String respuesta, Personaje personaje) {
        if (getResuelto()) return true;
        if (personaje == null || personaje.getInventario() == null) return false;

        boolean tieneElObjeto = personaje.getInventario().contieneItem(this.objetoRequerido);
        if (tieneElObjeto) {
            setResuelto(true);
        }
        return tieneElObjeto;
    }

    @Override
    public void interactuar(Personaje p) {
        ObservadorAcertijo obs = getObservador();
        if (getResuelto() || obs == null) return;

        if (p != null && p.getInventario() != null && p.getInventario().contieneItem(objetoRequerido)) {
            setResuelto(true);
            obs.acertijoResuelto(this);
        } else {
            obs.acertijoBloqueado(this);
        }
    }
}
