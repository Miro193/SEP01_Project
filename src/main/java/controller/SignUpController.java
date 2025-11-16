package controller;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;


//import utils.LanguageManager;

//import java.util.Locale;
//import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.LanguageManager;

public class SignUpController extends BaseController {

    @FXML
    private Label lblSignUp;
    @FXML
    private Label lblUsername;
    @FXML
    private TextField usernameField;
    @FXML
    private Label lblPassword;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label lblConfirm;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnCreateAccount;
    @FXML
    private Button btnBackToLogin;

    private UserDao userDao = new UserDao();
    //private ResourceBundle rb;


    @FXML
    public void initialize() {
        updateLanguage(); //from BaseController
        languageTexts();

    }

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            // showAlert("Error", "Please fill all fields!");
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("error.fillAll"));
            return;
        }

        User existingUser = userDao.login(username, password);
        if (existingUser != null) {
            //showAlert("Error", "Username already exists!");
            //showAlert(rb.getString("error.title"), rb.getString("error.usernameExists"));
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("error.usernameExists"));

            return;
        }

        User newUser = new User(username, password, confirmPassword);
        userDao.register(newUser);

        //showAlert("Success", "Account created successfully!");
        showAlert(LanguageManager.getTranslation("success.title"), LanguageManager.getTranslation("success.accountCreated"));

        handleLoginRedirect(event);
    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginRoot);


        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(loginScene);
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void languageTexts() {
        // Locale locale = new Locale(language, country);
        //rb = ResourceBundle.getBundle("MessagesBundle", locale);

        //set texts
        lblSignUp.setText(LanguageManager.getTranslation("lblSignUp"));
        lblUsername.setText(LanguageManager.getTranslation("lblUsername"));
        lblPassword.setText(LanguageManager.getTranslation("lblPassword"));
        lblConfirm.setText(LanguageManager.getTranslation("lblConfirm"));
        btnCreateAccount.setText(LanguageManager.getTranslation("btnCreateAccount"));
        btnBackToLogin.setText(LanguageManager.getTranslation("btnBackToLogin"));


    }

}