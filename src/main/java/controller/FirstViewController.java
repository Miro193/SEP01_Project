package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FirstViewController {

    @FXML private Button enterTask;
    @FXML private Button taskLists;


    @FXML private Label headerLabel;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private ResourceBundle rb;

    @FXML
    public void initialize() {

        handleLanguage("en", "US");
    }

    @FXML
    private void handleEnterTask(ActionEvent event) throws IOException {
        Parent taskRoot = FXMLLoader.load(getClass().getResource("/AddTask.fxml"));
        Scene taskScene = new Scene(taskRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskScene);
        window.show();
    }

    @FXML
    private void directToTaskLists(ActionEvent event) throws IOException {
        Parent taskListsRoot = FXMLLoader.load(getClass().getResource("/TaskList.fxml"));
        Scene taskListsScene = new Scene(taskListsRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(taskListsScene);
        window.show();
    }



    @FXML
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("messages", locale);
        headerLabel.setText(rb.getString("firstView.header"));
        enterTask.setText(rb.getString("firstView.enterTask"));
        taskLists.setText(rb.getString("firstView.taskLists"));
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
