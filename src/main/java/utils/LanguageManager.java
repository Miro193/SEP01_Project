package utils;

import java.util.HashMap;
import java.util.Locale;
//import java.util.ResourceBundle;
import dao.LocalizationDao;
import java.util.Map;

public class LanguageManager {

    private static Locale currentLocale = new Locale("en", "US");
   // private static ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
    private static Map<String, String> localizedStrings;
    private static LocalizationDao localizationDao = new LocalizationDao();

    public static void setLanguage(String language, String country) {
        currentLocale = new Locale(language, country);
        //bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        localizedStrings = localizationDao.getLocalizedStrings(currentLocale);

    }

   /* public static ResourceBundle getBundle() {
        return bundle;
    }*/
   public static Map<String, String> getLocalizedStrings() {
       return localizedStrings;
   }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}