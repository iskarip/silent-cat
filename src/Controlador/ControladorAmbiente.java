package Controlador;

import Modelo.ObservadorAmbiente;
import Modelo.Personaje;
import Modelo.ZonaProximidad;
import Vista.AvisoInteraccion;
import Vista.FlashbackGato;
import Vista.MensajeAmbiental;
import Vista.NivelPanel;
import Modelo.EspacioBase;

// Conecta las zonas del modelo con las capas visuales.
// Implementa ObservadorAmbiente: las zonas le piden "mostrá esto" y él sabe
// con qué clase de la Vista hacerlo. Así el modelo nunca conoce la Vista.

public class ControladorAmbiente implements ObservadorAmbiente {

    private final FlashbackGato flashbackGato;
    private final AvisoInteraccion avisoInteraccion;
    private final MensajeAmbiental mensajeAmbiental;
    private final NivelPanel vista;
    private Runnable alCompletar;

    public ControladorAmbiente(FlashbackGato flashbackGato, MensajeAmbiental mensajeAmbiental,
                           AvisoInteraccion avisoInteraccion, NivelPanel vista) {
        this.flashbackGato = flashbackGato;
        this.mensajeAmbiental = mensajeAmbiental;
        this.avisoInteraccion = avisoInteraccion;
        this.vista = vista;
    }

    // Se llama en cada tick. Recorre todas las zonas del nivel; cada una decide
    // sola si corresponde activarse y qué mostrar. Acá no hay ifs por tipo de zona.
    public void comprobarZonas(EspacioBase espacio, Personaje personaje) {
        for (ZonaProximidad zona : espacio.getZonas()) {
            zona.comprobar(personaje, this);
        }
    }

    public void setAlCompletar(Runnable alCompletar) { this.alCompletar = alCompletar; }

    // Pedido de una ZonaMensaje: el texto aparece en pantalla y se desvanece solo
    // (MensajeAmbiental calcula la opacidad al dibujarse).
    @Override
    public void mostrarMensaje(String mensaje) {
        mensajeAmbiental.mostrarMensaje(mensaje);
    }

    // Pedido de un PuntoFlashback: muestra la imagen del flashback y, cuando termina,
    // el mensaje del personaje (si el punto tiene uno).
    @Override
    public void mostrarFlashback(String mensajeAlTerminar) {
        flashbackGato.activar(vista, () -> {
            if (mensajeAlTerminar != null) {
                mensajeAmbiental.mostrarMensaje(mensajeAlTerminar);
            }
        });
    }


    @Override
    public void mostrarAvisoInteraccion(String texto) { avisoInteraccion.mostrar(texto); }

    @Override
    public void ocultarAvisoInteraccion() { avisoInteraccion.ocultar(); }

    @Override
    public void juegoCompletado() {
    if (alCompletar != null) alCompletar.run();
    }
}