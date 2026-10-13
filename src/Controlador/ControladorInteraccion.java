package Controlador;

import Modelo.Habitacion;
import Modelo.Nivel;
import Modelo.Personaje;
import Vista.ReproductorSonido;

public class ControladorInteraccion {

    private final ControladorPista controladorPista;
    private final ControladorEspacio controladorEspacio;
    private final ControladorAcertijo controladorAcertijo;
    private final ControladorTeclado controladorTeclado;
    private final Modelo.Partida partida; 

    public ControladorInteraccion(ControladorPista controladorPista, 
                                  ControladorEspacio controladorEspacio, 
                                  ControladorAcertijo controladorAcertijo,
                                  ControladorTeclado controladorTeclado,
                                  Modelo.Partida partida) {
        this.controladorPista = controladorPista;
        this.controladorEspacio = controladorEspacio;
        this.controladorAcertijo = controladorAcertijo;
        this.controladorTeclado = controladorTeclado;
        this.partida = partida;
    }

    public void manejarInteraccion() {
        Personaje personaje = partida.getPersonaje();
        Nivel nivel = partida.getNivelActual();

        // 1. Si hay una pista abierta, la E sirve para cerrarla
        if (controladorPista.estaActivo()) {
            controladorPista.cerrar();
            return; 
        }

        // 2. Si está en una habitación, verifica si sale
       if (controladorEspacio.estaEnHabitacion()) {
            Habitacion actual = partida.getHabitacionActiva();
            if (actual.salidaAlAlcance(personaje)) {
                ReproductorSonido.reproducir("Recursos/Sonidos/Objetos/puerta.wav");
                controladorEspacio.salir();
                controladorTeclado.limpiarTeclas();
            }
            return;
        }
        
        // 3. Si está en el nivel, busca entrar a habitación o interactuar con acertijo
       Habitacion habitacion = nivel.getHabitacionAlAlcance(personaje);
        if (habitacion != null) {
            ReproductorSonido.reproducir("Recursos/Sonidos/Objetos/puerta.wav");
            controladorEspacio.entrar(habitacion);
            controladorTeclado.limpiarTeclas();
        } else {
            controladorAcertijo.intentarInteraccion(nivel, personaje, controladorTeclado);
        }
    }
}