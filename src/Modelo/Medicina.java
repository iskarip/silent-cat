package Modelo;

public class Medicina extends Item{
    
//ATRIBUTOS

    private int curacion;

//CONSTRUCTOR
    public Medicina(String nombre,int curacion){
        super (nombre); //aca lo que hace es llamar al constructor de item y pone recogido con false
    this.curacion = curacion;
    }

//GET Y SET

public int getCantidadCuracion(){
    return curacion;
}

public void setCuracion(int curacion){
    this.curacion = curacion;
}

//METODOS
public void aplicarCuracion(Personaje personaje){
    if (!isRecogido()){
        recoger(); //entonces marca el item como recogido a verdadero
        int nuevaVida = personaje.getPuntosVida() + this.curacion;
        personaje.setPuntosVida(nuevaVida);
    }

}

@Override
    public void interactuar(Personaje p){
        aplicarCuracion(p);
}
    
}
