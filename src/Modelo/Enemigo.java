package Modelo;
import java.util.Random;

public class Enemigo extends Entidad {

//como vamos a tener diferentes enemigos definimos usar id enemigo
//para diferenciar lo que hace cada uno.

//--ATRIBUTOS--

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
    public void atacar(Entidad objetivo) {
        if (ticksEnfriamientoAtaque > 0) return; // en cooldown, no puede golpear de nuevo

        if (objetivo != null) {
            System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
            objetivo.recibirDanio(this.danioBase);
            ticksEnfriamientoAtaque = ENFRIAMIENTO_ATAQUE;
        }
    }

//metodo propio del enemigo (mover, patrullar)

    public void moverHaciaJugador(Personaje jugador) {
        int deltaX = 0;
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

        mover(deltaX, deltaY);

        this.moviendose = (deltaX != 0 || deltaY != 0);

    }


    public void patrullar() {
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
            mover(dxPatrulla, dyPatrulla);
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

    public void actualizarComportamiento(Personaje jugador, boolean linternaEncendida) {
        if (ticksEnfriamientoAtaque > 0) {
            ticksEnfriamientoAtaque--;
        }

        if (detectaAlJugador(jugador, linternaEncendida)) {
            alertado = true;
            moverHaciaJugador(jugador);
        } else if (alertado) { // perdió de vista al jugador, pero sigue en alerta un rato antes de volver a patrullar
            spawnX = getPosicionX();
            spawnY = getPosicionY();
            alertado = false;
            ticksHastaCambiarDireccion = 0;
            patrullar();
        } else {
            patrullar();
        }
    }
}




