package Persistencia;

import Modelo.Configuracion;
import Modelo.ConfiguracionDAO;

public class ConfiguracionDAOMemoria implements ConfiguracionDAO {

    private Configuracion configuracion = new Configuracion(0.5f, 1.0f);

    @Override 
    public void guardar(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    @Override 
    public Configuracion cargar() {
        return configuracion;
    }
    
}
