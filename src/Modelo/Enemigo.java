package Modelo;

import java.util.Random;
import java.awt.Rectangle;

public abstract class Enemigo extends EntidadCombatible {

    private static final int ANCHO_HITBOX = (int) (10 * ESCALA);
    private static final int ALTO_HITBOX = (int) (6 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (5 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (11 * ESCALA);

    private int idEnemigo;
    private int danioBase;
    private int radioDeteccion = 200;
    private boolean alertado = false;
    private int ticksEnfriamientoAtaque = 0;
    private static final int ENFRIAMIENTO_ATAQUE = 60;

    private int spawnX;
    private int spawnY;
    private static final int RADIO_PATRULLA = 60;
    private int dxPatrulla = 0;
    private int dyPatrulla = 0;
    private int ticksHastaCambiarDireccion = 0;
    private final Random random = new Random();
    private boolean moviendose = false;
    private int ticksSinAvanzar = 0;
    private static final int TICKS_ANTES_DE_ESQUIVAR = 20;
    private int ticksEsquivando = 0;
    private static final int DURACION_ESQUIVE = 25;
    private int dxEsquive = 0;
    private int dyEsquive = 0;

    public Enemigo(int puntosVida, int danioBase, int idEnemigo,
                   int columna, int fila, String rutaSprites) {
        super(puntosVida);
        this.danioBase = danioBase;
        this.idEnemigo = idEnemigo;
        this.rutaSprites = rutaSprites;
        colocarEnTile(columna, fila);
    }

    public int getIdEnemigo() {
        return this.idEnemigo;
    }

    public void setIdEnemigo(int idEnemigo) {
        this.idEnemigo = idEnemigo;
    }

    public int getDanioBase() {
        return this.danioBase;
    }

    @Override
    public boolean estaMoviendose() {
        return this.moviendose;
    }

    @Override
    public Rectangle getHitbox() {
        return construirHitbox(getPosicionX(), getPosicionY());
    }

    public Rectangle getHitboxEnPosicion(int x, int y) {
        return construirHitbox(x, y);
    }

    private Rectangle construirHitbox(int x, int y) {
        return new Rectangle(x + OFFSET_X_HITBOX, y + OFFSET_Y_HITBOX, ANCHO_HITBOX, ALTO_HITBOX);
    }

    @Override
    public void atacar(EntidadCombatible objetivo) {
        if (ticksEnfriamientoAtaque > 0) return;

        if (objetivo != null) {
            System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
            objetivo.recibirDanio(this.danioBase);
            ticksEnfriamientoAtaque = ENFRIAMIENTO_ATAQUE;
        }
    }

    public void moverHaciaJugador(Personaje jugador, MapaColision mapa) {
        if (ticksEsquivando > 0) {
            moverConLimites(dxEsquive, dyEsquive, mapa);
            ticksEsquivando--;
            this.moviendose = true;
            return;
        }

        int deltaX = 1;
        int deltaY = 0;

        if (jugador.getPosicionX() > this.getPosicionX()) {
            deltaX = 1;
            direccion = Direccion.DERECHA;
        } else if (jugador.getPosicionX() < this.getPosicionX()) {
            deltaX = -1;
            direccion = Direccion.IZQUIERDA;
        }

        if (jugador.getPosicionY() > this.getPosicionY()) {
            deltaY = 1;
            direccion = Direccion.ABAJO;
        } else if (jugador.getPosicionY() < this.getPosicionY()) {
            deltaY = -1;
            direccion = Direccion.ARRIBA;
        }

        int xAntesDeMover = getPosicionX();
        int yAntesDeMover = getPosicionY();

        moverConLimites(deltaX, deltaY, mapa);

        boolean logroAvanzar = (getPosicionX() != xAntesDeMover || getPosicionY() != yAntesDeMover);

        if (logroAvanzar) {
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
        int[][] direcciones = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int i = direcciones.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = direcciones[i];
            direcciones[i] = direcciones[j];
            direcciones[j] = temp;
        }

        for (int[] dir : direcciones) {
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

            if (random.nextInt(5) != 0) { // 1 de cada 5 veces se queda quieto
                Direccion nueva = Direccion.aleatoria(random);
                direccion = nueva;
                dxPatrulla = nueva.getDx();
                dyPatrulla = nueva.getDy();
            }

            ticksHastaCambiarDireccion = 40 + random.nextInt(60);
        }

        int futuroX = getPosicionX() + dxPatrulla;
        int futuroY = getPosicionY() + dyPatrulla;
        boolean seAlejaDeMas = Math.hypot(futuroX - spawnX, futuroY - spawnY) > RADIO_PATRULLA;

        if (seAlejaDeMas) {
            dxPatrulla = 0;
            dyPatrulla = 0;
        } else if (dxPatrulla != 0 || dyPatrulla != 0) {
            moverConLimites(dxPatrulla, dyPatrulla, mapa);
        }

        this.moviendose = (dxPatrulla != 0 || dyPatrulla != 0);
    }
//cada tipo de enemigo tiene su propia forma de detectar al jugador y de decidir si puede atacarlo, por eso son metodos abstractos

    public abstract boolean detectaAlJugador(Personaje jugador);

    protected abstract boolean puedeAtacar (Personaje jugador);

    protected int getRadioDeteccion(){
        return radioDeteccion;
    }
    protected boolean jugadorDentroDeRadio(Personaje jugador, int radio) {
        double dx = jugador.getPosicionX() - this.getPosicionX();
        double dy = jugador.getPosicionY() - this.getPosicionY();
        return Math.hypot(dx, dy) <= radio;
    }

    //los pasos variables son abstractos, metodoo

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
    public void actualizar(Personaje jugador, MapaColision mapa) {
        actualizarComportamiento(jugador, mapa);
        if (puedeAtacar(jugador) && jugador.getHitbox().intersects(getHitbox())) {
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