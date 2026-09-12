package Modelo;

public class itemBateria extends Item {

// -- ATRIBUTOS --

    private int cantidadCarga;

// -- CONSTRUCTOR --
    public itemBateria(){
        super("Bateria");
        cantidadCarga = 10;
    }

// -- GET y SET --

    public int getCantidadCarga(){
        return cantidadCarga;
    }

// -- METODOS --

    public void aplicarCarga(Personaje p) {
        p.recargarLinterna(this.cantidadCarga);
    }

    @Override
    public void interactuar(Personaje p) {
        aplicarCarga(p);
    }
}    
