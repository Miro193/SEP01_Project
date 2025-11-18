package controller;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;
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
    @FXML
    private AnchorPane rootAnchorPane;

    private UserDao userDao = new UserDao();
    private static final String ALIGN_RIGHT = "-fx-text-alignment: right;";
    private static final String ALIGN_LEFT = "-fx-text-alignment: left;";

    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();
        applyTextDirection();

    }

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("error.fillAll"));
            return;
        }

        User existingUser = userDao.login(username, password);
        if (existingUser != null) {
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("error.usernameExists"));
            return;
        }

        User newUser = new User(username, password, confirmPassword);
        userDao.register(newUser);

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
        lblSignUp.setText(LanguageManager.getTranslation("lblSignUp"));
        lblUsername.setText(LanguageManager.getTranslation("lblUsername"));
        lblPassword.setText(LanguageManager.getTranslation("lblPassword"));
        lblConfirm.setText(LanguageManager.getTranslation("lblConfirm"));
        btnCreateAccount.setText(LanguageManager.getTranslation("btnCreateAccount"));
        btnBackToLogin.setText(LanguageManager.getTranslation("btnBackToLogin"));
    }
    private void applyTextDirection() {
        String lang = LanguageManager.getCurrentLocale().getLanguage();
        boolean isRTL = lang.equals("fa");
        String alignment = isRTL ? ALIGN_RIGHT : ALIGN_LEFT;

        if (rootAnchorPane != null) {
            rootAnchorPane.setNodeOrientation(
                    isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT
            );
        }

        // Apply to text fields
        if (usernameField != null ) {
            usernameField.setStyle(alignment);
        }
        if (passwordField != null) {
            passwordField.setStyle(alignment);
        }
        if (confirmPasswordField != null) {
            confirmPasswordField.setStyle(alignment);
        }
    }

}