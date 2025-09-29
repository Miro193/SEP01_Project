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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskController {
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    private TaskDao taskDao = new TaskDao();
    private ObservableList<Task> taskListObservable;
    private static Task taskToEdit;


    public static void setTaskToEdit(Task task) {
        taskToEdit = task;
    }

    @FXML
    public void initialize() {
        if (taskTable != null) {
            List<Task> tasks = taskDao.getTasksByUserId(CurrentUser.get().getId());
            taskListObservable = FXCollections.observableArrayList(tasks);

            titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
            descColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
            dueDateColumn.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                return new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter));
            });
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            taskTable.setItems(taskListObservable);
        }


        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue("TODO");
        }

        if (taskToEdit != null) {
            titleField.setText(taskToEdit.getTitle());
            descField.setText(taskToEdit.getDescription());
            dueDatePicker.setValue(taskToEdit.getDueDate().toLocalDate());
            statusChoice.setValue(taskToEdit.getStatus());
        }
    }


    @FXML
    private void handleAddTask(ActionEvent event) throws IOException {
        navigate(event, "/AddTask.fxml");
    }

    @FXML
    private void handleEditTask(ActionEvent event) throws IOException {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            setTaskToEdit(selectedTask);
            navigate(event, "/EditTask.fxml");
        } else {
            showAlert("No task selected", "Please select a task to edit.");
        }
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
        showAlert("Not Implemented", "Calendar view is not yet implemented.");
    }

    @FXML
    private void handleOpenDoneTasks(ActionEvent event) throws IOException {
        navigate(event, "/DoneTask.fxml");
    }


    @FXML
    private void handleSaveTask(ActionEvent event) throws IOException {
        if (CurrentUser.get() == null) {
            showAlert("Error", "No user logged in. Cannot save task.");
            return;
        }
        if (taskToEdit != null) {
            taskToEdit.setTitle(titleField.getText());
            taskToEdit.setDescription(descField.getText());
            taskToEdit.setDueDate(dueDatePicker.getValue().atStartOfDay());
            taskToEdit.setStatus(statusChoice.getValue());

            taskDao.update(taskToEdit);
            taskToEdit = null;
        } else {
            Task newTask = new Task();
            newTask.setUserId(CurrentUser.get().getId());
            newTask.setTitle(titleField.getText());
            newTask.setDescription(descField.getText());
            newTask.setDueDate(dueDatePicker.getValue().atStartOfDay());
            newTask.setStatus(statusChoice.getValue());

            taskDao.persist(newTask);
        }

        navigate(event, "/TaskList.fxml");
    }




    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigate(event, "/TaskList.fxml");
    }


    private void navigate(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
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
}
