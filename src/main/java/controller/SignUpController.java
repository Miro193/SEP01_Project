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
import model.User;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;

public class SignUpController extends BaseController {

    @FXML private Label headerSignUp;
    @FXML private Label labelUsername;
    @FXML private Label labelPassword;
    @FXML private Label labelConfirm;
    @FXML private Button btnCreateAccount;
    @FXML private Button btnBackToLogin;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final UserDao userDao = new UserDao();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        LanguageManager tm = LanguageManager.getInstance();
        headerSignUp.setText(tm.getTranslation("lblSignUp"));
        labelUsername.setText(tm.getTranslation("lblUsername"));
        labelPassword.setText(tm.getTranslation("lblPassword"));
        labelConfirm.setText(tm.getTranslation("lblConfirm"));
        btnCreateAccount.setText(tm.getTranslation("btnCreateAccount"));
        btnBackToLogin.setText(tm.getTranslation("btnBackToLogin"));
    }

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        try {
            User existingUser = userDao.login(username, password);
            if (existingUser != null) {
                showAlert("Error", "Username already exists!");
                return;
            }

            User newUser = new User(username, password, confirmPassword);
            userDao.register(newUser);

            showAlert("Success", "Account created successfully!");
            handleLoginRedirect(event);

        } catch (Exception e) {
            System.err.println("Signup error: " + e.getMessage());
            showAlert("Database Error", "Cannot create account at the moment. Please try again later.\n\nError: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) throws IOException {
        URL fxmlUrl = getClass().getResource("/Login.fxml");
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
