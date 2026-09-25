package Modelo;

public class itemMedicina extends Item{
    
//ATRIBUTOS

    private int curacion;

//CONSTRUCTOR

    public itemMedicina( int curacion){
        super("Medicina");
        this.curacion = curacion;
        this.rutaImagen = "/Recursos/Sprites/Items/medicina.png"; // ajustá al path real
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
