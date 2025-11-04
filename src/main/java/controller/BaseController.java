package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseController {

    @FXML
    protected MenuButton btnLanguage;

    @FXML
    public void initialize() {
        if (btnLanguage != null) {
            btnLanguage.setText(getBundle().getString("btnLanguage.text"));
        }
    }

    protected ResourceBundle getBundle() {
        return ResourceBundle.getBundle("MessagesBundle", LanguageManager.getInstance().getCurrentLocale());
    }

    private void switchLanguage(Locale locale) {
        LanguageManager.getInstance().setCurrentLocale(locale);
        reloadScene();
    }

    private void reloadScene() {
        try {
            Scene scene = btnLanguage.getScene();
            URL fxmlUrl = (URL) scene.getRoot().getProperties().get("fxmlLoaderLocation");

            FXMLLoader loader = new FXMLLoader(fxmlUrl, getBundle());
            Parent root = loader.load();
            root.getProperties().put("fxmlLoaderLocation", fxmlUrl);

            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnglishClick(ActionEvent event) {
        switchLanguage(new Locale("en", "US"));
    }

    @FXML
    public void onPersianClick(ActionEvent event) {
        switchLanguage(new Locale("fa", "IR"));
    }

    @FXML
    public void onFinnishClick(ActionEvent event) {
        switchLanguage(new Locale("fi", "FI"));
    }

    @FXML
    public void onChineseClick(ActionEvent event) {
        switchLanguage(new Locale("zh", "CN"));
    }
}
