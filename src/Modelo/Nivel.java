package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;

public class Nivel extends EspacioBase {

    private int numeroNivel;
    private List<Habitacion> habitaciones;
    private boolean nivelSuperado;
    private boolean checkpoint;
    private int accesoSiguienteNivelX, accesoSiguienteNivelY;
    private int acertijoColumna, acertijoFila;
    private List<ZonaProximidad> zonas = new ArrayList<>();

    private int checkpointX;
    private int checkpointY;

    public Nivel(int numeroNivel, Acertijo acertijoNivel, String rutaGrid, String rutaImagenFondo,
                 int posicionInicialX, int posicionInicialY) {
        // Se pasan exactamente los 5 parámetros requeridos por EspacioBase
        super(rutaGrid, rutaImagenFondo, posicionInicialX, posicionInicialY, acertijoNivel);
        this.numeroNivel = numeroNivel;
        this.nivelSuperado = false;
        this.checkpoint = false;
        this.habitaciones = new ArrayList<>();

        // Checkpoint inicial configurado en el spawn
        this.checkpointX = posicionInicialX * MapaColision.TILE;
        this.checkpointY = posicionInicialY * MapaColision.TILE;
    }

    // -- GETTERS Y SETTERS --

    public boolean getNivelSuperado() { return this.nivelSuperado; }
    public void setNivelSuperado(boolean nivelSuperado) { this.nivelSuperado = nivelSuperado; }
    public boolean getCheckpoint() { return this.checkpoint; }
    public int getNumeroNivel() { return this.numeroNivel; }

    public List<Habitacion> getHabitaciones() { return this.habitaciones; }

    public int getAccesoSiguienteNivelX() { return accesoSiguienteNivelX; }
    public void setAccesoSiguienteNivelX(int accesoSiguienteNivelX) { this.accesoSiguienteNivelX = accesoSiguienteNivelX; }

    public int getAccesoSiguienteNivelY() { return accesoSiguienteNivelY; }
    public void setAccesoSiguienteNivelY(int accesoSiguienteNivelY) { this.accesoSiguienteNivelY = accesoSiguienteNivelY; }

    public void guardarCheckpoint(int pixelX, int pixelY) {
        this.checkpoint = true;
        this.checkpointX = pixelX;
        this.checkpointY = pixelY;
        System.out.println("Checkpoint activado en: (" + pixelX + ", " + pixelY + ")");
    }

    public void guardarCheckpoint() {
        this.checkpoint = true;
        System.out.println("Checkpoint activado en el nivel " + this.numeroNivel + "!!!");
    }

    public boolean verificarSiCompleto() {
        return getAcertijo() != null && getAcertijo().getResuelto();
    }

    public void agregarHabitacion(Habitacion h) {
        if (h != null) this.habitaciones.add(h);
    }

    public void agregarZona(ZonaProximidad zona) {
        if (zona != null) zonas.add(zona);
    }

    public void resolverAtaque(Personaje atacante) {
        if (listaEntidades == null) return;
        Rectangle hitboxGolpe = atacante.getHitboxAtaque();
        for (Entidad e : listaEntidades) {
            if (e.estaVivo() && hitboxGolpe.intersects(e.getHitbox())) {
                atacante.atacar(e);
            }
        }
    }

    public List<ZonaProximidad> getZonas() { return zonas; }

    public int getCheckpointX() {
        return checkpointX;
    }

    public int getCheckpointY() {
        return checkpointY;
    }

    // -- LOGICA DE UBICACION DEL ACERTIJO --

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

    public int getAcertijoX() {
        return acertijoColumna * MapaColision.TILE + MapaColision.TILE / 2;
    }

    public int getAcertijoY() {
        return acertijoFila * MapaColision.TILE + MapaColision.TILE / 2;
    }

    public Acertijo getAcertijoAlAlcance(Personaje personaje) {
        if (acertijoAlAlcance(personaje)) return getAcertijo();
        for (Habitacion h : habitaciones) {
            if (h.acertijoAlAlcance(personaje)) {
                return h.getAcertijo();
            }
        }
        return null;
    }

    public Habitacion getHabitacionAlAlcance(Personaje personaje) {
        for (Habitacion h : habitaciones) {
            if (h.puertaAlAlcance(personaje)) return h;
        }
        return null;
    }
}
