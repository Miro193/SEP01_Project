package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstViewController extends BaseController {

    @FXML
    private void handleEnterTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTask.fxml"), getBundle());
        Parent root = loader.load();
        root.getProperties().put("fxmlLoaderLocation", getClass().getResource("/AddTask.fxml"));

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    @FXML
    private void directToTaskLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TaskList.fxml"), getBundle());
        Parent root = loader.load();
        root.getProperties().put("fxmlLoaderLocation", getClass().getResource("/TaskList.fxml"));

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }
}
