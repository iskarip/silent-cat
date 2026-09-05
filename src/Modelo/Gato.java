package Modelo;

public class Gato {
    
//ATRIBUTOS

private Boolean encontrado;

//CONSTRUCTOR

public Gato(){
        this.encontrado = false; //el gato se inicializa como no encontrado
}

//GET Y SET

public boolean getEncontrado(){
    return encontrado;
}

public void setEncontrado(boolean encontrado){
    this.encontrado = encontrado;
}

//METODOS
public void maullar(){
    System.out.println("El gato emite un maullido ecoico y distorsionado...");
}



}
