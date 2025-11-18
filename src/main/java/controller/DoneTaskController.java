package controller;
import dao.TaskDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;
import utils.LanguageManager;

public class DoneTaskController extends BaseController {
    @FXML private Button btnBackToTasks;
    @FXML private Label lblDoneTasks;

    @FXML private ListView<String> doneList;


    private final TaskDao taskDAO = new TaskDao();

    @FXML
    private void backToList(ActionEvent event) throws IOException {
        Parent taskListsRoot = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene taskListsScene = new Scene(taskListsRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskListsScene);
        window.show();
    }
    @FXML
    public void initialize() {
        loadDoneTasks();
        updateLanguage();
        languageTexts();

    }

    private void loadDoneTasks() {
        if (CurrentUser.get() == null) {
            return;
        }

        List<Task> tasks = taskDAO.getTasksByUserId(CurrentUser.get().getId());
        for (Task task : tasks) {
            if ("Done".equalsIgnoreCase(task.getStatus())) {
                doneList.getItems().add(task.getTitle());
            }
        }
    }
    @FXML
    private void languageTexts() {
        btnBackToTasks.setText(LanguageManager.getTranslation("btnBackToTasks"));
        lblDoneTasks.setText(LanguageManager.getTranslation("lblDoneTasks"));
    }
}