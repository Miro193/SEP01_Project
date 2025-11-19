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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CurrentUser;
import model.User;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;

public class LoginController extends BaseController {

    @FXML private Label headerLogin;
    @FXML private Label labelUsername;
    @FXML private Label labelPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnSignup;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final UserDao userDao = new UserDao();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        headerLogin.setText(tm.getTranslation("headerLogin"));
        labelUsername.setText(tm.getTranslation("lblUsername"));
        labelPassword.setText(tm.getTranslation("lblPassword"));
        btnLogin.setText(tm.getTranslation("btnLogin"));
        btnSignup.setText(tm.getTranslation("btnSignup"));
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

            URL fxmlUrl = getClass().getResource("/first_view.fxml");
            LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } else {
            showAlert("Error", "Invalid username or password");
        }
    }

    @FXML
    private void handleSignupRedirect(ActionEvent event) throws IOException {
        URL fxmlUrl = getClass().getResource("/SignUp.fxml");
        LanguageManager.getInstance().setCurrentFxmlUrl(fxmlUrl);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
