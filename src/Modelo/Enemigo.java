package Modelo;
import java.util.Random;
import java.awt.Rectangle;

public class Enemigo extends Entidad {

//como vamos a tener diferentes enemigos definimos usar id enemigo
//para diferenciar lo que hace cada uno.

//--ATRIBUTOS--

    private static final int ANCHO_HITBOX = (int) (10 * ESCALA);
    private static final int ALTO_HITBOX = (int) (6 * ESCALA);
    private static final int OFFSET_X_HITBOX = (int) (5 * ESCALA);
    private static final int OFFSET_Y_HITBOX = (int) (11 * ESCALA);

    private int idEnemigo;
    private int danioBase;
    private int radioDeteccion = 200; // distancia en pixeles a la que "nota" al jugador
    private boolean alertado = false; // si ya vio al jugador o no
    private int ticksEnfriamientoAtaque = 0; // cuenta atrás hasta que pueda volver a atacar
    private static final int ENFRIAMIENTO_ATAQUE = 60; // ticks de espera entre golpe y golpe


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
    private static final int DURACION_ESQUIVE = 25; // cuánto sostiene la dirección de esquive antes de volver a perseguir directo
    private int dxEsquive = 0;
    private int dyEsquive = 0;

//--CONSTRUCTOR--

    public Enemigo(int puntosVida, int danioBase, int idEnemigo, int xInicial, int yInicial) {
        super(puntosVida);
        this.danioBase = danioBase;
        this.idEnemigo = idEnemigo;
        setPosicionX(xInicial);
        setPosicionY(yInicial);
        this.spawnX = xInicial;
        this.spawnY = yInicial;
    }

//--GET Y SET--

    public int getIdEnemigo() {
        return this.idEnemigo;
    }

    public void setIdEnemigo(int idEnemigo) {
        this.idEnemigo = idEnemigo;
    }

    public int getDanioBase() {
        return this.danioBase;
    }

    public boolean estaMoviendose() {
        return this.moviendose;
    }

//uso de override para los metodos heredados, atacar y recibirDanio

    //sacar este println cuando la Vista (Swing) muestre el ataque visualmente
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
    public void atacar(Entidad objetivo) {
        if (ticksEnfriamientoAtaque > 0) return; // en cooldown, no puede golpear de nuevo

        if (objetivo != null) {
            System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
            objetivo.recibirDanio(this.danioBase);
            ticksEnfriamientoAtaque = ENFRIAMIENTO_ATAQUE;
        }
    }

//metodo propio del enemigo (mover, patrullar)

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

    private void moverConLimites(int deltaX, int deltaY, MapaColision mapa) {
        if (deltaX == 0 && deltaY == 0) return;
        
        // avance en X
        if (deltaX != 0) {
            int intentoX = getPosicionX() + deltaX;
            Rectangle hbX = getHitboxEnPosicion(intentoX, getPosicionY());
            if (mapa == null || mapa.esRectanguloValido(hbX.x, hbX.y, hbX.width, hbX.height)) {
                mover(deltaX, 0);
            }
        }

        // avance en Y
        if (deltaY != 0) {
            int intentoY = getPosicionY() + deltaY;
            Rectangle hbY = getHitboxEnPosicion(getPosicionX(), intentoY);
            if (mapa == null || mapa.esRectanguloValido(hbY.x, hbY.y, hbY.width, hbY.height)) {
                mover(0, deltaY);
            }
        }
    }


    public void patrullar(MapaColision mapa) {
        ticksHastaCambiarDireccion--;
        if (ticksHastaCambiarDireccion <= 0) {
            int opcion = random.nextInt(5); // 0 = quieto, 1-4 = una dirección

            switch (opcion) {
                case 1:
                    dxPatrulla = 1;
                    dyPatrulla = 0;
                    direccion = Direccion.DERECHA;
                    break;
                case 2:
                    dxPatrulla = -1;
                    dyPatrulla = 0;
                    direccion = Direccion.IZQUIERDA;
                    break;
                case 3:
                    dxPatrulla = 0;
                    dyPatrulla = 1;
                    direccion = Direccion.ABAJO;
                    break;
                case 4:
                    dxPatrulla = 0;
                    dyPatrulla = -1;
                    direccion = Direccion.ARRIBA;
                    break;
                default:
                    dxPatrulla = 0;
                    dyPatrulla = 0;
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

    // metodo que decide si se detecta al jugador
    public boolean detectaAlJugador(Personaje jugador, boolean linternaEncendida) {
        double dx = jugador.getPosicionX() - this.getPosicionX();
        double dy = jugador.getPosicionY() - this.getPosicionY();
        double distancia = Math.hypot(dx, dy);

        int radioEfectivo = linternaEncendida ? radioDeteccion + 40 : radioDeteccion;

        return distancia <= radioEfectivo;
    }

    public void actualizarComportamiento(Personaje jugador, boolean linternaEncendida, MapaColision mapa) {
        if (ticksEnfriamientoAtaque > 0) {
            ticksEnfriamientoAtaque--;
        }

        if (detectaAlJugador(jugador, linternaEncendida)) {
            alertado = true;
            moverHaciaJugador(jugador, mapa);
        } else if (alertado) { // perdió de vista al jugador, pero sigue en alerta un rato antes de volver a patrullar
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
    public void colocarEnTile(int columna, int fila) {
        super.colocarEnTile(columna, fila);
        this.spawnX = getPosicionX();
        this.spawnY = getPosicionY();
    }
}




