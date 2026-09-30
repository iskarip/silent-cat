package Controlador;

import Modelo.Nivel;
import Modelo.ObservadorAmbiente;
import Modelo.Personaje;
import Modelo.ZonaProximidad;
import Vista.FlashbackGato;
import Vista.MensajeAmbiental;
import Vista.NivelPanel;

// Conecta las zonas del modelo con las capas visuales.
// Implementa ObservadorAmbiente: las zonas le piden "mostrá esto" y él sabe
// con qué clase de la Vista hacerlo. Así el modelo nunca conoce la Vista.

public class ControladorAmbiente implements ObservadorAmbiente {

    private final FlashbackGato flashbackGato;
    private final MensajeAmbiental mensajeAmbiental;
    private final NivelPanel vista;

    public ControladorAmbiente(FlashbackGato flashbackGato,
                               MensajeAmbiental mensajeAmbiental,
                               NivelPanel vista) {
        this.flashbackGato = flashbackGato;
        this.mensajeAmbiental = mensajeAmbiental;
        this.vista = vista;
    }

    // Se llama en cada tick. Recorre todas las zonas del nivel; cada una decide
    // sola si corresponde activarse y qué mostrar. Acá no hay ifs por tipo de zona.
    public void comprobarZonas(Nivel nivel, Personaje personaje) {
        for (ZonaProximidad zona : nivel.getZonas()) {
            zona.comprobar(personaje, this);
        }
    }

    // Pedido de una ZonaMensaje: el texto aparece en pantalla y se desvanece solo
    // (MensajeAmbiental calcula la opacidad al dibujarse).
    @Override
    public void mostrarMensaje(String mensaje) {
        mensajeAmbiental.mostrarMensaje(mensaje);
    }

    // Pedido de un PuntoFlashback: muestra la imagen del flashback.
    @Override
    public void mostrarFlashback() {
        flashbackGato.activar(vista);
    }
}