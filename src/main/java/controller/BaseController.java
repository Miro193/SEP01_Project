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

public abstract class BaseController {

    @FXML
    protected MenuButton btnLanguage;

    public void initialize() {
        if (btnLanguage != null) {
            btnLanguage.setText(LanguageManager.getInstance().getTranslation("btnLanguage"));
        }
    }

    private void switchLanguage(String language) {
        LanguageManager.getInstance().setLanguage(language);
        reloadScene();
    }

    private void reloadScene() {
        try {
            URL fxmlUrl = LanguageManager.getInstance().getCurrentFxmlUrl();
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = (Stage) btnLanguage.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnglishClick(ActionEvent event) {
        switchLanguage("en");
    }

    @FXML
    public void onPersianClick(ActionEvent event) {
        switchLanguage("fa");
    }

    @FXML
    public void onFinnishClick(ActionEvent event) {
        switchLanguage("fi");
    }

    @FXML
    public void onChineseClick(ActionEvent event) {
        switchLanguage("zh");
    }
}
