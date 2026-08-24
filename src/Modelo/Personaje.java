package Modelo;

public class Personaje extends Entidad {

// -- ATRIBUTOS --

    private int posicionX;
    private int posicionY;
    private String nombrePersonaje; 
    private int nivelEstamina;
    private Arma arma;          //definir
    private Linterna linterna;  //definir

// -- CONSTRUCTOR --

    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
        super(100,10);
        this.nombrePersonaje = nombrePersonaje;
        this.nivelEstamina = 100;
        this.arma = new Arma();
        this.linterna = linterna;
    }

// -- Getters y Setters --

    public String getNombrePersonaje(){
        return nombrePersonaje;
    }

    public int getNivelEstamina(){
        return nivelEstamina;
    }

    public Arma getArma(){
        return arma;
    }

    public Linterna getLinterna(){
        return linterna;
    }


// -- METODOS -- 

    public void moverPersonaje(int deltaX,int deltaY){  // Cuando tengamos definida las entidades de Enemigo, definimos si este metodo vale la pena que este en personaje o en Entidad
        this.posicionX += deltaX;
        this.posicionY += deltaY;
    }
    @Override

    public void atacar(Entidad objetivo) {
        if (arma.usarArma()) {
            int danio = arma.calcularDanio();
            objetivo.recibirDanio(danio);
        }
    }

    public void usarLinterna(){
        if (linterna.getEncendido()){
            linterna.apagarLinterna();
        } else {
            linterna.encenderLinterna();
        }
    }
   
    public boolean revisarBateria() {
        return linterna.bateriaBaja();
    }
    
    public void recargarLinterna(int cantidad) {
       linterna.recargarLinterna(cantidad); // si bateria es un item q el personaje obtiene, ese item le da la cantidad a recargar
    }


}