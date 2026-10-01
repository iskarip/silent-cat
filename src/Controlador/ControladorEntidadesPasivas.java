package Controlador;

import Modelo.EntidadPasiva;
import Modelo.Nivel;
import Modelo.Personaje;

import java.util.List;

//Va de la mano a controladorenemigos pero este seria para aquellas entidades que no pelean.
//Cada entidad pasiva, por ahora gato, que agreguemos decide su propia logica de movimiento via moverse():

//aplique polimorfismo ya que el controlador no va a preguntar de que tipo es cada una, sino que
//le pediria a todas por igual.

public class ControladorEntidadesPasivas {
    
    public void actualizar(Nivel nivel, Personaje personaje) {
        if (nivel == null || personaje == null) return;
 
        List<EntidadPasiva> entidades = nivel.getListaEntidadesPasivas();
        if (entidades == null || entidades.isEmpty()) return;
 
        for (EntidadPasiva entidad : entidades) {
            entidad.moverse(personaje, nivel.getMapaColision());
        }
    }
    
}
