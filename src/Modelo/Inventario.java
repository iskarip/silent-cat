package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private List<Item> items; //atributo principal, es una lista q va a guardar objetos de tipo item

    // constructor

    public Inventario() {
        this.items = new ArrayList<>();
    }

    // metodos

    public void agregarItem(Item item){
      item.recoger();
      items.add(item); // esto agrega el item a la lista
    }

    public void quitarItem(Item item) {
        items.remove(item); // saca un item especifico de la lista
    }

    // getter

    public List<Item> getItems(){ 
        return items;
    }
}
