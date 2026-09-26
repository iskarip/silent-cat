package Controlador;

import Modelo.Gato;
import Modelo.Nivel;
import Modelo.Personaje;
import Modelo.PuntoFlashback;
import Modelo.ZonaMensaje;
import Vista.FlashbackGato;
import Vista.MensajeAmbiental;
import Vista.NivelPanel;


public class ControladorAmbiente {
    
    public void comprobarFlashbacks (Nivel nivel, Personaje personaje, Gato gato, FlashbackGato flashbackGatoVista, NivelPanel vista){
        if (nivel.getPuntosFlashback() ==null) return;

        for(PuntoFlashback punto : nivel.getPuntosFlashback()){
            if (!punto.getActivado() && punto.estaCerca(personaje)) {
                punto.marcarActivado();
                if (gato != null) {
                    gato.activarEfectoFlashback(personaje);
                }
                flashbackGatoVista.activar(vista);
            }
        }
    }
        //ahora repito casi lo mismo pero para zonas de mensajes
     public void comprobarMensajes(Nivel nivel, Personaje personaje, MensajeAmbiental Mensaje) {
        if (nivel== null || nivel.getZonasMensaje() == null) return;
 
        for (ZonaMensaje zona : nivel.getZonasMensaje()) {
            if (!zona.getActivado() && zona.estaCerca(personaje)) {
                zona.marcarActivado();
                Mensaje.mostrarMensaje(zona.getMensaje());
            }
        }
    }
}
