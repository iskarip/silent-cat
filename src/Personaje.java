public class Personaje {

    // -- ATRIBUTOS --
    private String nombre;
    private int puntosVida;
    private int nivelEstamina;
    // private armaPersonaje;
    // private linterna

    // -- CONSTRUCTOR --
    public Personaje (String nombre) {
        this.nombre = nombre;
        this.puntosVida = 100;
        this.nivelEstamina = 100;
    }

    // -- SET's y GET's --
    public String getNombre () {
        return nombre;
    }

    public int getPuntosVida (){
        return puntosVida;
    }

    public int getNivelEstamina(){
        return nivelEstamina;
    }

    public void setPuntosVida(int puntosVida) {
        if (this.puntosVida < 0 ) {
            puntosVida = 0;
        } else if (this.puntosVida > 100) {
            puntosVida = 100;
        } else {
            this.puntosVida = puntosVida;
        }

    }

    public void setNivelEstamina (int nivelEstamina) {
        if (this.nivelEstamina < 0) {
            nivelEstamina = 0;
        } else if (this.nivelEstamina > 100) {
            nivelEstamina = 100;
        } else {
            this.nivelEstamina = nivelEstamina;
        }
    }






}
