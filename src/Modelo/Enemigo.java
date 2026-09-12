package Modelo;

public class Enemigo extends Entidad {
    
//como vamos a tener diferentes enemigos definimos usar id enemigo
//para diferenciar lo que hace cada uno.

//--ATRIBUTOS--

private int idEnemigo;
private int direccion = 1;
private int danioBase;
private int radioDeteccion = 80; // distancia en pixeles a la que "nota" al jugador (80 para probar)
private boolean alertado = false; // si ya vio al jugador o no

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

// metodo que decide si se detecta al jugador
public boolean detectaAlJugador (Personaje jugador, boolean linternaEncendida) {
    double dx = jugador.getPosicionX() - this.getPosicionX();
    double dy = jugador.getPosicionY() - this.getPosicionY();
    double distancia = Math.hypot(dx, dy);

    int radioEfectivo = linternaEncendida ? radioDeteccion + 40 : radioDeteccion;

    return distancia <= radioEfectivo;
}

public void actualizarComportamiento (Personaje jugador, boolean linternaEncendida) {
    if (detectaAlJugador(jugador, linternaEncendida)) {
        alertado = true;
        moverHaciaJugador(jugador);
    } else if (alertado) { // perdió de vista al jugador, pero sigue en alerta un rato antes de volver a patrullar
        patrullar();
    } else {
        patrullar();
    }
}

}
