package dao;

import datasource.ConnectionDB;

import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
// connect to database for take
public class LocalizationDao {

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
            System.out.println("✅ Loaded localization strings for language: " + lang );
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: ");
            e.printStackTrace();
        }

        return strings;
    }
}
