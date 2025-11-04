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
import model.CurrentUser;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskController extends BaseController {

    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    private final TaskDao taskDao = new TaskDao();
    private ObservableList<Task> taskListObservable;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        if (taskTable != null && CurrentUser.get() != null) {
            List<Task> tasks = taskDao.getTasksByUserId(CurrentUser.get().getId());
            taskListObservable = FXCollections.observableArrayList(tasks);

            titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
            descColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
            dueDateColumn.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDateTime dueDate = cellData.getValue().getDueDate();
                return new SimpleStringProperty(dueDate != null ? dueDate.format(formatter) : "");
            });
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            taskTable.setItems(taskListObservable);
        }

        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue("TODO");
        }
    }

    @FXML
    private void handleAddTask(ActionEvent event) throws IOException {
        navigate(event, "/AddTask.fxml");
    }

    @FXML
    private void handleEditTask(ActionEvent event) throws IOException {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("No Task Selected", "Please select a task in the table to edit.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditTask.fxml"), getBundle());
        Parent root = loader.load();
        root.getProperties().put("fxmlLoaderLocation", getClass().getResource("/EditTask.fxml"));

        EditTaskController controller = loader.getController();
        controller.setTask(selectedTask);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskDao.delete(selectedTask);
            taskListObservable.remove(selectedTask);
        } else {
            showAlert("No task selected", "Please select a task to delete.");
        }
    }

    @FXML
    private void handleOpenCalendar(ActionEvent event) throws IOException {
        navigate(event, "/Calendar.fxml");
    }

    @FXML
    private void handleOpenDoneTasks(ActionEvent event) throws IOException {
        navigate(event, "/DoneTask.fxml");
    }

    @FXML
    private void handleSaveTask(ActionEvent event) throws IOException {
        String title = titleField.getText();
        String description = descField.getText();
        LocalDateTime dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().atStartOfDay() : null;
        String status = statusChoice.getValue();

        if (title == null || title.trim().isEmpty() || dueDate == null || status == null) {
            showAlert("Validation Error", "Title, Due Date, and Status are required fields.");
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

        showAlert("Success", "Task has been saved successfully!");
        navigate(event, "/TaskList.fxml");
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigate(event, "/TaskList.fxml");
    }

    private void navigate(ActionEvent event, String fxmlPath) throws IOException {
        URL fxmlUrl = getClass().getResource(fxmlPath);
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
