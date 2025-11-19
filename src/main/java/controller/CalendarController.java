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
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarController extends BaseController {

    @FXML private TableView<Task> calendarTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private Label headerCalendar;
    @FXML private Button btnBack;

    private final TaskDao taskDao = new TaskDao();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        headerCalendar.setText(tm.getTranslation("lblCalendarView"));
        btnBack.setText(tm.getTranslation("btnBackToTaskList"));
        titleColumn.setText(tm.getTranslation("titleColumn"));
        dueDateColumn.setText(tm.getTranslation("dueDateColumn"));
        statusColumn.setText(tm.getTranslation("statusColumn"));

        if (CurrentUser.get() == null) {
            return;
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
        URL fxmlUrl = getClass().getResource("/TaskList.fxml");
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }
}
