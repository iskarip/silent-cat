package Modelo;

public class Linterna {


// -- ATRIBUTOS -- owo

    private int bateria;
    private boolean encendido;
    private int alcance;

// -- CONSTRUCTOR -- u.u

    public Linterna(){
        this.bateria = 100;
        this.encendido = true;
        this.alcance = 100;
    }

// -- Setters y Getters -- uwu

    public int getBateria () {
        return bateria;
    }

    public boolean getEncendido  () {
        return encendido;
    }

    public int getAlcance (){
        return alcance;
    }

    public void setBateria (int bateria){
        this.bateria = bateria;
    }

    public void setEncendido (boolean encendido) {
        this.encendido = encendido;
    }

// -- METODOS -- :3


    public void encenderLinterna (){
        if ( getBateria() == 0) {
            setEncendido(false);
        } else {
            setEncendido(true);
        }
    }

    public void apagarLinterna (){
        setEncendido(false);
    }

    public boolean bateriaBaja (){
        return getBateria() < 20;
    }

    public void recargarLinterna (int recargaLinterna){
        int nuevaBateria = getBateria() + recargaLinterna;
        if (nuevaBateria > 100) {
            setBateria (100);
        } else if (nuevaBateria < 0){
            setBateria (0);
            setEncendido(false);
        } else {
        setBateria (nuevaBateria);    
        }
    }

    public void gastarBateria (){
        if (encendido && bateria > 0){
            bateria --;
        } 
        
        if (bateria == 0) {
            setEncendido(false);
        }
    }
    }
