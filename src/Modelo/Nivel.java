package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class Nivel extends EspacioBase {

//--ATRIBUTOS--

    private int numeroNivel;
    private Acertijo acertijoNivel;
    private List<Habitacion> habitaciones; // cada nivel puede tener varias habitaciones, pero por ahora solo vamos a usar una
    private boolean nivelSuperado;
    private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado
    private int accesoSiguienteNivelX, accesoSiguienteNivelY;
    private int acertijoColumna, acertijoFila;// posicion donde se activa el acertijo del nivel

    private int checkpointX;
    private int checkpointY;

    private List<ZonaProximidad> zonas = new ArrayList<>();

    private static final int RADIO_ACTIVACION_ACERTIJO = 50;


//--CONSTRUCTOR--

    public Nivel(int numeroNivel, Acertijo acertijoNivel, String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY) { //aca va a marcar error hasta que este definida la clase acertijo
        super(rutaGrid, rutaImagenFondo, posicionInicialX, posicionInicialY);
        this.numeroNivel = numeroNivel;
        this.acertijoNivel = acertijoNivel;
        this.nivelSuperado = false;
        this.checkpoint = false; //arranca desactivado por defecto
        this.habitaciones = new ArrayList<>();
        this.checkpointX = posicionInicialX * MapaColision.TILE;
        this.checkpointY = posicionInicialY * MapaColision.TILE;
    }

    //--SET Y GET--

    public boolean getNivelSuperado() {
        return this.nivelSuperado;
    }

    public void setNivelSuperado(boolean nivelSuperado) {
        this.nivelSuperado = nivelSuperado;
    }

    public boolean getCheckpoint() {
        return this.checkpoint;
    }

    public int getNumeroNivel() {
        return this.numeroNivel;
    }

    public Acertijo getAcertijo() {
        return this.acertijoNivel;
    }

    public List<Habitacion> getHabitaciones() {
        return this.habitaciones;
    }

    public int getAccesoSiguienteNivelX() {
        return accesoSiguienteNivelX;
    }

    public void setAccesoSiguienteNivelX(int accesoSiguienteNivelX) {
        this.accesoSiguienteNivelX = accesoSiguienteNivelX;
    }

    public int getAccesoSiguienteNivelY() {
        return accesoSiguienteNivelY;
    }

    public void setAccesoSiguienteNivelY(int accesoSiguienteNivelY) {
        this.accesoSiguienteNivelY = accesoSiguienteNivelY;
    }

    public void guardarCheckPoint (int pixelX, int pixelY) {
        this.checkpoint = true;
        this.checkpointX = pixelX;
        this.checkpointY = pixelY;
    }

    public int getCheckpointX() {
        return checkpointX;
    }

    public int getCheckpointY() {
        return checkpointY;
    }

    // LOGICA PARA LOS ACERTIJOS

    public void setAcertijoEnTile(int columna, int fila) {
        this.acertijoColumna = columna;
        this.acertijoFila = fila;
    }

    public int getAcertijoColumna() {
        return acertijoColumna;
    }

    public int getAcertijoFila() {
        return acertijoFila;
    }

            // Devuelven el CENTRO del tile en píxeles, así ControladorAcertijo
            // sigue llamando a getAcertijoX()/getAcertijoY() sin cambios.

    public int getAcertijoX() {
        return acertijoColumna * MapaColision.TILE + MapaColision.TILE / 2;
    }

    public int getAcertijoY() {
        return acertijoFila * MapaColision.TILE + MapaColision.TILE / 2;
    }

//--METODOS--
    
    // true si el acertijo existe, sigue sin resolverse y el personaje esta en su zona
    public boolean acertijoAlAlcance(Personaje personaje) {
        if (acertijoNivel == null || acertijoNivel.getResuelto()) {
            return false;
        }
        double distancia = Math.hypot(personaje.getPosicionX() - getAcertijoX(),
                                      personaje.getPosicionY() - getAcertijoY());
        return distancia <= RADIO_ACTIVACION_ACERTIJO;
    }

    public void guardarCheckpoint() {
        this.checkpoint = true;
        System.out.println("Checkpoint activado en el nivel " + this.numeroNivel + "!!!");
    }

    public boolean verificarSiCompleto() {
        boolean acertijoOk = acertijoNivel.getResuelto();
        return acertijoOk;
    }

    public void agregarHabitacion(Habitacion h) {
        if (h != null) {
            this.habitaciones.add(h);
        }
    }

    public void agregarZona(ZonaProximidad zona) {
        if (zona != null) zonas.add(zona);
    }

    // aplica el ataque del personaje a todas las entidades vivas que quedan dentro del golpe
    public void resolverAtaque(Personaje atacante) {
        if (listaEntidades == null) return;

        Rectangle hitboxGolpe = atacante.getHitboxAtaque();

        for (Entidad e : listaEntidades) {
            if (e.estaVivo() && hitboxGolpe.intersects(e.getHitbox())) {
                atacante.atacar(e);
            }
        }
    }

    public List<ZonaProximidad> getZonas() {
        return zonas;
    }

}
