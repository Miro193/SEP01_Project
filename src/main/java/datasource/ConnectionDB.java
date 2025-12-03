package datasource;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final Log log = LogFactory.getLog(ConnectionDB.class);

    private ConnectionDB() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection obtenerConexion() {
        try {
            Dotenv dotenv = Dotenv.load();
            String host = dotenv.get("DB_HOST");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            if (host == null || user == null || password == null) {
                throw new IllegalStateException("Missing DB credentials in .env");
            }

            String url = String.format("jdbc:mariadb://%s:3306/Studyplanner", host);

            if (log.isInfoEnabled()) {
                log.info("Connecting to " + url);
            }

            Connection conn = DriverManager.getConnection(url, user, password);

            if (log.isInfoEnabled()) {
                log.info("Connection established: " + (conn != null));
            }
            return conn;
        } catch (SQLException e) {
            log.error("Connection error: " + e.getMessage(), e);
        }
        return null;
    }
}
