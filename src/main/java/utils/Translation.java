package utils;

import datasource.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Translation {
    private Translation() {
        throw new UnsupportedOperationException("Utility class");
    }

    // idioma actual global
    private static String currentLanguage = "en";

    public static void setCurrentLanguage(String lang) {
        currentLanguage = lang;
    }

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    // versión simplificada: solo pasas la clave
    public static String getText(String translationKey) {
        return getText(translationKey, currentLanguage);
    }

    // versión completa: pasas clave + idioma
    public static String getText(String translationKey, String language) {
        String text = "[MISSING TRANSLATION]";
        String sql = "SELECT translation_value FROM translations WHERE translation_key = ? AND language = ?";
        try (Connection conn = ConnectionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, translationKey);
            stmt.setString(2, language);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    text = rs.getString("translation_value");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }
}