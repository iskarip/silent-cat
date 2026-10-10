package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//singleton garantiza UNA sola conexion a la base de datos durante toda la partida, en vez
//de abrir una nueva cada vez que algo se guarda.

public class GestorConexionBd {
    
    private static GestorConexionBd instancia;
    private Connection conexion;

private static final String URL_BD = "jdbc:sqlite:silentcat.db";

    private GestorConexionBd() { // constructor privado
        try {
            conexion = DriverManager.getConnection(URL_BD);
            crearTablasSiNoExisten();
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la base de datos: " + e.getMessage());
        }
    }

    public static GestorConexionBd getInstancia() {
        if (instancia == null) {
            instancia = new GestorConexionBd();
        }
        return instancia;
    }

public Connection getConexion() {
        return conexion;
    }

    private void crearTablasSiNoExisten() {
        String sql = "CREATE TABLE IF NOT EXISTS configuracion (" +
                     "id INTEGER PRIMARY KEY CHECK (id = 1)," + // obliga a que exista UNA sola fila
                     "volumen_musica REAL NOT NULL," +
                     "volumen_efectos REAL NOT NULL)";
        try (Statement st = conexion.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("No se pudo crear la tabla configuracion: " + e.getMessage());
        }
    }
}
