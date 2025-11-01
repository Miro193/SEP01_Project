package controller;

import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.CurrentUser;
import model.User;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private MenuButton btnLanguage;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignup;


    private UserDao userDao = new UserDao();

    @FXML
    public void initialize() {

        Locale defaultLocale = new Locale("en", "US");
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", defaultLocale);
        labelTitle.setText(rb.getString("login.title"));
        labelUsername.setText(rb.getString("login.username.label"));
        labelPassword.setText(rb.getString("login.password.label"));
        btnLogin.setText(rb.getString("login.button"));
        btnSignup.setText(rb.getString("signup.button"));
        btnLanguage.setText(rb.getString("language.menu"));
        usernameField.setPromptText(rb.getString("usernameField.prompt"));
        passwordField.setPromptText(rb.getString("passwordField.prompt"));


    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and Password cannot be empty");
            return;
        }

        try {
            User user = userDao.login(username, password);

            if (user != null && user.getPassword().equals(password)) {
                CurrentUser.set(user);
                showAlert("Success", "Login successful! Welcome " + user.getUsername());
                Parent firstViewRoot = FXMLLoader.load(getClass().getResource("/first_view.fxml"));
                Scene firstViewScene = new Scene(firstViewRoot);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(firstViewScene);
                window.show();
            } else {
                showAlert("Error", "Invalid username or password");
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            showAlert("Database Error", "Cannot connect to database. Please try again later.");
        }
    }

    @FXML
    private void handleSignupRedirect(ActionEvent event) throws IOException {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
            Scene signUpScene = new Scene(signUpRoot);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(signUpScene);
            window.show();
        } catch (Exception e) {
            System.err.println("Error loading signup page: " + e.getMessage());
            showAlert("Error", "Cannot load signup page. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLanguage(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String selectedLanguage = selectedItem.getText();

        Locale locale = switch (selectedLanguage) {
            case "English" -> new Locale("en", "US");
            case "Finnish" -> new Locale("fi", "FI");
            case "Persian" -> new Locale("fa", "IR");
            default -> Locale.getDefault();
        };

        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);


        labelTitle.setText(rb.getString("login.title"));
        labelUsername.setText(rb.getString("login.username.label"));
        labelPassword.setText(rb.getString("login.password.label"));
        btnLogin.setText(rb.getString("login.button"));
        btnSignup.setText(rb.getString("signup.button"));
        btnLanguage.setText(rb.getString("language.menu"));
        usernameField.setPromptText(rb.getString("usernameField.prompt"));
        passwordField.setPromptText(rb.getString("passwordField.prompt"));


    }
}