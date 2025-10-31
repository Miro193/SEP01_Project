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
    @FXML private Label headerLogin;
    @FXML private Label labelUsername;
    @FXML private TextField usernameField;
    @FXML private Label labelPassword;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;
    @FXML private Button btnSignup;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemEnglish;


    private UserDao userDao = new UserDao();
    private ResourceBundle rb;

    @FXML
    public void initialize() {
        handleLanguage("fa", "IR");
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and Password cannot be empty");
            return;
        }

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
    }
    @FXML
    private void handleSignupRedirect(ActionEvent event) throws IOException {
        Parent signUpRoot = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
        Scene signUpScene = new Scene(signUpRoot);


        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signUpScene);
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("MessagesBundle", locale);
        headerLogin.setText(rb.getString("headerLogin.text"));
        labelUsername.setText(rb.getString("labelUsername.text"));
        labelPassword.setText(rb.getString("labelPassword.text"));
        btnLogin.setText(rb.getString("btnLogin.text"));
        btnSignup.setText(rb.getString("btnSignup.text"));
        btnLanguage.setText(rb.getString("btnLanguage.text"));
        itemPersian.setText(rb.getString("itemPersian.text"));
    }

    public void onPersianClick(ActionEvent event) {
    }

    public void onFinnishClick(ActionEvent event) {
    }

    public void onEnlishClick(ActionEvent event) {
    }
}
