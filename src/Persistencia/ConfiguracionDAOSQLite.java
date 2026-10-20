package Persistencia;

import java.sql.*;

import Modelo.Configuracion;
import Modelo.ConfiguracionDAO;

public class ConfiguracionDAOSQLite implements ConfiguracionDAO{

private final Connection conexion;

    public ConfiguracionDAOSQLite(Connection conexion) {
        this.conexion = conexion;
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
    String sql = "CREATE TABLE IF NOT EXISTS configuracion (" +
                 "id INTEGER PRIMARY KEY CHECK (id = 1)," +
                 "volumen_musica REAL NOT NULL," +
                 "volumen_efectos REAL NOT NULL)";
    try (Statement st = conexion.createStatement()) {
        st.execute(sql);
    } catch (SQLException e) {
        System.out.println("No se pudo crear la tabla configuracion: " + e.getMessage());
    }
}

@Override
    public void guardar(Configuracion configuracion) {
        if (conexion == null) return; // sin base de datos no se puede guardar
        String sql = "INSERT INTO configuracion (id, volumen_musica, volumen_efectos) VALUES (1, ?, ?) " +
                     "ON CONFLICT(id) DO UPDATE SET volumen_musica = excluded.volumen_musica, " +
                     "volumen_efectos = excluded.volumen_efectos";

        synchronized (conexion) { //misma conexcion que usa progresoDAOSQLite: un hilo por vez
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setFloat(1, configuracion.getVolumenMusica());
                ps.setFloat(2, configuracion.getVolumenEfectos());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("No se pudo guardar la configuración: " + e.getMessage());
            }
        }
    }

    @Override
    public Configuracion cargar() {
        if (conexion == null) return new Configuracion(0.5f, 1.0f); // sonido default
        String sql = "SELECT volumen_musica, volumen_efectos FROM configuracion WHERE id = 1";

        synchronized (conexion) { //mismo lock que en guardar()
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return new Configuracion(rs.getFloat("volumen_musica"), rs.getFloat("volumen_efectos"));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo cargar la configuración: " + e.getMessage());
            }
        }
        return new Configuracion(0.5f, 1.0f); // valores por defecto, para la primera vez que corre el juego
    }

}