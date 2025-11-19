package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;

public class FirstViewController extends BaseController {

    @FXML private Label headerLabel;
    @FXML private Button enterTask;
    @FXML private Button taskLists;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        headerLabel.setText(tm.getTranslation("lblHeaderMyTasks"));
        enterTask.setText(tm.getTranslation("btnEnterTask"));
        taskLists.setText(tm.getTranslation("btnTaskLists"));
    }

    @FXML
    private void handleEnterTask(ActionEvent event) throws IOException {
        URL fxmlUrl = getClass().getResource("/AddTask.fxml");
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    @FXML
    private void directToTaskLists(ActionEvent event) throws IOException {
        URL fxmlUrl = getClass().getResource("/TaskList.fxml");
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }
}
