package datasourse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    private static final String URL = "jdbc:mariadb://localhost:3306/studyplanner";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection GetConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MariaDB driver not found.");
        } catch (SQLException e) {
            System.out.println("Error: Could not connect to the database. " + e.getMessage());

        }
        return null;
    }


}