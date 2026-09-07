package Modelo;

import java.util.ArrayList;
import java.util.List;


public class Nivel {
    
//--ATRIBUTOS--

    private int numeroNivel;
    private List<Enemigo> listaEnemigos;
    private Acertijo acertijo; 
    private boolean nivelSuperado;
    private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado

    private MapaColision mapaColision = new MapaColision(); // cada nivel tiene su propio mapa de colision, que se carga desde un archivo .txt

    private Gato gato; // null en los niveles que no lo tienen (ej: nivel 1 y 2)

//--CONSTRUCTOR--

public Nivel (int numeroNivel, Acertijo acertijo){ //aca va a marcar error hasta que este definida la clase acertijo
    this.numeroNivel= numeroNivel;
    this.acertijo = acertijo;
    this.nivelSuperado = false;
    this.checkpoint = false; //arranca desactivado por defecto
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

    public void movimientoEntidad() {
        System.out.println("Moviendo entidades enemigas en el nivel " + this.numeroNivel);
        for (Enemigo e : listaEnemigos) {
            e.patrullar();
        }
    }

    public boolean verificarSiCompleto() {
        boolean acertijoOk = acertijo.getResuelto();
        boolean gatoOk = (gato == null) || gato.getEncontrado();
    return acertijoOk && gatoOk;
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
    return this.acertijo;
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

}
