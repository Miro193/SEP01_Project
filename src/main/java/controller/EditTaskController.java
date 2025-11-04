package controller;

import dao.TaskDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

public class EditTaskController extends BaseController {

    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;

    private final TaskDao taskDao = new TaskDao();
    private Task selectedTask;

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
        URL fxmlUrl = getClass().getResource("/TaskList.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, getBundle());
        Parent root = loader.load();
        root.getProperties().put("fxmlLoaderLocation", fxmlUrl);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
