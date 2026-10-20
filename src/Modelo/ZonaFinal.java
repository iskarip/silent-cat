package Modelo;

public class ZonaFinal extends ZonaMensaje{
        public ZonaFinal(int columna, int fila, int radioEnTiles, String mensaje) {
        super(columna, fila, radioEnTiles, mensaje);
    }

    @Override
    protected void alActivar(Personaje personaje, ObservadorAmbiente observador) {
        super.alActivar(personaje, observador); // muestra el mensaje
        observador.juegoCompletado();           // y avisa al observadorque se terminó
    }
}
