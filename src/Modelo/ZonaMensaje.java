package Modelo;

//para que al acercarse el personaje a ciertas zonas, aparezca el
//mensaje que queramos mostrar, la idea es que el mensaje se active solo
//cuando pasa una vez el pj, no cada vez que pasa por la zona, hereda de zona proximidad

public class ZonaMensaje extends ZonaProximidad {

    private final String mensaje;

    public ZonaMensaje(int columna, int fila, int radioEnTiles, String mensaje) {
        super(columna, fila, radioEnTiles);
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }

    @Override
    protected void alActivar(Personaje personaje, ObservadorAmbiente observador) {
        observador.mostrarMensaje(mensaje);
    }
}