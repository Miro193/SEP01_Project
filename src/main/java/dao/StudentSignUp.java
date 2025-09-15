package dao;

import datasourse.ConnectionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentSignUp {
    public boolean registerStudent(int id, String username){
        try {
            Connection conn = ConnectionBD.GetConnection();
            if (conn == null) return false;
            PreparedStatement checkstmt = conn.prepareStatement(
                    "SELECT * FROM student where  id = ? or username = ?"
            );
            checkstmt.setInt(1, id);
            checkstmt.setString(2, username);
            ResultSet rs = checkstmt.executeQuery();

            if (rs.next()) {
                rs.close();
                checkstmt.close();
                conn.close();
                return false;
            }
            rs.close();
            checkstmt.close();
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO student (id, username) VALUES (?, ?)"
            );
            insertStmt.setInt(1, id);
            insertStmt.setString(2, username);

            int rowsInserted = insertStmt.executeUpdate();

            insertStmt.close();
            conn.close();

            return rowsInserted > 0;
        }catch (Exception e){
            return false;
        }
    }
}
