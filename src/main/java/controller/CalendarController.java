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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Task;
import utils.LanguageManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarController extends BaseController {

    @FXML private TableView<Task> calendarTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private Label lblCalendarView;
    @FXML private Button btnBackToTaskList;

    private TaskDao taskDao = new TaskDao();

    @FXML
    public void initialize() {

        updateLanguage();
        languageTexts();

        if (CurrentUser.get() == null) {
            return; // Or show an error
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);

        List<Task> tasks = taskDao.getTasksByDateRange(CurrentUser.get().getId(), startDate, endDate);
        ObservableList<Task> taskListObservable = FXCollections.observableArrayList(tasks);

        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        dueDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return new SimpleStringProperty(cellData.getValue().getDueDate().format(formatter));
        });
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        calendarTable.setItems(taskListObservable);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void languageTexts() {
        lblCalendarView.setText(LanguageManager.getTranslation("lblCalendarView.text"));
        btnBackToTaskList.setText(LanguageManager.getTranslation("btnBackToTaskList.text"));
        titleColumn.setText(LanguageManager.getTranslation("titleColumn.text"));
        dueDateColumn.setText(LanguageManager.getTranslation("dueDateColumn.text"));
        statusColumn.setText(LanguageManager.getTranslation("statusColumn.text"));
    }
}
