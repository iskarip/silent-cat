public abstract class Entidad {

    // -- ATRIBUTOS --
    protected int puntosVida;

    // -- CONSTRUCTOR --
    public Entidad (int puntosVida) {
        this.puntosVida = 100;
    }

    // -- SET's y GET's --
    public int getPuntosVida (){
        return puntosVida;
    }


    public void setPuntosVida(int puntosVida) {
        if (puntosVida < 0 ) {
            this.puntosVida = 0;
        } else if (puntosVida > 100) {
            this.puntosVida = 100;
        } else {
            this.puntosVida = puntosVida;
        }

    }

    // -- METODOS --

    public void recibirDanio (int cantidad){
        setPuntosVida(this.puntosVida - cantidad);
    }

    public abstract void atacar(Entidad objetivo);  // método SIN cuerpo, termina en ";" — cada subclase decide cómo atacar


}
