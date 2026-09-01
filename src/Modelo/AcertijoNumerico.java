package Modelo;

public class AcertijoNumerico extends Acertijo {

// -- ATRIBUTOS -- 

    private int respuesta;

// -- CONSTRUCTOR --

    public AcertijoNumerico (int id, String descripcion, int respuesta){
        super(id, descripcion);
        this.respuesta = respuesta;
    }

// -- GET y SET

    public int getRespuesta(){
        return this.respuesta;
    }
    
// -- METODOS --

    @Override
    protected boolean verificarRespuesta(String respuesta) {
      try {
           int intento = Integer.parseInt(respuesta);
           return intento == this.respuesta;
        }  catch (NumberFormatException error) { // para que el juego no se rompa si ingresan algo distinto a un numero
           return false;
        }
    }

    }


