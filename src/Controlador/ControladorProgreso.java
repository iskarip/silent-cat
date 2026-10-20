package Controlador;

import Modelo.Partida;
import Modelo.Personaje;
import Modelo.Progreso;
import Modelo.ProgresoDAO;

public class ControladorProgreso {

    private final Partida partida;
    private final ProgresoDAO progresoDAO;

    public ControladorProgreso(Partida partida, ProgresoDAO progresoDAO) {
        this.partida = partida;
        this.progresoDAO = progresoDAO;
    }
// Paso 1 (hilo de eventos): lee la partida y saca una "foto" de como esta ahora.
// Progreso es inmutable, asi que despues se la puede pasar a otro hilo sin riesgo.
// Devuelve null si no hay nada que guardar (no se guarda una partida perdida).
    public Progreso tomarInstantanea() {
        Personaje personaje = partida.getPersonaje();
        if (personaje == null || !personaje.estaVivo()) return null;
        return partida.crearProgreso();
    }

//Paso 2 (puede correr en segundo plano): NO toca la partida, solo escribe en la BD
    public boolean guardar(Progreso instantanea) {
        return instantanea != null && progresoDAO.guardar(instantanea);
    }


    public boolean hayProgreso() {
        return progresoDAO.hayProgreso();
    }

    public Progreso cargar() {
        return progresoDAO.cargar();
    }
    
}
