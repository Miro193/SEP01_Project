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
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskController extends BaseController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private Button btnBack;
    @FXML private Button btnAddTask;
    @FXML private Button btnDeleteTask;
    @FXML private Button btnEditTask;
    @FXML private Button btnCalendarView;
    @FXML private Button btnDoneTasks;

    private final TaskDao taskDao = new TaskDao();
    private ObservableList<Task> taskListObservable;

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        btnBack.setText(tm.getTranslation("btnBack"));
        btnAddTask.setText(tm.getTranslation("btnAddTask"));
        btnDeleteTask.setText(tm.getTranslation("btnDeleteTask"));
        btnEditTask.setText(tm.getTranslation("btnEditTask"));
        btnCalendarView.setText(tm.getTranslation("btnCalendarView"));
        btnDoneTasks.setText(tm.getTranslation("btnDoneTasks"));
        titleColumn.setText(tm.getTranslation("titleColumn"));
        descColumn.setText(tm.getTranslation("descColumn"));
        dueDateColumn.setText(tm.getTranslation("dueDateColumn"));
        statusColumn.setText(tm.getTranslation("statusColumn"));

        if (CurrentUser.get() != null) {
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
    }

    @FXML
    private void handleBackToFirstView(ActionEvent event) throws IOException {
        navigate(event, "/first_view.fxml");
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

        URL fxmlUrl = getClass().getResource("/EditTask.fxml");
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

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

    private void navigate(ActionEvent event, String fxmlPath) throws IOException {
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
