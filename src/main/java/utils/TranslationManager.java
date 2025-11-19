package utils;

import dao.TranslationDAO;

import java.util.Map;

public class TranslationManager {

    private static TranslationManager instance;
    private String currentLanguage;
    private Map<String, String> translations;
    private final TranslationDAO translationDAO = new TranslationDAO();

    private TranslationManager() {
        setLanguage("en");
    }

    public static synchronized TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    public void setLanguage(String language) {
        this.currentLanguage = language;
        this.translations = translationDAO.getTranslations(language);
    }

    public String getTranslation(String key) {
        return translations.getOrDefault(key, "!" + key + "!");
    }
}
