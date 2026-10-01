package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Nivel extends EspacioBase {

//--ATRIBUTOS--

    private int numeroNivel;
    private Acertijo acertijoNivel;
    private List<Habitacion> habitaciones; // cada nivel puede tener varias habitaciones, pero por ahora solo vamos a usar una
    private boolean nivelSuperado;
    private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado
    private int accesoSiguienteNivelX, accesoSiguienteNivelY;
    private int acertijoColumna, acertijoFila; // posicion donde se activa el acertijo del nivel

    private List<ZonaProximidad> zonas = new ArrayList<>();


//--CONSTRUCTOR--

    public Nivel(int numeroNivel, Acertijo acertijoNivel, String rutaGrid, String rutaImagenFondo, int posicionInicialX, int posicionInicialY) { //aca va a marcar error hasta que este definida la clase acertijo
        super(rutaGrid, rutaImagenFondo, posicionInicialX, posicionInicialY);
        this.numeroNivel = numeroNivel;
        this.acertijoNivel = acertijoNivel;
        this.nivelSuperado = false;
        this.checkpoint = false; //arranca desactivado por defecto
        this.habitaciones = new ArrayList<>();
    }

    //--SET Y GET--

    public boolean getNivelSuperado() {
        return this.nivelSuperado;
    }

    public void setNivelSuperado(boolean nivelSuperado) {
        this.nivelSuperado = nivelSuperado;
    }

    public boolean getCheckpoint() {
        return this.checkpoint;
    }

    public int getNumeroNivel() {
        return this.numeroNivel;
    }

    public Acertijo getAcertijo() {
        return this.acertijoNivel;
    }

//agrega un gato en este caso o puede ser otra entidad pasiva al nivel. 

    public void agregarGato(Gato gato) {
      agregarEntidadPasiva(gato);
    }

    
    public Gato getGato() {
        for (EntidadPasiva entidad : getListaEntidadesPasivas()){
            if (entidad instanceof Gato){
                return (Gato) entidad;
            }
        }
        return null;
    }

    public List<Habitacion> getHabitaciones() {
        return this.habitaciones;
    }

    public int getAccesoSiguienteNivelX() {
        return accesoSiguienteNivelX;
    }

    public void setAccesoSiguienteNivelX(int accesoSiguienteNivelX) {
        this.accesoSiguienteNivelX = accesoSiguienteNivelX;
    }

    public int getAccesoSiguienteNivelY() {
        return accesoSiguienteNivelY;
    }

    public void setAccesoSiguienteNivelY(int accesoSiguienteNivelY) {
        this.accesoSiguienteNivelY = accesoSiguienteNivelY;
    }

    // LOGICA PARA LOS ACERTIJOS

    public void setAcertijoEnTile(int columna, int fila) {
        this.acertijoColumna = columna;
        this.acertijoFila = fila;
    }

    public int getAcertijoColumna() {
        return acertijoColumna;
    }

    public int getAcertijoFila() {
        return acertijoFila;
    }

            // Devuelven el CENTRO del tile en píxeles, así ControladorAcertijo
            // sigue llamando a getAcertijoX()/getAcertijoY() sin cambios.

    public int getAcertijoX() {
        return acertijoColumna * MapaColision.TILE + MapaColision.TILE / 2;
    }

    public int getAcertijoY() {
        return acertijoFila * MapaColision.TILE + MapaColision.TILE / 2;
    }

//--METODOS--

    public void guardarCheckpoint() {
        this.checkpoint = true;
        System.out.println("Checkpoint activado en el nivel " + this.numeroNivel + "!!!");
    }

    public boolean verificarSiCompleto() {
        boolean acertijoOk = acertijoNivel.getResuelto();
        Gato gato = getGato();
        boolean gatoOk = (gato == null) || gato.getEncontrado();
        return acertijoOk && gatoOk;
    }

    public void agregarHabitacion(Habitacion h) {
        if (h != null) {
            this.habitaciones.add(h);
        }
    }

    public void agregarZona(ZonaProximidad zona) {
        if (zona != null) zonas.add(zona);
    }

    public List<ZonaProximidad> getZonas() {
        return zonas;
    }

}
