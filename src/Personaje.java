public class Personaje extends Entidad {

// -- ATRIBUTOS --

    private Int posicionX;
    private Int posicionY;
    private String nombrePersonaje; 
    private Int nivelEstamina;
    private Arma arma;          //definir
    private Linterna linterna;  //definir

// -- CONSTRUCTOR --

    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
        super(100,10);
        this.nombrePersonaje = nombrePersonaje;
        this.ninvelEstamina = 100;
        this.Arma = arma;
        this.linterna = linterna;
    }

// -- Getters y Setters --

    public getNombrePersonaje(){
        return puntosVida;
    }

    public getNivelEstamina(){
        return danioBase;
    }

    // getter arma y linterna

// -- METODOS -- 

    public void moverPersonaje(int deltaX,int deltaY){  // Cuando tengamos definida las entidades de Enemigo, definimos si este metodo vale la pena que este en personaje o en Entidad
        this.posicionX += deltaX;
        this.posicionY += deltaY;
    }


}