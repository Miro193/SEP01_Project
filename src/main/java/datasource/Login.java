package datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print(" write your ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        System.out.print("write your username: ");
        String username = scanner.nextLine();

        try (Connection conexion = ConnectionDB.obtenerConexion()) {
            String sql = "SELECT * FROM student WHERE id = ? AND username = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"), rs.getString("username"));
                System.out.println(" welcome, " + user.getName());
            } else {
                System.out.println(" ID or username is incorect .");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
