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
import java.io.IOException;


public class FirstViewController extends BaseController{

    @FXML
    private Button enterTask;
    @FXML
    private Button taskLists;
    @FXML
    private Label lblMyTasks;



    @FXML
    public void initialize() {
        updateLanguage(); //from BaseController
        languageTexts();
    }

    @FXML
    private void handleEnterTask(ActionEvent event) throws IOException {
        Parent taskRoot = FXMLLoader.load(getClass().getResource("/AddTask.fxml"));
        Scene taskScene = new Scene(taskRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskScene);
        window.show();
    }

    @FXML
    private void directToTaskLists(ActionEvent event) throws IOException {
        Parent taskListsRoot = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene taskListsScene = new Scene(taskListsRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskListsScene);
        window.show();
    }

    @FXML
    private void languageTexts() {
        lblMyTasks.setText(rb.getString("headerMyTasks"));
        enterTask.setText(rb.getString("btnEnterTask.text"));
        taskLists.setText(rb.getString("btnTaskLists.text"));


    }

}
