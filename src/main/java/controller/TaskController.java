package controller;
import utils.LanguageManager;
import dao.TaskDao;
import java.io.IOException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskController extends BaseController {

    // Fields for AddTask.fxml
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ChoiceBox<String> statusChoice;

    @FXML private Button btnAddTask;
    @FXML private Button btnDeleteTask;
    @FXML private Button btnEditTask;
    @FXML private Button btnCalendarView;
    @FXML private Button btnDoneTasks;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;
    @FXML private Label lblAddTask;
    @FXML private Label lblAddTitle;
    @FXML private Label lblAddDescription;
    @FXML private Label lblAddDueDate;
    @FXML private Label lblAddStatus;


    // Fields for TaskList.fxml
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> languageColumn;

    private TaskDao taskDao = new TaskDao();
    private ObservableList<Task> taskListObservable;

    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();
        if (languageColumn != null) {
            languageColumn.setVisible(false);
        }


        // Initialization logic for TaskList.fxml
        if (taskTable != null && CurrentUser.get() != null) {
            List<Task> tasks = taskDao.getTasksByUserId(CurrentUser.get().getUserID());
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

        // Initialization logic for AddTask.fxml
        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue("TODO");
        }
    }

    // Methods for TaskList.fxml
    @FXML
    private void handleAddTask(ActionEvent event) throws IOException {
        navigate(event, "/AddTask.fxml");
    }

    @FXML
    private void handleEditTask(ActionEvent event) throws IOException {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            LanguageManager.getTranslation("error.noTaskMessage");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditTask.fxml"));
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
            showAlert("error.deleteMessage", "error.taskSelectMessage");
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

    @FXML
    private void languageTexts() {
        btnAddTask.setText(LanguageManager.getTranslation("btnAddTask"));
        btnDeleteTask.setText(LanguageManager.getTranslation("btnDeleteTask"));
        btnEditTask.setText(LanguageManager.getTranslation("btnEditTask"));
        btnCalendarView.setText(LanguageManager.getTranslation("btnCalendarView"));
        btnDoneTasks.setText(LanguageManager.getTranslation("btnDoneTasks"));
        titleColumn.setText(LanguageManager.getTranslation("titleColumn"));
        descColumn.setText(LanguageManager.getTranslation("descColumn"));
        dueDateColumn.setText(LanguageManager.getTranslation("dueDateColumn"));
        statusColumn.setText(LanguageManager.getTranslation("statusColumn"));
    }
}
