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
import model.CurrentUser;
import model.Task;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

public class AddTaskController extends BaseController {

    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private Label lblAddTask;
    @FXML private Label lblTitle;
    @FXML private Label lblDescription;
    @FXML private Label lblDueDate;
    @FXML private Label lblStatus;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private final TaskDao taskDao = new TaskDao();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        lblAddTask.setText(tm.getTranslation("lblAddTask"));
        lblTitle.setText(tm.getTranslation("lblTitle"));
        lblDescription.setText(tm.getTranslation("lblDescription"));
        lblDueDate.setText(tm.getTranslation("lblDueDate"));
        lblStatus.setText(tm.getTranslation("lblStatus"));
        btnCancel.setText(tm.getTranslation("btnCancel"));
        btnSave.setText(tm.getTranslation("btnSave"));

        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue("TODO");
        }
    }

    @FXML
    private void handleSaveTask(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String description = descField.getText();
        LocalDateTime dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().atStartOfDay() : null;
        String status = statusChoice.getValue();

        if (title == null || title.trim().isEmpty() || dueDate == null || status == null) {
            showAlert("Error", "Title, Due Date, and Status are required fields.");
            return;
        }

        if (CurrentUser.get() == null) {
            showAlert("Authentication Error", "No user is logged in. Please log in to save a task.");
            return;
        }

        Task newTask = new Task();
        newTask.setUserId(CurrentUser.get().getId());
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setStatus(status);

        taskDao.persist(newTask);

        showAlert("Success", "Task has been added successfully!");
        navigateTo(event, "/TaskList.fxml");
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigateTo(event, "/TaskList.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        URL fxmlUrl = getClass().getResource(fxmlPath);
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
