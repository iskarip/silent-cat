package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//singleton garantiza UNA sola conexion a la base de datos durante toda la partida, en vez
//de abrir una nueva cada vez que algo se guarda.

public class GestorConexionBd {
    
    private static GestorConexionBd instancia;
    private Connection conexion;

private static final String URL_BD = "jdbc:sqlite:silentcat.db";

    private GestorConexionBd() { // constructor privado
        try {
            // Fuerza la carga e inicialización del driver JDBC de SQLite
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(URL_BD);
            System.out.println("Conexión con la base de datos establecida con éxito.");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de SQLite en lib: " + e.getMessage());
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

}
