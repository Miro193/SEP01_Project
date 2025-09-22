package controller;

import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
public class SignUpController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDao userDao = new UserDao();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        User existingUser = userDao.login(username, password);
        if (existingUser != null) {
            showAlert("Error", "Username already exists!");
            return;
        }

        User newUser = new User(username, password);
        userDao.register(newUser);

        showAlert("Success", "Account created successfully!");
        // TODO: redirect to login page
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
