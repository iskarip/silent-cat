package Modelo;

public abstract class EntidadPasiva extends Entidad {
   
//nuestro gato se hace presente recien el el ultimo nivel
//la idea es que camine hacia el personaje con una ia parecida a la
//de los enemigos. 
 public abstract void moverse(Personaje objetivo, MapaColision mapa);

 
}
