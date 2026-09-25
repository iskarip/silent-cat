package Controlador;

import Modelo.Personaje;
import Modelo.Nivel;
import Modelo.Item;
import java.awt.Rectangle;
import java.util.List;

//esta clase es necesaria ya que ningun controlador revisaba la cercania del personaje
//sobre los items que vamos a implementar bateria, medicina


public class ControladorItems {
    
    //esto seria que tan cerca hay que estar del item
    private static final int RADIO_RECOGIDA = 20;

    public void actualizar(Nivel nivelActual, Personaje personaje){
        if (nivelActual == null || personaje == null)
            return;
    
    List<Item> items = nivelActual.getListaItems();
        if (items == null) 
            return;
//use personajeHitbox que ya lo usamos tambien en controlador: combate y enemigos.
    Rectangle hitboxJugador = personaje.getHitbox();

    for (Item item : items){
        if (item.isRecogido()) continue; //si lo agarra ya esta
    
        Rectangle hitboxItem = new Rectangle
        (item.getPosicionX() - RADIO_RECOGIDA, item.getPosicionY() - RADIO_RECOGIDA, RADIO_RECOGIDA * 2, RADIO_RECOGIDA  * 2);
    
        if(hitboxJugador.intersects(hitboxItem)){
            personaje.interactuarCon(item); // aca aplicamos polimorfismo porque el objeto decide que hacer
        }
    
    }


}

















}
