package Modelo;

// --OBSERVADOR PERSONAJE --

public interface ObservadorPersonaje {

    void vidaCambio (int vidaActual, int vidaMaxima);
    // salta cuando la vida del personaje llega a 0.
    void personajeMurio();

}
