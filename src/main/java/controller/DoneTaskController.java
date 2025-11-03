package controller;
import dao.TaskDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DoneTaskController {

    @FXML private ListView<String> doneList;
    @FXML private Button backTolist;


    @FXML private Label headerDoneTasks;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private TaskDao taskDAO = new TaskDao();
    private ResourceBundle rb;

    @FXML
    public void initialize() {
        loadDoneTasks();

        handleLanguage("en", "US");
    }

    @FXML
    private void backToList(ActionEvent event) throws IOException {
        Parent taskListsRoot = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene taskListsScene = new Scene(taskListsRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskListsScene);
        window.show();
    }

    private void loadDoneTasks() {
        if (CurrentUser.get() == null) {
            return;
        }

        List<Task> tasks = taskDAO.getTasksByUserId(CurrentUser.get().getId());
        for (Task task : tasks) {
            if ("Done".equalsIgnoreCase(task.getStatus())) {
                doneList.getItems().add(task.getTitle());
            }
        }
    }



    @FXML
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("messages", locale);
        headerDoneTasks.setText(rb.getString("doneTask.header"));
        backTolist.setText(rb.getString("doneTask.back"));
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
