package dao;
import datasource.ConnectionDB;

import java.sql.*;
import model.User;
public class UserDao {
    private Connection testConn;

    public UserDao() {
        this.testConn = null;
    }

    public UserDao(Connection conn) {
        this.testConn = conn;
    }

    private Connection getConnection() throws SQLException {
        if (testConn != null) {
            return testConn;
        }
        return ConnectionDB.obtenerConexion();
    }

    public void register(User user) {
        String sql = "INSERT INTO users (username, password, confirmPassword) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getConfirmPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
