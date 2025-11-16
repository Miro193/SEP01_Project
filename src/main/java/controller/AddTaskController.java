package controller;

import dao.TaskDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.CurrentUser;
import model.Task;
import utils.LanguageManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddTaskController extends BaseController {
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private  Label lblAddTask;
    @FXML private  Label lblTitle;
    @FXML private  Label lblDescription;
    @FXML private  Label lblDueDate;
    @FXML private  Label lblStatus;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private TaskDao taskDao = new TaskDao();


    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();

        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO","IN_PROGRESS","DONE"));
            statusChoice.setValue("TODO");
        }
    }

    @FXML
    private void handleSaveTask(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String description = descField.getText();
        LocalDateTime dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().atStartOfDay() : null;
        String status = statusChoice.getValue();
        String language = LanguageManager.getCurrentLocale() != null
                ? LanguageManager.getCurrentLocale().getLanguage()
                : "en";

        if (title == null || title.trim().isEmpty() || dueDate == null || status == null) {
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("error.validation"));
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
        newTask.setLanguage(language);

        taskDao.persist(newTask);

       // showAlert("Success", "Task has been Added successfully!");
        showAlert(LanguageManager.getTranslation("success.title"), LanguageManager.getTranslation("success.taskAddmessage"));
        navigateTo(event, "/TaskList.fxml");
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigateTo(event, "/TaskList.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void languageTexts() {
        lblAddTask.setText(LanguageManager.getTranslation("lblAddTask"));
        lblTitle.setText(LanguageManager.getTranslation("lblTitle"));
        btnCancel.setText(LanguageManager.getTranslation("btnCancel"));
        btnSave.setText(LanguageManager.getTranslation("btnSave"));
        lblDescription.setText(LanguageManager.getTranslation("lblDescription"));
        lblDueDate.setText(LanguageManager.getTranslation("lblDueDate"));
        lblStatus.setText(LanguageManager.getTranslation("lblStatus"));


    }
}



