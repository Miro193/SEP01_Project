package controller;
import java.util.Map;
import utils.LanguageManager;

public abstract class BaseController {
    protected Map<String, String> localizedStrings;

   public void updateLanguage() {
       localizedStrings = LanguageManager.getLocalizedStrings();
   }
    public String getTranslation(String key) {
        return LanguageManager.getTranslation(key);
    }

}