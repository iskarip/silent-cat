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

    public void setBateria (int bateria){ //consistencia 
        if (bateria>= 100){
            this.bateria= 100;
        }
        else if (bateria <= 0){
            this.bateria=0;
            this.encendido = false;//se apagaria la bateria si llega a 0
        }
        else{
            this.bateria= bateria;
        }
  
    }

    public void setEncendido (boolean encendido) {
        this.encendido = encendido;
    }

// -- METODOS -- :3


    public void encenderLinterna (){ 
           if(this.bateria >0){ 
        setEncendido(true);
        }
    }
        /*        if ( getBateria() == 0) {
            setEncendido(false);
        } else {
            setEncendido(true);
        }*/
    

    public void apagarLinterna (){
        setEncendido(false);
    }

    public boolean bateriaBaja (){
        return getBateria() < 20;
    }

    public void recargarLinterna (int recargaLinterna){
      setBateria(this.bateria + recargaLinterna); //como ya esta la consistencia en el set bateria ya se valida que no supere el 100 ni baje del 0.

      
        /*  int nuevaBateria = getBateria() + recargaLinterna;
        if (nuevaBateria > 100) {
            setBateria (100);
        } else if (nuevaBateria < 0){
            setBateria (0);
            setEncendido(false);
        } else {
        setBateria (nuevaBateria);    
        }
    */
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
