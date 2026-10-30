package Modelo;

import java.util.Random;
import java.awt.Rectangle;

public abstract class Enemigo extends EntidadCombatible {

    private static final int MARGEN_CONTACTO = 4;
    private static final int ENFRIAMIENTO_ATAQUE_DEFAULT = 60;
    private static final int DURACION_ANIMACION_ATAQUE = 30;
    private static final int RADIO_PATRULLA = 60;
    private static final int TICKS_ANTES_DE_ESQUIVAR = 20;
    private static final int DURACION_ESQUIVE = 25;

    private int idEnemigo;
    private int danioBase;
    private int radioDeteccion = 100;
    private boolean alertado = false;

    private int ticksEnfriamientoAtaque = 0;
    private int ticksAtacando = 0;
    private boolean golpePendiente = false;

    private int spawnX;
    private int spawnY;
    private int dxPatrulla = 0;
    private int dyPatrulla = 0;
    private int ticksHastaCambiarDireccion = 0;

    private boolean moviendose = false;
    private int ticksSinAvanzar = 0;
    private int ticksEsquivando = 0;
    private int dxEsquive = 0;
    private int dyEsquive = 0;

    private final Random random = new Random();

    public Enemigo(int puntosVida, int danioBase, int idEnemigo,
                   int columna, int fila, String rutaSprites) {
        super(puntosVida);
        this.danioBase = danioBase;
        this.idEnemigo = idEnemigo;
        this.rutaSprites = rutaSprites;
        colocarEnTile(columna, fila);
    }

    public int getIdEnemigo() { return this.idEnemigo; }
    public void setIdEnemigo(int idEnemigo) { this.idEnemigo = idEnemigo; }
    public int getDanioBase() { return this.danioBase; }

    @Override
    public boolean estaMoviendose() { return this.moviendose; }

    @Override
    public int getNivelAmenaza() { return alertado ? 2 : 0; }

    @Override
    public boolean estaAtacando() { return ticksAtacando > 0; }

    @Override
    public double getProgresoAtaque() {
        if (ticksAtacando <= 0) return 0.0;
        return 1.0 - (double) ticksAtacando / getDuracionAnimacionAtaque();
    }

    // Métodos plantilla / ganchos para especialización en subclases
    public abstract boolean detectaAlJugador(Personaje jugador);
    protected abstract boolean puedeAtacar(Personaje jugador);

    protected int getEnfriamientoAtaque() { return ENFRIAMIENTO_ATAQUE_DEFAULT; }
    protected int getPreparacionAtaque() { return 0; }
    protected int getDuracionAnimacionAtaque() {
        return Math.max(DURACION_ANIMACION_ATAQUE, 2 * getPreparacionAtaque());
    }
    protected int getRadioDeteccion() { return radioDeteccion; }

    protected boolean jugadorDentroDeRadio(Personaje jugador, int radio) {
        double dx = jugador.getPosicionX() - this.getPosicionX();
        double dy = jugador.getPosicionY() - this.getPosicionY();
        return Math.hypot(dx, dy) <= radio;
    }

    public void moverHaciaJugador(Personaje jugador, MapaColision mapa) {
        if (ticksEsquivando > 0) {
            moverConLimites(dxEsquive, dyEsquive, mapa);
            ticksEsquivando--;
            this.moviendose = true;
            return;
        }

        int deltaX = 0;
        int deltaY = 0;

        if (jugador.getPosicionX() > this.getPosicionX()) {
            deltaX = 1;
            setDireccion(Direccion.DERECHA);
        } else if (jugador.getPosicionX() < this.getPosicionX()) {
            deltaX = -1;
            setDireccion(Direccion.IZQUIERDA);
        }

        if (jugador.getPosicionY() > this.getPosicionY()) {
            deltaY = 1;
            setDireccion(Direccion.ABAJO);
        } else if (jugador.getPosicionY() < this.getPosicionY()) {
            deltaY = -1;
            setDireccion(Direccion.ARRIBA);
        }

        int xAntes = getPosicionX();
        int yAntes = getPosicionY();
        moverConLimites(deltaX, deltaY, mapa);

        boolean avanzo = (getPosicionX() != xAntes || getPosicionY() != yAntes);
        if (avanzo) {
            ticksSinAvanzar = 0;
        } else {
            ticksSinAvanzar++;
            if (ticksSinAvanzar > TICKS_ANTES_DE_ESQUIVAR) {
                elegirDireccionDeEsquive(mapa);
                ticksSinAvanzar = 0;
            }
        }
        this.moviendose = (deltaX != 0 || deltaY != 0);
    }

