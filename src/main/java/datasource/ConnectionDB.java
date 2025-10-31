/*package datasource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mariadb://host.docker.internal:3306/StudyPlanner";
    //private static final String URL = "jdbc:mariadb://localhost:3306/studyplanner";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection obtenerConexion() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
*/
package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static Connection obtenerConexion() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String host = System.getenv("DB_HOST");
            if (host == null || host.isEmpty()) {
                host = "localhost"; // fallback
            }

            String url = "jdbc:mariadb://" + host + ":3306/StudyPlanner";
            String user = "root";
            String password = "root";

            System.out.println("Connecting to: " + url);
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established: " + (conn != null));
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return null;
    }
}
