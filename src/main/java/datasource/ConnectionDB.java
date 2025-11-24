package datasource;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionDB {
    private static final Log log = LogFactory.getLog(ConnectionDB.class);
    //lisatu contructor private
    private ConnectionDB() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static Connection obtenerConexion() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            // hankin tiedosto .env
            Dotenv dotenv = Dotenv.load();
            String host = dotenv.get("DB_HOST", "localhost");   // fallback
            String user = dotenv.get("DB_USER", "root");        // fallback
            String password = dotenv.get("DB_PASSWORD", "");    // fallback

            String url = String.format("jdbc:mariadb://%s:3306/Studyplanner", host);
            log.info(String.format("Connecting to %s", url));

            Connection conn = DriverManager.getConnection(url, user, password);
            log.info("Connection established: " + (conn != null));
            return conn;
        } catch (ClassNotFoundException e) {
            log.error("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            log.error("Connection error: " + e.getMessage());
        }
        return null;



    }
}
