package Modelo;

public class itemBateria extends Item {

// -- ATRIBUTOS --

    private int cantidadCarga;

// -- CONSTRUCTOR --

    public itemBateria(){
        super("Bateria");
        cantidadCarga = 10;
        this.rutaImagen = "/Recursos/Sprites/Items/bateria.png"; // ajustá al path real
    }

// -- GET y SET --

    public int getCantidadCarga(){
        return cantidadCarga;
    }

// -- METODOS --

    public void aplicarCarga(Personaje p) {
        if (!isRecogido()){
            recoger();
            p.recargarLinterna(this.cantidadCarga);
        }
    }

    @Override
    public void interactuar(Personaje p) {
        aplicarCarga(p);
    }
}    
