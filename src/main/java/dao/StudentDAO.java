package dao;

import datasourse.ConnectionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDAO {
    public boolean validateLogin(int id, String username) {
        try {
            Connection conn = ConnectionBD.GetConnection();
            if (conn == null) return false;

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM student WHERE id = ? AND username = ?"
            );
            stmt.setInt(1, id);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            boolean existe = rs.next();

            rs.close();
            stmt.close();
            conn.close();

            return existe;
        } catch (Exception e) {
            return false;
        }
    }
}
