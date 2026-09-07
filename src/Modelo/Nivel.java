package Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Nivel {
    
//--ATRIBUTOS--

    private int numeroNivel;
    private List<Enemigo> listaEnemigos;
    private Acertijo acertijo; 
    private boolean nivelSuperado;
    private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado

    //atributo para el mapa de colisiones, que va a ser un arreglo de booleanos

    private boolean[][] mapaColision;     
    private static final int TILE = 32;

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

    // metodo para cargar el mapa de colisiones desde un archivo de texto
    
    public void cargarMapaColision(String rutaArchivo) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error cargando el mapa de colisión: " + e.getMessage());
            return; 
        }
        int filas = lineas.size();
        int columnas = lineas.get(0).length();
        mapaColision = new boolean[filas][columnas];
        for (int f = 0; f < filas; f++) {
            String linea = lineas.get(f);
            for (int c = 0; c < columnas; c++) {
                mapaColision[f][c] = (linea.charAt(c) == '0');
            }
        }
    }
    
    // metodo para verificar si una posición en píxeles es válida (no colisiona con un obstáculo)
   
    public boolean esPosicionValida(int pixelX, int pixelY) {
        if (mapaColision == null) return true;
        int columna = pixelX / TILE;
        int fila = pixelY / TILE;
        if (fila < 0 || fila >= mapaColision.length) return false;
        if (columna < 0 || columna >= mapaColision[0].length) return false;
        return mapaColision[fila][columna];
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
