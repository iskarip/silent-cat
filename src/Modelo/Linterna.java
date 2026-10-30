package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Linterna {

    // -- ATRIBUTOS (todos private: nadie de afuera los modifica) --

    private int bateria;       // carga actual, siempre entre 0 y 100
    private boolean encendido; // true si la luz esta prendida
    private int alcance;       // hasta donde llega la luz

    // Lista de interesados en los cambios. Es final: la lista no se reemplaza,
    // solo se le agregan o sacan observadores
    private final List<ObservadorLinterna> observadores = new ArrayList<>();

    // -- CONSTRUCTOR --

    // Toda linterna nace en un estado conocido y valido: carga completa,
    // encendida y con su alcance estandar
    public Linterna() {
        this.bateria = 100;
        this.encendido = true;
        this.alcance = 100;
    }

    // -- OBSERVER: registro de interesados --

    // Anota a un observador para que reciba los avisos
    // Ignora null y los repetidos (para no avisarle dos veces a lo mismo)
    public void agregarObservador(ObservadorLinterna obs) {
        if (obs != null && !observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    // Saca a un observador de la lista (deja de recibir avisos)
    public void removerObservador(ObservadorLinterna obs) {
        observadores.remove(obs);
    }

    // Avisa a todos los observadores el estado actual. Es private: solo la
    // propia linterna decide cuando avisar. Se llama al final de cada metodo
    // que cambia el estado
    private void notificar() {
        for (ObservadorLinterna o : observadores) {
            o.linternaCambio(this.bateria, this.encendido);
        }
    }

    // -- SETTERS --

    public void setBateria(int bateria) {
        if (bateria >= 100) {
            this.bateria = 100;
        } else if (bateria <= 0) {
            this.bateria = 0;
            this.encendido = false;
        } else {
            this.bateria = bateria;
        }
        notificar();
    }

    private void setEncendido(boolean encendido) {
        this.encendido = encendido;
        notificar();
    }

    // -- OPERACIONES  --

    public void encenderLinterna() {
        if (this.bateria > 0) {
            setEncendido(true);
        }
    }

    // Apaga la linterna. Apagar siempre es posible, no hace falta validar
    public void apagarLinterna() {
        setEncendido(false);
    }

    // Suma carga a la bateria. Usa setBateria, asi que nunca pasa de 100
    // Ejemplo: la usa el item Bateria cuando el personaje lo recoge
    public void recargarLinterna(int recargaLinterna) {
        setBateria(this.bateria + recargaLinterna);
    }

    // Gasta 1 punto de bateria, pero solo si esta encendida y le queda carga
    // Se llama una vez por tick del juego. Si llega a 0, se apaga sola
    public void gastarBateria() {
        if (encendido && bateria > 0) {
            bateria--;
            if (bateria == 0) {
                encendido = false;
            }
            notificar();
        }
    }

    // -- GETTERS (solo lectura: permiten mirar el estado sin poder cambiarlo) --

    // Devuelve la carga actual (0 a 100)
    public int getBateria() { return bateria; }

    // Devuelve true si la linterna esta encendida
    public boolean getEncendido() { return encendido; }

    // Devuelve el alcance de la luz. No tiene setter: no cambia desde afuera.
    public int getAlcance() { return alcance; }

    // Pregunta derivada: true si queda menos del 20% de bateria.
    // No guarda nada nuevo, solo responde a partir de la bateria.
    public boolean bateriaBaja() { return bateria < 20; }
}