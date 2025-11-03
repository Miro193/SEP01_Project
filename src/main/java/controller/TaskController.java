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
import java.util.Locale;
import java.util.ResourceBundle;

public class TaskController {

    // --- FXML Fields ---

    // Fields for AddTask.fxml
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private Label headerAddTask;
    @FXML private Label labelTitle;
    @FXML private Label labelDescription;
    @FXML private Label labelDueDate;
    @FXML private Label labelStatus;
    @FXML private Button btnCancel;
    @FXML private Button btnSaveTask;

    // Fields for TaskList.fxml
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private Button btnAddTask;
    @FXML private Button btnDeleteTask;
    @FXML private Button btnEditTask;
    @FXML private Button btnCalendarView;
    @FXML private Button btnDoneTasks;

    // Common Fields
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private TaskDao taskDao = new TaskDao();
    private ObservableList<Task> taskListObservable;
    private ResourceBundle rb;

    @FXML
    public void initialize() {
        // Initialization logic for TaskList.fxml
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

        // Initialization logic for AddTask.fxml
        if (statusChoice != null) {
            statusChoice.setItems(FXCollections.observableArrayList("TODO", "IN_PROGRESS", "DONE"));
            statusChoice.setValue("TODO");
        }
        // Set default language
        handleLanguage("en", "US");
    }

    // --- Navigation and Action Handlers ---

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

    // --- Helper Methods ---

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
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("messages", locale);


        if (btnAddTask != null) btnAddTask.setText(rb.getString("taskList.addTask"));
        if (btnDeleteTask != null) btnDeleteTask.setText(rb.getString("taskList.deleteTask"));
        if (btnEditTask != null) btnEditTask.setText(rb.getString("taskList.editTask"));
        if (btnCalendarView != null) btnCalendarView.setText(rb.getString("taskList.calendarView"));
        if (btnDoneTasks != null) btnDoneTasks.setText(rb.getString("taskList.doneTasks"));
        if (titleColumn != null) titleColumn.setText(rb.getString("taskList.title"));
        if (descColumn != null) descColumn.setText(rb.getString("taskList.description"));
        if (dueDateColumn != null) dueDateColumn.setText(rb.getString("taskList.dueDate"));
        if (statusColumn != null) statusColumn.setText(rb.getString("taskList.status"));


        if (headerAddTask != null) headerAddTask.setText(rb.getString("addTask.header"));
        if (labelTitle != null) labelTitle.setText(rb.getString("addTask.title"));
        if (labelDescription != null) labelDescription.setText(rb.getString("addTask.description"));
        if (labelDueDate != null) labelDueDate.setText(rb.getString("addTask.dueDate"));
        if (labelStatus != null) labelStatus.setText(rb.getString("addTask.status"));
        if (btnCancel != null) btnCancel.setText(rb.getString("addTask.cancel"));
        if (btnSaveTask != null) btnSaveTask.setText(rb.getString("addTask.save"));


        if (btnLanguage != null) btnLanguage.setText(rb.getString("btnLanguage.text"));
        if (itemEnglish != null) itemEnglish.setText(rb.getString("itemEnglish.text"));
        if (itemPersian != null) itemPersian.setText(rb.getString("itemPersian.text"));
        if (itemFinnish != null) itemFinnish.setText(rb.getString("itemFinnish.text"));
        if (itemChinese != null) itemChinese.setText(rb.getString("itemChinese.text"));
    }

    public void onEnglishClick(ActionEvent event) { handleLanguage("en", "US"); }

    public void onPersianClick(ActionEvent event) { handleLanguage("fa", "IR"); }

    public void onFinnishClick(ActionEvent event) { handleLanguage("fi", "FI"); }

    public void onChineseClick(ActionEvent event) { handleLanguage("zh", "CN"); }

}
