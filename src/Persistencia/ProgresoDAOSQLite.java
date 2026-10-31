package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Modelo.Progreso;
import Modelo.ProgresoDAO;

public class ProgresoDAOSQLite implements ProgresoDAO {

    private final Connection conexion; // la recibe, no la crea ni la cierra

    public ProgresoDAOSQLite(Connection conexion) {
        this.conexion = conexion;
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS progreso ("
                + "id INTEGER PRIMARY KEY CHECK (id = 1), " // una sola partida guardada
                + "genero TEXT NOT NULL, "
                + "nivel INTEGER NOT NULL, "
                + "checkpoint_x INTEGER NOT NULL, "
                + "checkpoint_y INTEGER NOT NULL, "
                + "vida INTEGER NOT NULL, "
                + "bateria INTEGER NOT NULL, "
                + "items_inventario TEXT, "
                + "habitacion TEXT)";
        try (Statement st = conexion.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("No se pudo crear la tabla progreso: " + e.getMessage());
        }
    }

    @Override
    public boolean guardar(Progreso progreso) {
        if (progreso == null)
            return false;
        String sql = "INSERT INTO progreso (id, genero, nivel, checkpoint_x, checkpoint_y, vida, bateria, items_inventario, habitacion) "
                + "VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(id) DO UPDATE SET genero = excluded.genero, nivel = excluded.nivel, "
                + "checkpoint_x = excluded.checkpoint_x, checkpoint_y = excluded.checkpoint_y, "
                + "vida = excluded.vida, bateria = excluded.bateria, "
                + "items_inventario = excluded.items_inventario, habitacion = excluded.habitacion";

        // la connection la comparten los Dao y ahora la usan distintos hilos tambien
        // una sola operacion a la vez
        synchronized (conexion) {
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, progreso.getGenero());
                ps.setInt(2, progreso.getNumeroNivel());
                ps.setInt(3, progreso.getCheckpointX());
                ps.setInt(4, progreso.getCheckpointY());
                ps.setInt(5, progreso.getVida());
                ps.setInt(6, progreso.getBateria());
                ps.setString(7, progreso.getItemsInventario());
                ps.setString(8, progreso.getHabitacion());
                return ps.executeUpdate() == 1;
            } catch (SQLException e) {
                System.out.println("No se pudo guardar la partida: " + e.getMessage());
                return false;
            }
        }
    }

    @Override
    public Progreso cargar() {
        String sql = "SELECT genero, nivel, checkpoint_x, checkpoint_y, vida, bateria, items_inventario, habitacion "
                + "FROM progreso WHERE id = 1";

        synchronized (conexion) { // mismo lock que en guardar()
            try (PreparedStatement ps = conexion.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Progreso(
                            rs.getString("genero"),
                            rs.getInt("nivel"),
                            rs.getInt("checkpoint_x"),
                            rs.getInt("checkpoint_y"),
                            rs.getInt("vida"),
                            rs.getInt("bateria"),
                            rs.getString("items_inventario"),
                            rs.getString("habitacion")
                    );
                }
            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("No se pudo cargar la partida: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean hayProgreso() {
        return cargar() != null;
    }

}