public class Enemigo extends Entidad {
    
//como vamos a tener diferentes enemigos definimos usar id enemigo
//para diferenciar lo que hace cada uno.

//--ATRIBUTOS--

private int idEnemigo;

//--CONSTRUCTOR--

public Enemigo(int puntosVida, int danioBase, int idEnemigo){
super(puntosVida, danioBase);

this.idEnemigo= idEnemigo;
}

//--GET Y SET--

public int getIdEnemigo(){
    return this.idEnemigo;
}

public void setIdEnemigo(int idEnemigo){
    this.idEnemigo = idEnemigo;
}

//uso de override para los metodos heredados, atacar y recibirDanio

@Override
public void atacar(Entidad objetivo){
    if(objetivo !=null){
        System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
        objetivo.recibirDanio(this.danioBase);
    }   
}

//metodo propio del enemigo (mover, patrullar)

public void moverHaciaJugador(){
    System.out.println("El enemigo " + this.idEnemigo + " se mueve hacia la posicion del jugador. ");
}

public void patrullar(){
    System.out.println("El enemigo " + this.idEnemigo + "esta patrullando la zona cercana al jugador. ");
}


}
