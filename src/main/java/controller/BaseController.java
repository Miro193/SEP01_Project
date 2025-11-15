package controller;
import java.util.Map;
import utils.LanguageManager;
import java.util.ResourceBundle;
//This class makes sure every controller can easily access the same bundle
public abstract class BaseController {
    //protected ResourceBundle rb;
    protected Map<String, String> localizedStrings;

   /* // Load current language bundle
    public void updateLanguage() {
        rb = LanguageManager.getBundle();
    }*/
   // Load current language strings from database
   public void updateLanguage() {
       localizedStrings = LanguageManager.getLocalizedStrings();
   }


}