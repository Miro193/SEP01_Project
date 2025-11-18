package dao;

import datasource.ConnectionDB;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/*
connect to database for take
 */
public class LocalizationDao {
    Logger log = Logger.getLogger(LocalizationDao.class.getName());

    public Map<String, String> getLocalizedStrings(Locale locale) {
        Map<String, String> strings = new HashMap<>();
        String lang = locale.getLanguage();

        try (Connection conn = ConnectionDB.obtenerConexion()) {
            String query = "SELECT translation_key, translation_value FROM translations WHERE language = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, lang);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    strings.put(rs.getString("translation_key"), rs.getString("translation_value"));
                }
            }
        } catch (SQLException e) {
            log.severe("❌ Database connection failed: " + e.getMessage());
        }
        return strings;
    }
}
