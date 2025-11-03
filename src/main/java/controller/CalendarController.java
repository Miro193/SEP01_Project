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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CalendarController {

    @FXML private TableView<Task> calendarTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;


    @FXML private Label headerCalendar;
    @FXML private Button btnBack;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private TaskDao taskDao = new TaskDao();
    private ResourceBundle rb;

    @FXML
    public void initialize() {
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


        handleLanguage("en", "US");
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
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("messages", locale);
        headerCalendar.setText(rb.getString("calendar.header"));
        btnBack.setText(rb.getString("calendar.back"));
        titleColumn.setText(rb.getString("calendar.task"));
        dueDateColumn.setText(rb.getString("calendar.dueDate"));
        statusColumn.setText(rb.getString("calendar.status"));
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

    public void onChineseClick(ActionEvent event) {
        handleLanguage("zh", "CN");
    }
}
