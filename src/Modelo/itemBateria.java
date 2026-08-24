package Modelo;

public class itemBateria extends Item {

// -- ATRIBUTOS --
    
    private int cantidadCarga;

// -- CONSTRUCTOR --
    public itemBateria(){
        super();
        cantidadCarga = 10;
    }

// -- GET y SET --

    public int getCantidadCarga(){
        return cantidadCarga;
    }

// --  METODOS --
    
}
