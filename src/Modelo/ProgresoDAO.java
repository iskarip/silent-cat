package Modelo;

public interface ProgresoDAO {
    boolean guardar(Progreso progreso); // true si se pudo guardar
    Progreso cargar(); // null si no hay partida guardada
    boolean hayProgreso();
}
