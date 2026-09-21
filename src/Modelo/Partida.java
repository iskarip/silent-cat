package Modelo;

import java.util.HashMap;
import java.util.Map;

public class Partida {

    // -- ATRIBUTOS --
    private static Partida instancia;
    private Personaje personaje;

    private Map<Integer, Nivel> niveles;
    private int numeroNivelActual;

    // -- CONSTRUCTOR --
    private Partida() {
        this.niveles = new HashMap<>();
        this.numeroNivelActual = 1;
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

