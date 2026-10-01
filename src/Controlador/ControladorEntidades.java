package Controlador;

import Modelo.Entidad;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.NivelPanel;

public class ControladorEntidades {

    public void actualizar(Nivel nivelActual, Personaje personaje, NivelPanel vista) {
        if (nivelActual == null || personaje == null) return;

        for (Entidad e : nivelActual.getListaEntidades()) {
            if (e.estaVivo()) {
                e.actualizar(personaje, nivelActual.getMapaColision());
            }
        }
    }
}