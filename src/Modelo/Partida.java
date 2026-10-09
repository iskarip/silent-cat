package Modelo;

import java.util.HashMap;
import java.util.Map;

public class Partida {

    // -- ATRIBUTOS --
    private static Partida instancia;
    private Personaje personaje;
    private EspacioBase espacioActivo;

    private Map<Integer, Nivel> niveles;
    private int numeroNivelActual;
    private Habitacion habitacionActiva;

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

    public EspacioBase getEspacioActivo() { return espacioActivo; }

    public Habitacion getHabitacionActiva() {
        return habitacionActiva;
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
        // La partida arma al protagonista y lo ubica en el inicio del primer nivel
        this.personaje = new Personaje("Protagonista", new Arma(), new Linterna());
        Nivel inicial = getNivelActual();
        this.espacioActivo = inicial;
        this.habitacionActiva = null;
        if (inicial != null) {
            personaje.colocarEnTile(inicial.getPosicionInicialX(), inicial.getPosicionInicialY());
        }
    }

    public boolean avanzarSiguienteNivel() {
        getNivelActual().setNivelSuperado(true);

        int siguiente = numeroNivelActual + 1;
        if (niveles.containsKey(siguiente)) {
            numeroNivelActual = siguiente;
            Nivel nuevo = getNivelActual();
            espacioActivo = nuevo;
            habitacionActiva = null;
            personaje.colocarEnTile(nuevo.getPosicionInicialX(), nuevo.getPosicionInicialY());
            return true;
        }
        return false;
    }

    public void entrarAHabitacion(Habitacion habitacion) {
        espacioActivo = habitacion;
        habitacionActiva = habitacion;
        personaje.colocarEnTile(habitacion.getPosicionInicialX(), habitacion.getPosicionInicialY());
    }

    public void salirDeHabitacion() {
        Habitacion habitacion = habitacionActiva;
        if (habitacion == null) return; // ya esta en el nivel

        habitacionActiva = null;
        espacioActivo = getNivelActual();
        personaje.colocarEnTile(habitacion.getRetornoX(), habitacion.getRetornoY());
    }
}

