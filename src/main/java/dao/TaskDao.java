package dao;

import datasource.ConnectionDB;
import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public void persist(Task task) {
        String sql = "INSERT INTO task (user_id, title, description, status, due_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(task.getDueDate()));

            stmt.executeUpdate();


            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Task find(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task";

        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE user_id = ?";

        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public void update(Task task) {
        String sql = "UPDATE task SET title = ?, description = ?, status = ?, due_date = ? WHERE id = ?";
        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getStatus());
            stmt.setTimestamp(4, Timestamp.valueOf(task.getDueDate()));
            stmt.setInt(5, task.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(Task task) {
        String sql = "DELETE FROM task WHERE id = ?";
        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, task.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("due_date");
        if (ts != null) {
            task.setDueDate(ts.toLocalDateTime());
        }

        return task;
    }

}
