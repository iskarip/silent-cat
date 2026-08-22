public abstract class Entidad {

    // -- ATRIBUTOS --
    //private String nombre;
    protected int puntosVida;
    //private int nivelEstamina;
    // private armaPersonaje;
    // private linterna

    // -- CONSTRUCTOR --
    public Entidad (int puntosVida) {
        //this.nombre = nombre;
        this.puntosVida = 100;
        //this.nivelEstamina = 100;
    }

    // -- SET's y GET's --
    //public String getNombre () {
        ///
        /// }

    public int getPuntosVida (){
        return puntosVida;
    }

    //public int getNivelEstamina(){
       // return nivelEstamina;
    //}

    public void setPuntosVida(int puntosVida) {
        if (this.puntosVida < 0 ) {
            puntosVida = 0;
        } else if (this.puntosVida > 100) {
            puntosVida = 100;
        } else {
            this.puntosVida = puntosVida;
        }

    }

    /*
    public void setPuntosVida (int puntosVida) {
    if (puntosVida < this.puntosVida) {
    puntosVida=this.puntosVida;
    } else if (puntosVida > this.puntosVida){
    puntosVida = this.puntosVida;
    } else {
    this.puntosVida =puntosVida;
    }
     */

    /*
    public void setNivelEstamina (int nivelEstamina) {
        if (this.nivelEstamina < 0) {
            nivelEstamina = 0;
        } else if (this.nivelEstamina > 100) {
            nivelEstamina = 100;
        } else {
            this.nivelEstamina = nivelEstamina;
        }
    }

     */






}
