package Modelo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Personaje extends EntidadCombatible {

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
    private final Arma arma;
    private final Linterna linterna;
    private final Inventario inventario;

    private final List<ObservadorPersonaje> observadores = new ArrayList<>();

    private static final int ALCANCE_ATAQUE = (int) (18 * ESCALA);
    private static final int VELOCIDAD_BASE = 2;
    private static final double FACTOR_DIAGONAL = 0.7071;
    private static final double FACTOR_CORRER = 1.6;
    private static final double ESTAMINA_MAXIMA = 100;
    private static final double GASTO_CORRER = 0.5;
    private static final double RECUPERACION = 0.25;
    private static final double UMBRAL_RECUPERACION = 40;

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
        if (invulnerableRespawn) return;
        super.recibirDanio(cantidad);
    }

    // -- OBSERVER --

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

    // -- GETTERS Y ACCIONES --

    public String getNombrePersonaje() { return nombrePersonaje; }
    public int getNivelEstamina() { return (int) nivelEstamina; }
    public boolean estaAgotado() { return agotado; }
    public Arma getArma() { return arma; }
    public Linterna getLinterna() { return linterna; }
    public boolean linternaEncendida() { return linterna != null && linterna.getEncendido(); }
    public Inventario getInventario() { return inventario; }

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
        return arma != null && arma.usarArma();
    }

    @Override
    public void atacar(EntidadCombatible objetivo) {
        if (objetivo != null && arma != null) {
            objetivo.recibirDanio(arma.calcularDanio());
        }
    }

    public void usarLinterna() {
        if (linterna == null) return;
        if (linterna.getEncendido()) {
            linterna.apagarLinterna();
        } else {
            linterna.encenderLinterna();
        }
    }

    public boolean revisarBateria() { return linterna != null && linterna.bateriaBaja(); }
    public void recargarLinterna(int cantidad) { if (linterna != null) linterna.recargarLinterna(cantidad); }

    public void aplicarLentitud(int ticks) {
        this.multiplicadorVelocidad = 0.4;
        this.ticksLentitud = ticks;
    }

    public void actualizarEfectos() {
        if (ticksLentitud > 0) {
            ticksLentitud--;
            if (ticksLentitud == 0) multiplicadorVelocidad = 1.0;
        }
        if (arma != null) arma.actualizar();
    }

    public double getMultiplicadorVelocidad() { return multiplicadorVelocidad; }

    // -- MOVIMIENTO Y POLIMORFISMO ESTÁTICO  --

    public void mover(int direccionX, int direccionY, boolean quiereCorrer, MapaColision mapa) {
        boolean hayMovimiento = (direccionX != 0 || direccionY != 0);
        if (hayMovimiento) this.invulnerableRespawn = false;

        corriendo = quiereCorrer && hayMovimiento && !agotado && ticksLentitud == 0;
        double velocidad = VELOCIDAD_BASE * multiplicadorVelocidad;
        if (corriendo) velocidad *= FACTOR_CORRER;

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
            if (nivelEstamina == 0) agotado = true;
        } else {
            nivelEstamina = Math.min(ESTAMINA_MAXIMA, nivelEstamina + RECUPERACION);
            if (agotado && nivelEstamina >= UMBRAL_RECUPERACION) agotado = false;
        }
    }

    @Override
    public boolean estaMoviendose() { return moviendose; }
    public boolean estaCorriendo() { return corriendo; }

    public void interactuarCon(Interactuable objeto) {
        if (objeto != null) objeto.interactuar(this);
    }

    public void usarItem(Item item) {
        if (item == null || !inventario.getItems().contains(item)) return;
        item.interactuar(this);
        inventario.quitarItem(item);
    }

    public void agregarAlInventario(Item item) {
        inventario.agregarItem(item);
    }
}