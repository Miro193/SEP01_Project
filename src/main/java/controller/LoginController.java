package controller;

import datasource.ConnectionDB;
import model.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    // Verify login credentials
    public boolean authenticate(Login login) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login.getUsername());
            stmt.setString(2, login.getPassword());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
