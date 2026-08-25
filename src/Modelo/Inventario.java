package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    //-- ATRIBUTOS --


    private List<Item> items; //atributo principal, es una lista q va a guardar objetos de tipo item

    // -- CONSTRUCTOR--

    public Inventario() {
        this.items = new ArrayList<>();
    }


    public void quitarItem(Item item) {
        items.remove(item); // saca un item especifico de la lista
    }

    // -- GET --

    public List<Item> getItems() {
        return items;
    }

    // -- METODOS --

    public void agregarItem(Item item) {
        item.recoger();
        items.add(item); // esto agrega el item a la lista
    }

    public void sacarItem(Item item) {
        items.remove(item);
    }

    public boolean contieneItem (String nombreItem){
        for (Item item : items){
            if (item.getNombre().equalsIgnoreCase(nombreItem)){
                return true;
            }
        }
        return false;
    }
}
