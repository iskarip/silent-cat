public class Acertijo {  // public abstract class? creo que lo vemos mas adelante.

// -- METODOS --

    private int id;
    private String descripcion;
    private boolean resuelto;

// -- CONSTRUCTOR --

    public Acertijo (int id, String descripcion){
        this.id = id;
        this.descripcion = descripcion;
        resuelto = false;
    }

// -- GET y SET --

    public int getId(){
        return id;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public boolean getResuelto(){
        return resuelto;
    }

    public void setResuelto(boolean resuelto){
        this.resuelto = resuelto;
    }

// -- METODOS --

}
