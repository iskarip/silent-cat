import java.sql.Connection;

import javax.swing.SwingUtilities;

import Controlador.ControladorPrincipal;
import Modelo.ConfiguracionDAO;
import Modelo.ProgresoDAO;
import Persistencia.ConfiguracionDAOMemoria;
import Persistencia.ConfiguracionDAOSQLite;
import Persistencia.GestorConexionBd;
import Persistencia.ProgresoDAOMemoria;
import Persistencia.ProgresoDAOSQLite;
import Vista.JuegoFrame; 

public class Main {
    public static void main (String [] args) {

        // Activa la aceleración nativa por GPU de Windows
        System.setProperty("sun.java2d." +
                "d3d", "true");
        System.setProperty("sun.java2d.ddforcevram", "true");
        System.setProperty("sun.java2d.render", "speed");

    //crea el objeto JuegoFrame y como su constructor ya hace setVisible(true),
    //la ventana del juego se abre automatico.

    Connection conexion = GestorConexionBd.getInstancia().getConexion();

    ConfiguracionDAO configuracionDAO;
    ProgresoDAO progresoDAO;
    if (conexion != null) {
        configuracionDAO = new ConfiguracionDAOSQLite(conexion);
        progresoDAO = new ProgresoDAOSQLite(conexion);
    } else {
            // sin base de datos el juego igual funciona, pero no guarda nada al cerrar
        configuracionDAO = new ConfiguracionDAOMemoria();
        progresoDAO = new ProgresoDAOMemoria();
    }

    SwingUtilities.invokeLater(() -> {
        JuegoFrame ventana = new JuegoFrame();
        new ControladorPrincipal(ventana, configuracionDAO, progresoDAO);
    });

    }
}