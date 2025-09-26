package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Task;
import model.TaskList;
import dao.TaskDao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskController {

    private final TaskList taskList;

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    private ObservableList<Task> taskListObservable;
    private TaskList taskList;

    @FXML
    public void initialize() {
        taskList = new TaskList(new TaskDao()); // Wraps DAO
        taskListObservable = FXCollections.observableArrayList(taskList.getAllTasks());

        titleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        descColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        dueDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getDueDate().format(formatter));
        });
        statusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        taskTable.setItems(taskListObservable);
    }

    @FXML
    private void handleAddTask() {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("Description");
        newTask.setStatus("Not Started");
        newTask.setDueDate(LocalDateTime.now().plusDays(1));

        taskList.addTask(newTask);
        taskListObservable.add(newTask);   // Update UI
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.deleteTask(selectedTask);     // Remove from DB and cache
            taskListObservable.remove(selectedTask); // Update UI
        } else {
            showAlert("No task selected", "Please select a task to delete.");
        }
    }

    @FXML
    private void handleOpenCalendar() {
        System.out.println("Opening Calendar View...");
        // TODO: Load Calendar.fxml or switch scene
    }

    @FXML
    private void handleOpenDoneTasks() {
        System.out.println("Opening Done Tasks View...");
        // TODO: Load DoneTask.fxml or switch scene
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

