package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static Locale currentLocale = new Locale("en", "US");
    private static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

    public static void setLanguage(String language, String country) {
        currentLocale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
