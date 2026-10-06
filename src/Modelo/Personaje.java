package Modelo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa al jugador. Hereda de EntidadCombatible (vida, daño base, posición y el
 * método mover()), y le suma todo lo que es propio del personaje: arma,
 * linterna, inventario y la lógica de interactuar con el mundo (items, acertijos).
 *
 * IMPORTANTE: Personaje NO implementa Interactuable.
 * La relación con esa interfaz es de DEPENDENCIA (la usa como tipo de
 * parámetro en interactuarCon), no de REALIZACIÓN.
 */
public class Personaje extends EntidadCombatible {

    // -- ATRIBUTOS --

    private String nombrePersonaje;

    private double multiplicadorVelocidad = 1.0;
    private int ticksLentitud = 0;
    private boolean moviendose = false;
    private boolean invulnerableRespawn = false;
    private boolean corriendo = false;
    private boolean agotado = false;
    private double restoX = 0;
    private double restoY = 0;

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

    public Personaje(String nombrePersonaje, Arma arma, Linterna linterna) {
        super(100);
        this.nombrePersonaje = nombrePersonaje;
        this.nivelEstamina = ESTAMINA_MAXIMA;
        this.arma = arma;
        this.linterna = linterna;
        this.inventario = new Inventario();
    }

    public boolean isInvulnerableRespawn() {
        return invulnerableRespawn;
    }

    public void setInvulnerableRespawn(boolean invulnerableRespawn) {
        this.invulnerableRespawn = invulnerableRespawn;
    }

    public void revivirEn(int x, int y) {
        setPuntosVida(100);
        setPosicionX(x);
        setPosicionY(y);
        this.multiplicadorVelocidad = 1.0;
        this.ticksLentitud = 0;
        this.moviendose = false;
        this.invulnerableRespawn = true;
        setDireccion(Direccion.ABAJO);
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (invulnerableRespawn) {
            return;
        }
        super.recibirDanio(cantidad);
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
    public void setPuntosVida(int puntosVida) {
        super.setPuntosVida(puntosVida);
        this.notificarCambioVida();
    }

    // -- GETTERS Y SETTERS --

    public String getNombrePersonaje() {
        return nombrePersonaje;
    }

    public int getNivelEstamina() {
        return (int) nivelEstamina;
    }

    public Arma getArma() {
        return arma;
    }

    public Linterna getLinterna() {
        return linterna;
    }

    public Inventario getInventario() {
        return inventario;
    }

    // -- METODOS DE COMBATE Y HITBOX --

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

    public boolean intentarAtacar() {
        return arma.usarArma();
    }

    @Override
    public void atacar(EntidadCombatible objetivo) {
        if (objetivo != null) {
            objetivo.recibirDanio(arma.calcularDanio());
        }
    }

    public void atacar(Entidad objetivo) {
        if (objetivo != null) {
            objetivo.recibirDanio(arma.calcularDanio());
        }
    }

    // -- ACCIONES DE LINTERNA Y EFECTOS --

    public void usarLinterna() {
        if (linterna.getEncendido()) {
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

    public double getMultiplicadorVelocidad() {
        return multiplicadorVelocidad;
    }

    // -- MOVIMIENTO Y ESTAMINA --

    public void mover(int direccionX, int direccionY, boolean quiereCorrer, MapaColision mapa) {
        boolean hayMovimiento = (direccionX != 0 || direccionY != 0);

        if (hayMovimiento) {
            this.invulnerableRespawn = false;
        }

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

    public void mover(int direccionX, int direccionY, MapaColision mapa) {
        mover(direccionX, direccionY, false, mapa);
    }

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

    // -- INTERACCIONES E INVENTARIO --

    public void interactuarCon(Interactuable objeto) {
        objeto.interactuar(this);
    }

    public void usarItem(Item item) {
        if (item == null || !inventario.getItems().contains(item)) {
            System.out.println("No tenés ese item en el inventario.");
            return;
        }
        item.interactuar(this);
        inventario.quitarItem(item);
    }

    public void agregarAlInventario(Item item) {
        inventario.agregarItem(item);
    }

}
