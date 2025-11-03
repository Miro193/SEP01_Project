package controller;

import utils.LanguageManager;
import java.util.ResourceBundle;
public abstract class BaseController {
    protected ResourceBundle rb;

    public void updateLanguage() {
        rb = LanguageManager.getBundle();
    }
}
