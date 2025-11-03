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
import model.User;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignUpController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;


    @FXML private Label headerSignUp;
    @FXML private Label labelUsername;
    @FXML private Label labelPassword;
    @FXML private Label labelConfirm;
    @FXML private Button btnCreateAccount;
    @FXML private Button btnBackToLogin;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemEnglish;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemFinnish;
    @FXML private MenuItem itemChinese;

    private UserDao userDao = new UserDao();
    private ResourceBundle rb;

    @FXML
    public void initialize() {

        handleLanguage("en", "US");
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
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loginRoot);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (Exception e) {
            System.err.println("Error loading login page: " + e.getMessage());
            showAlert("Error", "Cannot load login page. Please try again.");
        }
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
        rb = ResourceBundle.getBundle("messages", locale);
        headerSignUp.setText(rb.getString("signup.header"));
        labelUsername.setText(rb.getString("signup.username"));
        labelPassword.setText(rb.getString("signup.password"));
        labelConfirm.setText(rb.getString("signup.confirm"));
        btnCreateAccount.setText(rb.getString("signup.createAccount"));
        btnBackToLogin.setText(rb.getString("signup.backToLogin"));
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
