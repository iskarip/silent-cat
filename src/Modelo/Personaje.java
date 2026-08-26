package Modelo;

/**
 * Representa al jugador. Hereda de Entidad (vida, daño base, posición y el
 * método mover()), y le suma todo lo que es propio del personaje: arma,
 * linterna, inventario y la lógica de interactuar con el mundo (items, acertijos).
 *
 * IMPORTANTE para quien la lea: Personaje NO implementa Interactuable.
 * La relación con esa interfaz es de DEPENDENCIA (la usa como tipo de
 * parámetro en interactuarCon), no de REALIZACIÓN. Quien "es" un
 * Interactuable son Item y Acertijo, no Personaje.
 */

public class Personaje extends Entidad {

// -- ATRIBUTOS --

    private String nombrePersonaje;

    // TODO (pendiente de decisión grupal): este atributo se inicializa pero
    // ningún método lo lee ni lo modifica todavía. Si el juego va a tener
    // correr/esquivar, falta consumirEstamina()/recuperarEstamina().
    // Si no, conviene sacarlo para no dejar código muerto.

    private int nivelEstamina;

    private Arma arma;
    private Linterna linterna;
    private Inventario inventario;

// -- CONSTRUCTOR --

    // Recibe arma y linterna ya construidas (no las crea el personaje),
    // así queda desacoplado de cómo se arman esos objetos.
    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
        super(100,10); // Asignación de Vida y daño base (heredado de Entidad)
        this.nombrePersonaje = nombrePersonaje;
        this.nivelEstamina = 100;
        this.arma = arma;
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

    // Implementación concreta del método abstracto atacar() de Entidad.
    // Cada subclase de Entidad decide CÓMO ataca; Personaje usa su Arma.
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
       linterna.recargarLinterna(cantidad);
    }

    // Punto de entrada genérico para interactuar con CUALQUIER cosa que
    // implemente Interactuable (Item, Acertijo, etc). Personaje no pregunta
    // "¿qué sos?" con instanceof: delega en el objeto y que cada uno decida
    // su propio comportamiento (polimorfismo).

    public void interactuarCon(Interactuable objeto) {
        objeto.interactuar(this);
    }

    // Usa un item que YA está en el inventario (no uno que está tirado en
    // el mapa: eso lo maneja el Controlador con agregarAlInventario más abajo).
    // item.interactuar(this) dispara el efecto propio de cada item
    // (curar, cargar la linterna, etc) sin que Personaje sepa de qué tipo es.

    public void usarItem(Item item) {
        if (item == null || !inventario.getItems().contains(item)) {
            System.out.println("No tenés ese item en el inventario.");
            return;
        }
        item.interactuar(this);   // polimorfismo: cada item decide qué hacer al usarse
        inventario.quitarItem(item);
    }

    // Se llama desde el código de colisión/recolección (Controlador) cuando
    // el personaje toca un item en el mapa. Solo lo guarda: NO dispara
    // interactuar(), así el item queda disponible para usarse después
    // con usarItem() o para resolver un acertijo con intentarResolverAcertijo().

    public void agregarAlInventario(Item item){
        inventario.agregarItem(item);
    }

    // Puente entre el inventario y Acertijo.validarRespuesta(String).
    // Acertijo no conoce el Inventario, e Item no conoce a Acertijo:
    // Personaje es quien tiene acceso a los dos, así que arma la conexión acá.
    //
    // Solo saca el item del inventario si la respuesta es CORRECTA. Si el
    // jugador prueba con el objeto equivocado, lo conserva y puede intentar
    // con otro (no lo "pierde" por errar).
    //
    // No usa usarItem() por dentro a propósito: usarItem() dispara
    // interactuar() (el efecto propio del item, como curar), y acá no
    // queremos ningún efecto — solo comparar el nombre del item contra
    // la respuesta esperada del acertijo.
    
    public boolean intentarResolverAcertijo(Acertijo acertijo, Item item) {
        if (item == null || !inventario.getItems().contains(item)) {
            System.out.println("No tenés ese item en el inventario.");
            return false;
        }
        boolean resuelto = acertijo.validarRespuesta(item.getNombre());
        if (resuelto) {
            inventario.quitarItem(item); // se "entrega" el objeto al resolver
        }
        return resuelto;
    }
}