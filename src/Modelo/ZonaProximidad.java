package Modelo;

//va a ser superclase para poder usarla de herencia en zonamensaje y puntoflashback
//del flashback del gato que se va a ctivar por zona y de los mensajes que vamos a ir mostrando

//por lo tanto es la base de cualquier zona a la que el pj se acerque

//va a ser abstracta porque siempre se va a usar coon un puntoFlashback o una zonamensaje, nunca solita
public abstract class ZonaProximidad {
    protected int x, y, radio;
    protected boolean activado = false;

    //va a recibir columna y fila, como nuestros items
    public ZonaProximidad(int columna, int fila, int radioEnTiles){
        this.x = columna * MapaColision.TILE;
        this.y = fila * MapaColision.TILE;
        this.radio = radioEnTiles *MapaColision.TILE;
    }

    public boolean estaCerca(Personaje personaje){
        int dx = personaje.getPosicionX() - x;
        int dy = personaje.getPosicionY() - y;
        return Math.sqrt((double) dx * dx + (double) dy * dy) <= radio;
    }

    public boolean getActivado(){
        return activado;
    }

    public void marcarActivado(){
      this.activado = true;
    }

}
