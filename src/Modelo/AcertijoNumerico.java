package Modelo;

public class AcertijoNumerico extends Acertijo{

// -- ATRIBUTOS -- 

    private int respuesta;

// -- CONSTRUCTOR --

    public AcertijoNumerico (int id, String descripcion, int respuesta){
        super(id, descripcion);
        this.respuesta = respuesta;
    }

// -- GET y SET

// -- METODOS --

    @Override
    public boolean validarRespuesta(String respuesta) {
        int intento = Integer.parseInt(respuesta); // convertís el String a int
        this.resuelto = (intento == this.respuesta);
        return resuelto;
    }

    }


