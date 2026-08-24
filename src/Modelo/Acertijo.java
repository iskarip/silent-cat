public abstract class Acertijo {  // public abstract class? creo que lo vemos mas adelante.

// -- METODOS --

    private int id;
    private String descripcion;
    private boolean resuelto;

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

    public void setResuelto(boolean resuelto){
        this.resuelto = resuelto;
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

    // -- METODOS ABSTRACTOS --

    protected abstract boolean verificarRespuesta(String respuesta);

    // -- PARTE INTERACTUABLE --
    @Override
    public void interactuar(Personaje perso) {
        mostrarEnunciado();
        // TODO: Aca se debe mostrar un cuadro de texto para que el jugador escriba su respuesta
    }



}
