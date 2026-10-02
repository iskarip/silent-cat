package Modelo;

public abstract class Acertijo implements Interactuable { 

// -- METODOS --

    private int id;
    private String descripcion;
    private boolean resuelto;

    private static final int INTENTOS_MAXIMOS = 3;
    private static final int DANIO_FALLO = 10;
    private int intentosRestantes = INTENTOS_MAXIMOS;

// -- CONSTRUCTOR --

    public Acertijo (int id, String descripcion){
        this.id = id;
        this.descripcion = descripcion;
        resuelto = false;
    }

// -- GET y SET --

    public int getId(){
        return id;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public boolean getResuelto(){
        return resuelto;
    }

    protected void setResuelto(boolean resuelto){
        this.resuelto = resuelto;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

// -- METODOS --

    public void mostrarEnunciado () {
        System.out.println(this.descripcion);
    }

    public boolean validarRespuesta (String respuesta) {
        if (this.resuelto) {
            return true;
        }
        boolean esCorrecta = verificarRespuesta(respuesta);
        if (esCorrecta) {
            this.resuelto = true;
        }
            return esCorrecta;
    }

//Para que acertijoObjeto pueda sobreescribir y validar mirando el inventario
//en lugar del texto sin tocar acertijonumerico ni el controlador

    public boolean validarRespuesta (String respuesta, Personaje personaje){
        return validarRespuesta(respuesta);
    }

    public void reiniciarIntentos(){
        intentosRestantes = INTENTOS_MAXIMOS;
    }

    // evalua la respuesta y descuenta un intento si falla
    public boolean responder(String respuesta, Personaje personaje) {
        if (validarRespuesta(respuesta, personaje)) {
            return true;
        }

        intentosRestantes--;

        if (intentosRestantes <= 0) {
            personaje.recibirDanio(DANIO_FALLO);
        }
        return false;
    }


    // -- METODOS ABSTRACTOS --

    protected abstract boolean verificarRespuesta(String respuesta);

    // -- PARTE INTERACTUABLE --
    @Override
    public void interactuar(Personaje p) {
        mostrarEnunciado();
        // TODO: Aca se debe mostrar un cuadro de texto para que el jugador escriba su respuesta
    }

}
