package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FirstViewController {

    @FXML
    private Button enterTask;

    @FXML
    private Button taskLists;

    private ResourceBundle rb;

    @FXML
    public void initialize() {

        handleLanguage("en", "US");
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
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("MessagesBundle", locale);
        //set texts
        enterTask.setText(rb.getString("btnEnterTask.text"));
        taskLists.setText(rb.getString("btnTaskLists.text"));

    }
}
