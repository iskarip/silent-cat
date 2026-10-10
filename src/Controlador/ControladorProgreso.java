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

    public boolean guardar() {
        Personaje personaje = partida.getPersonaje();
        if (personaje == null || !personaje.estaVivo()) return false; // no se guarda una partida perdida
        return progresoDAO.guardar(partida.crearProgreso());
    }

    public boolean hayProgreso() {
        return progresoDAO.hayProgreso();
    }

    public Progreso cargar() {
        return progresoDAO.cargar();
    }
    
}
