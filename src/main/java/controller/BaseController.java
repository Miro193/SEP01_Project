package controller;

import utils.LanguageManager;
import java.util.ResourceBundle;
//This class makes sure every controller can easily access the same bundle
public abstract class BaseController {
    protected ResourceBundle rb;

    // Load current language bundle
    public void updateLanguage() {
        rb = LanguageManager.getBundle();
    }
}