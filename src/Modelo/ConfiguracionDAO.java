package Modelo;

//Sin este patron de diseño (data access object)
//mezclaria codigo del juego con codigo de sql, lo cual es mala practica
//actuara como intermediario o traductor.
   
public interface ConfiguracionDAO {
    void guardar(Configuracion configuracion);
    Configuracion cargar();

}
//quien lo use solo necesita cargar y guardar
