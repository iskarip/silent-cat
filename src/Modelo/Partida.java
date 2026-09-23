package Modelo;

import java.util.HashMap;
import java.util.Map;

public class Partida {

    // -- ATRIBUTOS --
    private static Partida instancia;
    private Personaje personaje;
    private Gato gato; //necesito ponerlo aca para poder usar los flashbacks durante el juego, es como un gato de narrativa de nuestro juego


    private Map<Integer, Nivel> niveles;
    private int numeroNivelActual;

    // -- CONSTRUCTOR --
    private Partida() {
        this.niveles = new HashMap<>();
        this.numeroNivelActual = 1;
        this.gato = new Gato(); // se crea una sola vez el gato
    }

    // -- SETS Y GETS --

    public static Partida getInstancia() {
        if (instancia == null) {
            instancia = new Partida();
        }
        return instancia;
    }

    public Nivel getNivelActual() {
        return niveles.get(numeroNivelActual); // Devuelve el Nivel, o null si la clave no existe en el mapa
    }

    public Personaje getPersonaje() { return personaje; }
    public void setPersonaje(Personaje personaje) { this.personaje = personaje; }

    public Gato getGato() { return this.gato; } //para poder usar el gato en el controlador
    
    // -- METODOS --

    public void iniciarPartida(int cantidadNiveles) {
        NivelFactory factory = new NivelFactory();
        niveles.clear();
        for (int i = 1; i <= cantidadNiveles; i++) {
            try {
                niveles.put(i, factory.crearNivel(i));
            } catch (NivelInvalidoException e) {
                System.out.println("No se pudo cargar el nivel " + i + ": " + e.getMessage());
                // el juego sigue — ese numero de nivel simplemente no queda disponible
            }
        }
        numeroNivelActual = 1;
    }

    public boolean avanzarSiguienteNivel() {
        int siguiente = numeroNivelActual + 1;
        if (niveles.containsKey(siguiente)) {
            numeroNivelActual = siguiente;
            return true;
        }
        return false;
    }
}

