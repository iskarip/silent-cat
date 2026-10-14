package Controlador;

import Modelo.Entidad;
import Modelo.Personaje;
import Vista.NivelPanel;
import Modelo.EspacioBase;

public class ControladorEntidades {

//distancia dentro de la cual la entidad cuenta para la tension
private static final int RADIO_TENSION = 350;

    public void actualizar(EspacioBase espacio, Personaje personaje, NivelPanel vista) {
        if (espacio == null || personaje == null) return;

        for (Entidad e : espacio.getListaEntidades()) {
            if (e.estaVivo()) {
                e.actualizar(personaje, espacio.getMapaColision());
            }
        }
    }

// devuelve la mayor amenaza de las entidades cercanas al jugador
// el polimorfismo aca es que nunca pregunta de que clase es cada entidad, solo llama getNivelAmenaza().
public int calcularTension(EspacioBase espacio, Personaje jugador) {
    if (espacio == null || jugador == null) return 0;

    int maxima = 0;
    for (Entidad e : espacio.getListaEntidades()) {
        if (!e.estaVivo()) continue;

        double distancia = Math.hypot(e.getPosicionX() - jugador.getPosicionX(),
                                      e.getPosicionY() - jugador.getPosicionY());
        if (distancia <= RADIO_TENSION) {
            maxima = Math.max(maxima, e.getNivelAmenaza());
        }
    }
    return maxima;
    }

}