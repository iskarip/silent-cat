package Persistencia;

import Modelo.Progreso;
import Modelo.ProgresoDAO;

public class ProgresoDAOMemoria implements ProgresoDAO {

    private Progreso guardado; // null miesntras no se guardo nada

    @Override 
    public boolean guardar(Progreso progreso) {
        if (progreso == null) return false;
        this.guardado = progreso;
        return true;
    }

    @Override 
    public Progreso cargar() {
        return guardado;
    }

    @Override 
    public boolean hayProgreso() {
        return guardado != null;
    }
    
}
