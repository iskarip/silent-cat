package Modelo;

public class Personaje extends Entidad {

// -- ATRIBUTOS --

    private int posicionX;
    private int posicionY;
    private String nombrePersonaje; 
    private int nivelEstamina;
    private Arma arma;          //definir
    private Linterna linterna;  //definir
    private Inventario inventario;

// -- CONSTRUCTOR --

    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
        super(100,10);
        this.nombrePersonaje = nombrePersonaje;
        this.nivelEstamina = 100;
        this.arma = new Arma();
        this.linterna = linterna;
        this.inventario = new Inventario();
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

    public Inventario getInventario(){
        return inventario;
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

    public void interactuarCon(Interactuable objeto) {
        objeto.interactuar(this);
    }

    public void agregarAlInventario(Item item){
        inventario.agregarItem(item);
    }
}