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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ConfiguracionDAO configuracionDAO;
            ProgresoDAO progresoDAO;

            Connection conexion = GestorConexionBd.getInstancia().getConexion();

            if (conexion != null) {
                configuracionDAO = new ConfiguracionDAOSQLite(conexion);
                progresoDAO = new ProgresoDAOSQLite(conexion);
            } else {
                System.out.println("Base de datos no disponible: usando almacenamiento en memoria");
                configuracionDAO = new ConfiguracionDAOMemoria();
                progresoDAO = new ProgresoDAOMemoria();
            }

            JuegoFrame ventana = new JuegoFrame();
            new ControladorPrincipal(ventana, configuracionDAO, progresoDAO);
        });
    }

    // crea el objeto JuegoFrame y como su constructor ya hace setVisible(true),
    // la ventana del juego se abre automatico.
}
