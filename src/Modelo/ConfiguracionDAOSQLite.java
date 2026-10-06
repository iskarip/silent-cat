package Modelo;

import java.sql.*;

public class ConfiguracionDAOSQLite implements ConfiguracionDAO{

@Override
    public void guardar(Configuracion configuracion) {
        String sql = "INSERT INTO configuracion (id, volumen_musica, volumen_efectos) VALUES (1, ?, ?) " +
                     "ON CONFLICT(id) DO UPDATE SET volumen_musica = excluded.volumen_musica, " +
                     "volumen_efectos = excluded.volumen_efectos";

        Connection conexion = GestorConexionBd.getInstancia().getConexion();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setFloat(1, configuracion.getVolumenMusica());
            ps.setFloat(2, configuracion.getVolumenEfectos());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("No se pudo guardar la configuración: " + e.getMessage());
        }
    }

    @Override
    public Configuracion cargar() {
        String sql = "SELECT volumen_musica, volumen_efectos FROM configuracion WHERE id = 1";

        Connection conexion = GestorConexionBd.getInstancia().getConexion();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return new Configuracion(rs.getFloat("volumen_musica"), rs.getFloat("volumen_efectos"));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo cargar la configuración: " + e.getMessage());
        }

        return new Configuracion(0.5f, 1.0f); // valores por defecto, para la primera vez que corre el juego
    }

}