    private void elegirDireccionDeEsquive(MapaColision mapa) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = dirs[i];
            dirs[i] = dirs[j];
            dirs[j] = temp;
        }
        for (int[] dir : dirs) {
            int intentoX = getPosicionX() + dir[0];
            int intentoY = getPosicionY() + dir[1];
            Rectangle hb = getHitboxEnPosicion(intentoX, intentoY);
            if (mapa == null || mapa.esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
                dxEsquive = dir[0];
                dyEsquive = dir[1];
                ticksEsquivando = DURACION_ESQUIVE;
                return;
            }
        }
    }

    public void patrullar(MapaColision mapa) {
        ticksHastaCambiarDireccion--;
        if (ticksHastaCambiarDireccion <= 0) {
            dxPatrulla = 0;
            dyPatrulla = 0;
            if (random.nextInt(5) != 0) {
                Direccion nueva = Direccion.aleatoria(random);
                setDireccion(nueva);
                dxPatrulla = nueva.getDx();
                dyPatrulla = nueva.getDy();
            }
            ticksHastaCambiarDireccion = 40 + random.nextInt(60);
        }

        int futuroX = getPosicionX() + dxPatrulla;
        int futuroY = getPosicionY() + dyPatrulla;
        if (Math.hypot(futuroX - spawnX, futuroY - spawnY) > RADIO_PATRULLA) {
            dxPatrulla = 0;
            dyPatrulla = 0;
        } else if (dxPatrulla != 0 || dyPatrulla != 0) {
            moverConLimites(dxPatrulla, dyPatrulla, mapa);
        }
        this.moviendose = (dxPatrulla != 0 || dyPatrulla != 0);
    }

    public void actualizarComportamiento(Personaje jugador, MapaColision mapa) {
        if (ticksEnfriamientoAtaque > 0) {
            ticksEnfriamientoAtaque--;
        }

        if (detectaAlJugador(jugador)) {
            alertado = true;
            moverHaciaJugador(jugador, mapa);
        } else if (alertado) {
            spawnX = getPosicionX();
            spawnY = getPosicionY();
            alertado = false;
            ticksHastaCambiarDireccion = 0;
            patrullar(mapa);
        } else {
            patrullar(mapa);
        }
    }

    @Override
    public void atacar(EntidadCombatible objetivo) {
        if (ticksEnfriamientoAtaque > 0) return;
        if (objetivo != null) {
            objetivo.recibirDanio(this.danioBase);
            ticksEnfriamientoAtaque = getEnfriamientoAtaque();
            ticksAtacando = getDuracionAnimacionAtaque();
        }
    }

    @Override
    public void actualizar(Personaje jugador, MapaColision mapa) {
        if (ticksAtacando > 0) {
            ticksAtacando--;
        }
        actualizarComportamiento(jugador, mapa);

        Rectangle alcance = getHitbox();
        alcance.grow(MARGEN_CONTACTO, MARGEN_CONTACTO);
        boolean enContacto = puedeAtacar(jugador) && jugador.getHitbox().intersects(alcance);

        if (!enContacto) {
            golpePendiente = false;
        }

        if (enContacto && ticksAtacando == 0 && ticksEnfriamientoAtaque <= getPreparacionAtaque()) {
            ticksAtacando = getDuracionAnimacionAtaque();
            golpePendiente = true;
        }

        boolean momentoGolpe = (getDuracionAnimacionAtaque() - ticksAtacando >= getPreparacionAtaque());
        if (golpePendiente && momentoGolpe) {
            golpePendiente = false;
            atacar(jugador);
        }
    }

    @Override
    public void colocarEnTile(int columna, int fila) {
        super.colocarEnTile(columna, fila);
        this.spawnX = getPosicionX();
        this.spawnY = getPosicionY();
    }
}