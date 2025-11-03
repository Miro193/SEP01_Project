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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class EditTaskController {

    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;

    // New FXML fields for localization
    @FXML private Label headerEditTask;
    @FXML private Label labelTitle;
    @FXML private Label labelDescription;
    @FXML private Label labelDueDate;
    @FXML private Label labelStatus;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private TaskDao taskDao = new TaskDao();
    private Task selectedTask;
    private ResourceBundle rb;

    @FXML
    public void initialize() {
        // Set default language
        handleLanguage("en", "US");
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

    // --- Localization Methods ---

    @FXML
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("messages", locale);
        headerEditTask.setText(rb.getString("editTask.header"));
        labelTitle.setText(rb.getString("editTask.title"));
        labelDescription.setText(rb.getString("editTask.description"));
        labelDueDate.setText(rb.getString("editTask.dueDate"));
        labelStatus.setText(rb.getString("editTask.status"));
        btnCancel.setText(rb.getString("editTask.cancel"));
        btnSave.setText(rb.getString("editTask.save"));
        btnLanguage.setText(rb.getString("btnLanguage.text"));
        itemEnglish.setText(rb.getString("itemEnglish.text"));
        itemPersian.setText(rb.getString("itemPersian.text"));
        itemFinnish.setText(rb.getString("itemFinnish.text"));
        itemChinese.setText(rb.getString("itemChinese.text"));
    }

    public void onEnglishClick(ActionEvent event) {
        handleLanguage("en", "US");
    }

    public void onPersianClick(ActionEvent event) {
        handleLanguage("fa", "IR");
    }

    public void onFinnishClick(ActionEvent event) {
        handleLanguage("fi", "FI");
    }

    public void onChineseClick(ActionEvent event) { handleLanguage("zh", "CN"); }
}
