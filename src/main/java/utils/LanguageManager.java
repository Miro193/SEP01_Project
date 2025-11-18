package utils;

import java.util.Locale;
import dao.LocalizationDao;
import java.util.Map;
import java.util.logging.Logger;

public class LanguageManager {
    private static Logger log = Logger.getLogger(LanguageManager.class.getName());

    private static Locale currentLocale = new Locale("en", "US");
    private static Map<String, String> localizedStrings;
    private static LocalizationDao localizationDao = new LocalizationDao();

    public static void setLanguage(String language, String country) {
        currentLocale = new Locale(language, country);
        localizedStrings = localizationDao.getLocalizedStrings(currentLocale);
    }

   public static Map<String, String> getLocalizedStrings() {
       return localizedStrings;
   }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
    public static String getTranslation(String key) {
        if (localizedStrings == null) {
            setLanguage("en", "US");
        }
        if (!localizedStrings.containsKey(key)) {
            log.info("Translation key" + key + " not found for language: " + currentLocale.getLanguage());

            return "TRANSLATION_MISSING";
        }

        return localizedStrings.get(key);
    }
}