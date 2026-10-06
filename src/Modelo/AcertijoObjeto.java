package Modelo;

public class AcertijoObjeto extends Acertijo {

    // -- ATRIBUTOS --

     private final String objetoRequerido;

    // -- CONSTRUCTOR --
    public AcertijoObjeto (int id, String descripcion, int columna, int fila, String objetoRequerido){
        super(id, descripcion, columna, fila);
        this.objetoRequerido = objetoRequerido;
    }

    @Override
    public void interactuar(Personaje p) {
        ObservadorAcertijo obs = getObservador();
        if (getResuelto() || obs == null) return;

        if (p.getInventario().contieneItem(objetoRequerido)) {
            setResuelto(true);
            obs.acertijoResuelto(this);
        } else {
            obs.acertijoBloqueado(this);
        }
    }
}
