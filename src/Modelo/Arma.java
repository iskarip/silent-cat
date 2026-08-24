package Modelo;

public class Arma {

    // -- ATRIBUTOS --
    private int danio;
    private int alcance;
    private int tiempoDeUtilizacion;

    // -- CONSTRUCTOR --
    public Arma (){
        this.danio = 10;
        this.alcance = 5;
        this.tiempoDeUtilizacion = 3;
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

    public boolean usarArma () {
        // TODO (Fase 2): comparar tiempo transcurrido desde el último ataque
        // contra tiempoDeUtilizacion. Si no pasó suficiente tiempo, devolver false
        // (el personaje no puede atacar todavía)
        return true; // por ahora, siempre permite atacar
    }


}
