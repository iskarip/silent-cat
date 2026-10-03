package Modelo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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

public class Personaje extends EntidadCombatible {

    // -- ATRIBUTOS --

    private String nombrePersonaje;

    private double multiplicadorVelocidad = 1.0;
    private int ticksLentitud = 0;
    private boolean moviendose = false;
    private boolean corriendo = false;
    private boolean agotado = false;
    private double restoX = 0;
    private double restoY = 0;

    // Para notificar cambios de vida al Controlador (que a su vez los pasa a la Vista)

    //public static final String PROP_VIDA = "puntosVida";
    //private final PropertyChangeSupport soporteCambios = new PropertyChangeSupport(this);

    private double nivelEstamina;

    private Arma arma;
    private Linterna linterna;
    private Inventario inventario;

    // -- LISTA DE OBSERVADORES --
    private final List<ObservadorPersonaje> observadores = new ArrayList<>();

// -- MEDIDAS DEL HITBOX --
    private static final int ANCHO_HITBOX = (int) (6 * ESCALA);
    private static final int ALTO_HITBOX = (int) (2 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (7 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (15 * ESCALA);
    private static final int ALCANCE_ATAQUE = (int) (18 * ESCALA);
    private static final int VELOCIDAD_BASE = 2;
    private static final double FACTOR_DIAGONAL = 0.7071;
    private static final double FACTOR_CORRER = 1.6;          // corriendo va 60% más rápido
    private static final double ESTAMINA_MAXIMA = 100;
    private static final double GASTO_CORRER = 0.5;           
    private static final double RECUPERACION = 0.2;           
    private static final double UMBRAL_RECUPERACION = 25;     // agotada, necesita 25 para volver a correr

// -- CONSTRUCTOR --

    // Recibe arma y linterna ya construidas (no las crea el personaje),
    // así queda desacoplado de cómo se arman esos objetos.
    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
        super(100); // Asignación de Vida y daño base (heredado de Entidad)
        this.nombrePersonaje = nombrePersonaje;
        this.nivelEstamina = ESTAMINA_MAXIMA;
        this.arma = arma;
        this.linterna = linterna;
        this.inventario = new Inventario();
    }

    // -- GESTION DE OBSERVADORES --

    public void agregarObservador(ObservadorPersonaje observador) {
        if (observador != null && !this.observadores.contains(observador)) {
            this.observadores.add(observador);
        }
    }

    public void removerObservador(ObservadorPersonaje observador) {
        this.observadores.remove(observador);
    }

    public void notificarCambioVida() {
        for (ObservadorPersonaje obs : this.observadores) {
            obs.vidaCambio(getPuntosVida(), 100);
            if (getPuntosVida() <= 0) {
                obs.personajeMurio();
            }
        }
    }

    @Override
    public void setPuntosVida (int puntosVida) {
        super.setPuntosVida(puntosVida);
        this.notificarCambioVida();
    }

    // -- GETTERS Y SETTERS --

    public String getNombrePersonaje(){
        return nombrePersonaje;
    }

    public int getNivelEstamina(){
        return (int) nivelEstamina;
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
    public Rectangle getHitbox() {
        return construirHitbox(getPosicionX(), getPosicionY());
    }

    private Rectangle construirHitbox(int x, int y) {
        return new Rectangle(x + OFFSET_X_HITBOX, y + OFFSET_Y_HITBOX, ANCHO_HITBOX, ALTO_HITBOX);
    }

    public Rectangle getHitboxAtaque() {
        Rectangle base = getHitbox();
        switch (getDireccion()) {
            case ARRIBA:    return new Rectangle(base.x, base.y - ALCANCE_ATAQUE, base.width, ALCANCE_ATAQUE);
            case ABAJO:     return new Rectangle(base.x, base.y + base.height, base.width, ALCANCE_ATAQUE);
            case IZQUIERDA: return new Rectangle(base.x - ALCANCE_ATAQUE, base.y, ALCANCE_ATAQUE, base.height);
            case DERECHA:   
            default:        return new Rectangle(base.x + base.width, base.y, ALCANCE_ATAQUE, base.height);
        }
    }

    // pregunta al arma si esta lista, se llama UNA vez por golpe
    public boolean intentarAtacar() {
        return arma.usarArma();
    }

    public void atacar(EntidadCombatible objetivo) {
        objetivo.recibirDanio(arma.calcularDanio());
    }

    public void atacar(Entidad objetivo) {
        objetivo.recibirDanio(arma.calcularDanio());
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

    public void aplicarLentitud(int ticks) {
    this.multiplicadorVelocidad = 0.4;
    this.ticksLentitud = ticks;
    }

    public void actualizarEfectos() {      
        if (ticksLentitud > 0) {
            ticksLentitud--;
            if (ticksLentitud == 0) multiplicadorVelocidad = 1.0;
        }
        arma.actualizar();
    }

    public double getMultiplicadorVelocidad(){
        return multiplicadorVelocidad;
    }

    // el personaje decide cuantos pixeles avanza
    public void mover(int direccionX, int direccionY, boolean quiereCorrer, MapaColision mapa) {
        boolean hayMovimiento = (direccionX != 0 || direccionY != 0);                               
        corriendo = quiereCorrer && hayMovimiento && !agotado && ticksLentitud == 0;                 

        double velocidad = VELOCIDAD_BASE * multiplicadorVelocidad;
        if (corriendo) {                                                                             
            velocidad *= FACTOR_CORRER;                                                          
        }
        double pasoX = direccionX * velocidad;
        double pasoY = direccionY * velocidad;

        if (direccionX != 0 && direccionY != 0) {
            pasoX *= FACTOR_DIAGONAL;
            pasoY *= FACTOR_DIAGONAL;
        }

        restoX += pasoX;
        restoY += pasoY;

        int deltaX = (int) restoX;
        int deltaY = (int) restoY;

        restoX -= deltaX;
        restoY -= deltaY;

        if (direccionX == 0) restoX = 0;
        if (direccionY == 0) restoY = 0;

        if (direccionX != 0) {
            setDireccion(direccionX > 0 ? Direccion.DERECHA : Direccion.IZQUIERDA);
        } else if (direccionY != 0) {
            setDireccion(direccionY > 0 ? Direccion.ABAJO : Direccion.ARRIBA);
        }

        moviendose = hayMovimiento;      
        actualizarEstamina();            
        moverConLimites(deltaX, deltaY, mapa);
    }

    // gasta estamina si esta corriendo y la recupera si no
    private void actualizarEstamina() {
        if (corriendo) {
            nivelEstamina = Math.max(0, nivelEstamina - GASTO_CORRER);
            if (nivelEstamina == 0) {
                agotado = true;
            }
        } else {
            nivelEstamina = Math.min(ESTAMINA_MAXIMA, nivelEstamina + RECUPERACION);
            if (agotado && nivelEstamina >= UMBRAL_RECUPERACION) {
                agotado = false;
            }
        }
    }

    @Override
    public boolean estaMoviendose() {
        return moviendose;
    }

    public boolean estaCorriendo() {
        return corriendo;
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