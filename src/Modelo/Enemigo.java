package Modelo;

public class Enemigo extends Entidad {
    
//como vamos a tener diferentes enemigos definimos usar id enemigo
//para diferenciar lo que hace cada uno.

//--ATRIBUTOS--

private int idEnemigo;
private int direccion = 1;
private int danioBase;
//--CONSTRUCTOR--


public Enemigo(int puntosVida, int danioBase, int idEnemigo){
super(puntosVida);
    this.danioBase= danioBase;
    this.idEnemigo= idEnemigo;

}

//--GET Y SET--

public int getIdEnemigo(){
    return this.idEnemigo;
}

public void setIdEnemigo(int idEnemigo){
    this.idEnemigo = idEnemigo;
}

public int getDanioBase() {
    return this.danioBase;
}

//uso de override para los metodos heredados, atacar y recibirDanio

//sacar este println cuando la Vista (Swing) muestre el ataque visualmente
@Override
public void atacar(Entidad objetivo){
    if(objetivo !=null){
        System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
        objetivo.recibirDanio(this.danioBase);
    }   
}

//metodo propio del enemigo (mover, patrullar)

public void moverHaciaJugador(Personaje jugador) {
    int deltaX = 0;
    int deltaY = 0;

    if (jugador.getPosicionX() > this.getPosicionX()) {
        deltaX = 1;
    } else if (jugador.getPosicionX() < this.getPosicionX()) {
        deltaX = -1;
    }

    if (jugador.getPosicionY()> this.getPosicionY()) {
        deltaY = 1;
    } else if (jugador.getPosicionY() < this.getPosicionY()) {
        deltaY = -1;
    }

    mover (deltaX, deltaY);
    System.out.println("El enemigo se acerca. Ten cuidado!");
}


public void patrullar(){
    System.out.println("El enemigo " + this.idEnemigo + "esta patrullando la zona cercana al jugador. ");
}



}
