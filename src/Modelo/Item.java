package Modelo;

public abstract class Item implements Interactuable {

    // -- ATRIBUTOS --
    protected String nombre;
    protected boolean recogido;

    // --CONSTRUCTOR --

    public Item (String nombre){
        this.recogido = false;
        this.nombre = nombre;
    }

    // -- GET y SET --

    public String getNombre () {
        return nombre;
    }

    public boolean isRecogido(){
        return recogido;
    }

    public void setRecogido(boolean valor){
        recogido = valor;
    }

    // -- METODOS --

    public void recoger(){
       recogido = true;
    }

    @Override
    public void interactuar(Personaje p) {
        recoger();
        p.getInventario().agregarItem(this);
    }

    // Este metodo hace que al interactuar (de la clase Interactuable), el elemento
    // se guarde en el inventario. Cada subclase (como Medicina) lo define respectivamente
    // si necesita una reaccion distinta.
}
