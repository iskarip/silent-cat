package Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 QUE HACE ESTA CLASE
 -------------------
 * Representa la linterna del personaje. Guarda tres datos: cuanta bateria
   tiene (0 a 100), si esta encendida o apagada, y hasta donde llega su luz.

 LA PROMESA DE LA CLASE (estado valido):
   1. La bateria siempre esta entre 0 y 100.
   2. Si la bateria llega a 0, la linterna se apaga sola.
   3. No se puede encender una linterna sin bateria.

 COMO LA CUMPLE (encapsulamiento):
   - Los atributos son private: nadie de afuera los toca directamente.
   - Los cambios se hacen con operaciones (encender, apagar, recargar,
     gastar) que aplican las reglas.
   - Desde afuera solo se puede MIRAR con los getters.

 COLABORACION (patron Observer):
    La linterna no conoce a la vista ni al sonido. Guarda una lista de
    ObservadorLinterna y les avisa (notificar) cada vez que algo cambia.
    Asi la barra de bateria y el aura se actualizan sin que la linterna
    sepa que existen.
 */
public class Linterna {

    // -- ATRIBUTOS (todos private: nadie de afuera los modifica) --

    private int bateria;       // carga actual, siempre entre 0 y 100
    private boolean encendido; // true si la luz esta prendida
    private int alcance;       // hasta donde llega la luz

    // Lista de interesados en los cambios. Es final: la lista no se reemplaza,
    // solo se le agregan o sacan observadores.
    private final List<ObservadorLinterna> observadores = new ArrayList<>();

    // -- CONSTRUCTOR --

    // Toda linterna nace en un estado conocido y valido: carga completa,
    // encendida y con su alcance estandar.
    public Linterna() {
        this.bateria = 100;
        this.encendido = true;
        this.alcance = 100;
    }

    // -- OBSERVER: registro de interesados --

    // Anota a un observador para que reciba los avisos.
    // Ignora null y los repetidos (para no avisarle dos veces a lo mismo).
    public void agregarObservador(ObservadorLinterna obs) {
        if (obs != null && !observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    // Saca a un observador de la lista (deja de recibir avisos).
    public void removerObservador(ObservadorLinterna obs) {
        observadores.remove(obs);
    }

    // Avisa a todos los observadores el estado actual. Es private: solo la
    // propia linterna decide cuando avisar. Se llama al final de cada metodo
    // que cambia el estado.
    private void notificar() {
        for (ObservadorLinterna o : observadores) {
            o.linternaCambio(this.bateria, this.encendido);
        }
    }

    // -- SETTERS --

    // SETTER de bateria (public porque Partida lo usa para restaurar la
    // bateria guardada al cargar una partida).
    // No es un setter "tonto": VALIDA antes de guardar.
    //   - Si el valor es 100 o mas, la bateria queda en 100.
    //   - Si es 0 o menos, queda en 0 y la linterna se apaga.
    //   - Si esta entre medio, se guarda tal cual.
    // Siempre termina avisando a los observadores.
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

    // SETTER de encendido.
    // Este metodo asigna el valor sin revisar la bateria. Si fuera
    // public, alguien podria hacer setEncendido(true) con bateria 0 y romper
    // la promesa "no se enciende sin bateria". Al ser private, el unico
    // camino para encender es encenderLinterna(), que SI lo valida.
    private void setEncendido(boolean encendido) {
        this.encendido = encendido;
        notificar();
    }

    // -- OPERACIONES (lo que la linterna ofrece al resto del programa) --

    // Enciende la linterna, pero solo si tiene bateria. Si esta en 0, no hace nada.
    public void encenderLinterna() {
        if (this.bateria > 0) {
            setEncendido(true);
        }
    }

    // Apaga la linterna. Apagar siempre es posible, no hace falta validar.
    public void apagarLinterna() {
        setEncendido(false);
    }

    // Suma carga a la bateria. Usa setBateria, asi que nunca pasa de 100.
    // Ejemplo: la usa el item Bateria cuando el personaje lo recoge.
    public void recargarLinterna(int recargaLinterna) {
        setBateria(this.bateria + recargaLinterna);
    }

    // Gasta 1 punto de bateria, pero solo si esta encendida y le queda carga.
    // Se llama una vez por tick del juego. Si llega a 0, se apaga sola.
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

    // Devuelve la carga actual (0 a 100).
    public int getBateria() { return bateria; }

    // Devuelve true si la linterna esta encendida.
    public boolean getEncendido() { return encendido; }

    // Devuelve el alcance de la luz. No tiene setter: no cambia desde afuera.
    public int getAlcance() { return alcance; }

    // Pregunta derivada: true si queda menos del 20% de bateria.
    // No guarda nada nuevo, solo responde a partir de la bateria.
    public boolean bateriaBaja() { return bateria < 20; }
}