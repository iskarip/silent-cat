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








}
