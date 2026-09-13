package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Nivel extends EspacioBase {
    
//--ATRIBUTOS--

    private int numeroNivel;
    private Acertijo acertijoNivel; 
    private List<Enemigo> listaEnemigos; // cada nivel puede tener varios enemigos, pero por ahora solo vamos a usar uno
    private List<Habitacion> habitaciones; // cada nivel puede tener varias habitaciones, pero por ahora solo vamos a usar una
    private boolean nivelSuperado;
    private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado
    private int accesoSiguienteNivelX, accesoSiguienteNivelY;

    private Gato gato; // null en los niveles que no lo tienen (ej: nivel 1 y 2)

//--CONSTRUCTOR--

public Nivel (int numeroNivel, Acertijo acertijoNivel, String rutaGrid){ //aca va a marcar error hasta que este definida la clase acertijo
    super(rutaGrid);
    this.numeroNivel= numeroNivel;
    this.acertijoNivel = acertijoNivel;
    this.nivelSuperado = false;
    this.checkpoint = false; //arranca desactivado por defecto
    this.habitaciones = new ArrayList<>();
    this.listaEnemigos = new ArrayList<>();
}

//--METODOS--

    public void guardarCheckpoint(){
        this.checkpoint = true;
        System.out.println("Checkpoint activado en el nivel "+ this.numeroNivel + "!!!");
    }

    public void agregarEnemigo(Enemigo e) {
        if (e != null) {
            this.listaEnemigos.add(e);
        }
    }

    public void movimientoEntidad(Personaje jugador, boolean linternaEncendida) {
        System.out.println("Moviendo entidades enemigas en el nivel " + this.numeroNivel);
        for (Enemigo e : listaEnemigos) {
            e.actualizarComportamiento(jugador, linternaEncendida);
        }
    }

    public boolean verificarSiCompleto() {
        boolean acertijoOk = acertijoNivel.getResuelto();
        boolean gatoOk = (gato == null) || gato.getEncontrado();
    return acertijoOk && gatoOk;
    }

    public void agregarHabitacion(Habitacion h) {
        if (h != null) {
            this.habitaciones.add(h);
        }
    }


//--SET Y GET--

    public boolean getNivelSuperado() {
        return this.nivelSuperado;
    }

    public void setNivelSuperado(boolean nivelSuperado) {
        this.nivelSuperado = nivelSuperado;
    }

    public boolean getCheckpoint(){
        return this.checkpoint;
    }

    public int getNumeroNivel() {
        return this.numeroNivel;
    }

    public Acertijo getAcertijo() {
        return this.acertijoNivel;
    }

    public List<Enemigo> getListaEnemigos() {
        return this.listaEnemigos;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public Gato getGato() {
        return this.gato; // puede devolver null si el nivel no tiene gato
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

}
