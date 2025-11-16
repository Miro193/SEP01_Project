package dao;

import datasource.ConnectionDB;
import model.Task;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private Connection testConn;

    public TaskDao() {
        this.testConn = null;
    }

    public TaskDao(Connection conn) {
        this.testConn = conn;
    }

    private Connection getConnection() throws SQLException {
        Connection conn = (testConn != null) ? testConn : ConnectionDB.obtenerConexion();
        if (conn == null) {
            throw new SQLException("Failed to establish database connection");
        }
        return conn;
    }


    public void persist(Task task) {
        String sql = "INSERT INTO task (user_id, title, description, status, dueDate, language) VALUES (?, ?, ?, ?, ?, ?)";
//        (Connection conn = ConnectionDB.obtenerConexion();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(task.getDueDate()));
            stmt.setString(6, task.getLanguage());
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
        String sql = "SELECT * FROM task WHERE task_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

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

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
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

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

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

    public List<Task> getTasksByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE user_id = ? AND dueDate BETWEEN ? AND ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));

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
        String sql = "UPDATE task SET title = ?, description = ?, status = ?, dueDate = ? WHERE task_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

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
        String sql = "DELETE FROM task WHERE task_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, task.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("task_id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
       // task.setLanguage(rs.getString("language"));

        Timestamp ts = rs.getTimestamp("dueDate");
        if (ts != null) {
            task.setDueDate(ts.toLocalDateTime());
        }

        return task;
    }

}