package Modelo;

public class Configuracion {
    
    private float volumenMusica;
    private float volumenEfectos;

    public Configuracion(float volumenMusica, float volumenEfectos) {
        this.volumenMusica = volumenMusica;
        this.volumenEfectos = volumenEfectos;
    }

    public float getVolumenMusica() { return volumenMusica; }
    public float getVolumenEfectos() { return volumenEfectos; }
}
