package Modelo;

import java.util.HashMap;
import java.util.Map;

public class Partida {

    // -- ATRIBUTOS --
    private Personaje personaje;
    private String genero; // distingue el personaje: hace falta para poder guardar y continuar
    private EspacioBase espacioActivo;

    private Map<Integer, Nivel> niveles;
    private int numeroNivelActual;
    private Habitacion habitacionActiva;

    // -- CONSTRUCTOR --
    // Constructor público para permitir la instanciación directa (inyección de dependencias)
    public Partida() {
        this.niveles = new HashMap<>();
        this.numeroNivelActual = 1;
    }

    // -- SETS Y GETS --

    public Nivel getNivelActual() {
        return niveles.get(numeroNivelActual); // Devuelve el Nivel, o null si la clave no existe en el mapa
    }

    public EspacioBase getEspacioActivo() { return espacioActivo; }

    public Habitacion getHabitacionActiva() {
        return habitacionActiva;
    }

    public Personaje getPersonaje() { return personaje; }
    public void setPersonaje(Personaje personaje) { this.personaje = personaje; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    
    // -- METODOS --

    public void iniciarPartida(int cantidadNiveles) {
        NivelFactory factory = new NivelFactory();
        niveles.clear();
        for (int i = 1; i <= cantidadNiveles; i++) {
            try {
                niveles.put(i, factory.crearNivel(i));
            } catch (NivelInvalidoException e) {
                System.out.println("No se pudo cargar el nivel " + i + ": " + e.getMessage());
                
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

    // Arma una foto del estado actual para guardarla
    public Progreso crearProgreso() {
        Nivel nivel = getNivelActual();
        
        // Guardamos la posición EXACTA en píxeles donde está el jugador en ese momento
        int posX = (personaje != null) ? personaje.getPosicionX() : nivel.getCheckpointX();
        int posY = (personaje != null) ? personaje.getPosicionY() : nivel.getCheckpointY();

        // Guardamos los ítems del inventario
        StringBuilder itemsStr = new StringBuilder();
        if (personaje != null && personaje.getInventario() != null) {
            for (Item item : personaje.getInventario().getItems()) {
                if (itemsStr.length() > 0) itemsStr.append(",");
                itemsStr.append(item.getNombre());
            }
        }

        String nombreHab = (habitacionActiva != null) ? habitacionActiva.getNombreHabitacion() : "";

        return new Progreso(genero, numeroNivelActual,
                posX, posY,
                personaje.getPuntosVida(), personaje.getLinterna().getBateria(),
                itemsStr.toString(), nombreHab);
    }

    // Lo contrario: después de iniciarPartida(), deja la partida como estaba al guardar
    public boolean aplicarProgreso(Progreso progreso) {
        if (progreso == null || !niveles.containsKey(progreso.getNumeroNivel())) {
            return false;
        }

        for (int i = 1; i < progreso.getNumeroNivel(); i++) {
            if (niveles.containsKey(i)) {
                niveles.get(i).setNivelSuperado(true);
            }
        }

        numeroNivelActual = progreso.getNumeroNivel();
        Nivel nivel = getNivelActual();
        nivel.guardarCheckpoint(progreso.getCheckpointX(), progreso.getCheckpointY());

        espacioActivo = nivel;
        habitacionActiva = null;

        // Se posiciona al personaje exactamente en los píxeles guardados
        personaje.setPosicionX(progreso.getCheckpointX());
        personaje.setPosicionY(progreso.getCheckpointY());

        // Para seguridad: si la hitbox pisa un obstáculo sólido, se ajusta un poco
        java.awt.Rectangle hb = personaje.getHitbox();
        if (nivel.getMapaColision() != null &&
                !nivel.getMapaColision().esRectanguloValido(hb.x, hb.y, hb.width, hb.height)) {
            personaje.setPosicionY(progreso.getCheckpointY() - 10);
        }

        personaje.setPuntosVida(progreso.getVida());
        personaje.getLinterna().setBateria(progreso.getBateria());
        this.genero = progreso.getGenero();

        // Restaurar inventario recogiendo los ítems del suelo 
        String guardados = progreso.getItemsInventario();
        if (guardados != null && !guardados.trim().isEmpty()) {
            String[] nombres = guardados.split(",");
            for (String nombre : nombres) {
                String nombreLimpio = nombre.trim();
                Item itemEncontrado = null;
                for (Item it : nivel.getListaItems()) {
                    if (it.getNombre().equalsIgnoreCase(nombreLimpio)) {
                        itemEncontrado = it;
                        break;
                    }
                }
                if (itemEncontrado != null) {
                    itemEncontrado.recoger();
                    personaje.getInventario().agregarItem(itemEncontrado);
                }
            }
        }

        return true;
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

    // en Partida
    public boolean hayInteraccionAlAlcance() {
        if (habitacionActiva != null) {
            return habitacionActiva.salidaAlAlcance(personaje);
        }
        Nivel nivel = getNivelActual();
        return nivel.getHabitacionAlAlcance(personaje) != null
                || nivel.getAcertijoAlAlcance(personaje) != null;
    }
}