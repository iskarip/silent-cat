package Controlador;

import Modelo.EspacioBase;
import Modelo.Habitacion;
import Modelo.Partida;
import Vista.NivelPanel;

// Coordina el cambio de espacio (nivel <-> habitación, nivel -> nivel siguiente).
// El estado vive en Partida; acá solo se le pide el cambio al modelo y se actualiza la vista.
public class ControladorEspacio {

    private final Partida partida;
    private final NivelPanel vista;

    public ControladorEspacio(Partida partida, NivelPanel vista) {
        this.partida = partida;
        this.vista = vista;
    }

    public EspacioBase getEspacioActivo() {
        return partida.getEspacioActivo();
    }

    public boolean estaEnHabitacion() {
        return partida.getHabitacionActiva() != null;
    }

    // Al iniciar el nivel: la vista pasa a mostrar el espacio con el que arranca la partida
    public void sincronizarVista() {
        if (partida.getNivelActual() != null) {
            vista.setNivelActual(partida.getNivelActual());
        }
    }

    public void entrar(Habitacion habitacion) {
        partida.entrarAHabitacion(habitacion);
        vista.setEspacioActual(habitacion);
    }

    public void salir() {
        if (!estaEnHabitacion()) return;
        partida.salirDeHabitacion();
        vista.setEspacioActual(partida.getNivelActual());
    }

    // true si había un nivel siguiente y se pasó a él
    public boolean cambiarDeNivel() {
        if (!partida.avanzarSiguienteNivel()) return false;
        vista.setNivelActual(partida.getNivelActual());
        return true;
    }

    public boolean hayInteraccionAlAlcance() {
        return partida.hayInteraccionAlAlcance();
    }

}
