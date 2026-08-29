package Modelo;
public class Item {
// -- ATRIBUTOS --
    protected boolean recogido;

    public Item (){
        this.recogido = false;
    }

// -- GET y SET --

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
}
