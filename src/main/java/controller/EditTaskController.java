package controller;

import dao.TaskDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Task;
import utils.LanguageManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class EditTaskController extends BaseController {

    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private Label lblEditTask;
    @FXML private Label lblTitle;
    @FXML private Label lblDescription;
    @FXML private Label lblDueDate;
    @FXML private Label lblStatus;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private TaskDao taskDao = new TaskDao();
    private Task selectedTask;

    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();
    }

    public void setTask(Task task) {
        this.selectedTask = task;
        if (task != null) {
            titleField.setText(task.getTitle());
            descField.setText(task.getDescription());
            dueDatePicker.setValue(task.getDueDate().toLocalDate());
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue(task.getStatus());
        }
    }

    @FXML
    private void handleUpdateTask(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String description = descField.getText();
        LocalDateTime dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().atStartOfDay() : null;
        String status = statusChoice.getValue();

        if (title == null || title.trim().isEmpty() || dueDate == null || status == null) {
            showAlert("Validation Error", "Title, Due Date, and Status are required fields.");
            return;
        }

        selectedTask.setTitle(title);
        selectedTask.setDescription(description);
        selectedTask.setDueDate(dueDate);
        selectedTask.setStatus(status);

        taskDao.update(selectedTask);

        showAlert("Success", "Task has been updated successfully!");
        navigateBack(event);
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigateBack(event);
    }

    private void navigateBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void languageTexts() {
        lblEditTask.setText(LanguageManager.getTranslation("lblEditTask"));
        lblTitle.setText(LanguageManager.getTranslation("lblTitle"));
        lblDescription.setText(LanguageManager.getTranslation("lblDescription"));
        lblDueDate.setText(LanguageManager.getTranslation("lblDueDate"));
        lblStatus.setText(LanguageManager.getTranslation("lblStatus"));
        btnCancel.setText(LanguageManager.getTranslation("btnCancel"));
        btnSave.setText(LanguageManager.getTranslation("btnSave"));
    }
}
