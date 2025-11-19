package dao;

import datasource.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TranslationDAO {

    public Map<String, String> getTranslations(String language) {
        Map<String, String> translations = new HashMap<>();
        String sql = "SELECT translation_key, translation_value FROM translations WHERE language = ?";

        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, language);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                translations.put(rs.getString("translation_key"), rs.getString("translation_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return translations;
    }
}
