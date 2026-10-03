package Modelo;

public class Arma {

    // -- ATRIBUTOS --
    private int danio;
    private int alcance;
    private int tiempoDeUtilizacion;
    private int ticksRestantes = 0;

    // -- CONSTRUCTOR --
    public Arma (){
        this.danio = 10;
        this.alcance = 5;
        this.tiempoDeUtilizacion = 24;
    }

    // -- GET --

    public int getDanio() {
        return danio;
    }

    public int getAlcance(){
        return alcance;
    }

    public int getTiempoDeUtilizacion() {
        return tiempoDeUtilizacion;
    }

    // -- METODOS --

    public int calcularDanio (){
        return this.danio;
    }

    // intenta usar el arma, si todavia se esta enfriando devuelve false
    // si esta lista, arranca el enfriamiento y devuelve true
    public boolean usarArma() {
        if (ticksRestantes > 0) {
            return false;
        }
        ticksRestantes = tiempoDeUtilizacion;
        return true;
    }

    // se llama una vez por tick
    // va descontando el enfriamiento
    public void actualizar() {
        if (ticksRestantes > 0) {
            ticksRestantes--;
        }
    }


}
