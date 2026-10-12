package Modelo;

import java.util.ArrayList;
import java.util.List;

// Detecta cuándo el personaje empieza y deja de caminar, y avisa a los observadores.
// No conoce sonidos: solo comunica el estado de los pasos y el piso donde ocurren.
public class Pasos {

    private final List<ObservadorPasos> observadores = new ArrayList<>();
    private boolean caminando = false;
    private TipoSuelo sueloActual = null;

    public void agregarObservador(ObservadorPasos obs) {
        if (obs != null && !observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    public void removerObservador(ObservadorPasos obs) {
        observadores.remove(obs);
    }

    // Se llama una vez por tick
    public void actualizar(boolean moviendose, TipoSuelo suelo) {
        if (moviendose && !caminando) {
            caminando = true;
            sueloActual = suelo;
            notificarInicio(suelo);
        } else if (!moviendose && caminando) {
            caminando = false;
            sueloActual = null;
            notificarFin();
        } else if (moviendose && suelo != sueloActual) {
            // sigue caminando pero cambió el piso (ej: pasó del nivel a una habitación)
            sueloActual = suelo;
            notificarInicio(suelo);
        }
    }

    // Para cortar los pasos desde afuera (pausa, muerte, cambio de pantalla)
    public void detener() {
        if (caminando) {
            caminando = false;
            sueloActual = null;
            notificarFin();
        }
    }

    private void notificarInicio(TipoSuelo suelo) {
        for (ObservadorPasos o : observadores) {
            o.pasosIniciados(suelo);
        }
    }

    private void notificarFin() {
        for (ObservadorPasos o : observadores) {
            o.pasosDetenidos();
        }
    }
}