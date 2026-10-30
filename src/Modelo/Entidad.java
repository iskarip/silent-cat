package Modelo;

import java.awt.Rectangle;

public abstract class Entidad implements Posicionable {

    public static final double ESCALA = 2.5; 

    private int posicionX;
    private int posicionY;
    private Direccion direccion = Direccion.ABAJO;

    protected String rutaSprites;
    protected double escalaSprite = 1.0; 

    // -- GETTERS Y SETTERS BÁSICOS --

    public String getRutaSprites() { return rutaSprites; }
    public void setRutaSprites(String ruta) { this.rutaSprites = ruta; }
    public int getPosicionX() { return posicionX; }
    public void setPosicionX(int posicionX) { this.posicionX = posicionX; }
    public int getPosicionY() { return posicionY; }
    public void setPosicionY(int posicionY) { this.posicionY = posicionY; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public double getEscalaSprite() { return escalaSprite; }
    protected void setEscalaSprite(double escalaSprite) { this.escalaSprite = escalaSprite; }

    // -- COMPORTAMIENTO POLIMÓRFICO POR DEFECTO --

    public boolean estaVivo() { return true; }
    public boolean estaMoviendose() { return false; }
    public boolean estaAtacando() { return false; }
    public PoseEntidadCombatible getPostura() { return PoseEntidadCombatible.NORMAL; }
    public double getProgresoPostura() { return 0.0; }
    public double getProgresoAtaque() { return 0.0; }
    public int getNivelAmenaza() { return 0; }
    public boolean mostrarBarraVida() { return false; }
    public int getPorcentajeVida() { return 100; }
    public void recibirDanio(int cantidad) { }
    public void actualizar(Personaje jugador, MapaColision mapa) { }

    // -- MÉTODOS GANCHO (HOOKS) DE DIMENSIONES DEL HITBOX --
    // Medidas base estándar (humanoides). Las subclases las sobrescriben según su sprite.
    protected int getAnchoHitbox() { return (int) (6 * ESCALA); }
    protected int getAltoHitbox() { return (int) (2 * ESCALA); }
    protected int getOffsetXHitbox() { return (int) (7 * ESCALA); }
    protected int getOffsetYHitbox() { return (int) (15 * ESCALA); }

    /**
     * REUTILIZACIÓN DE CÓDIGO (Template Method):
     * La lógica de cálculo geométrico está escrita UNA SOLA VEZ aquí en Entidad.
     * Toma las medidas dinámicamente de la subclase correspondiente.
     */
    public Rectangle getHitbox() {
        return getHitboxEnPosicion(getPosicionX(), getPosicionY());
    }

    public Rectangle getHitboxEnPosicion(int x, int y) {
        return new Rectangle(x + getOffsetXHitbox(), y + getOffsetYHitbox(),
                             getAnchoHitbox(), getAltoHitbox());
    }

    // -- MOVIMIENTO Y COLISIÓN CENTRALIZADOS --

    public void mover(int deltaX, int deltaY) {
        this.posicionX += deltaX;
        this.posicionY += deltaY;
    }

    protected void moverConLimites(int deltaX, int deltaY, MapaColision mapa) {
        if (deltaX != 0) {
            Rectangle hb = getHitbox();
            hb.translate(deltaX, 0);
            if (mapa == null || mapa.esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                mover(deltaX, 0);
            }
        }
        if (deltaY != 0) {
            Rectangle hb = getHitbox();
            hb.translate(0, deltaY);
            if (mapa == null || mapa.esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                mover(0, deltaY);
            }
        }
    }

    @Override
    public void colocarEnTile(int columna, int fila) {
        Rectangle hb = getHitbox();
        int offsetX = hb.x - getPosicionX();
        int offsetY = hb.y - getPosicionY();
        int centroX = columna * MapaColision.TILE + MapaColision.TILE / 2;
        int centroY = fila * MapaColision.TILE + MapaColision.TILE / 2;
        setPosicionX(centroX - offsetX - hb.width / 2);
        setPosicionY(centroY - offsetY - hb.height / 2);
    }
}