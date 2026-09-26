package Modelo;

public class AcertijoObjeto extends Acertijo {

    // -- ATRIBUTOS --

    private String respuesta;

    // -- CONSTRUCTOR --
    public AcertijoObjeto (int id, String descripcion, String respuesta){
        super (id, descripcion);
        this.respuesta = respuesta;
    }

    // -- GET's y SET's--

    public String getRespuesta(){
        return this.respuesta;
    }

    // -- METODOS HEREDADOS --
    @Override
    protected boolean verificarRespuesta(String respuesta) {
        return this.respuesta.equalsIgnoreCase(respuesta);
    }

//este sobreescribe la validadcion, en lugar de comparar lo que el jugador
//escriba, revisa si tiene el objeto necesario en el inventario. El texto que haya escrito
//en el cuadro de respuesta se va a ignorar, por lo que solo va a hacer falta tener el item no es necesario escribir.

   @Override
    public boolean validarRespuesta(String respuesta, Personaje personaje) {
        if (this.getResuelto()) {
            return true;
        }
        boolean tieneElObjeto = personaje.getInventario().contieneItem(this.respuesta);
        if (tieneElObjeto) {
            this.setResuelto(true);
        }
        return tieneElObjeto;
    }
}
