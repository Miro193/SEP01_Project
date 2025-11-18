package controller;

import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CurrentUser;
import model.User;
import java.io.IOException;
import utils.LanguageManager;

public class LoginController extends BaseController {
    @FXML private Label headerLogin;
    @FXML private Label lblUsername;
    @FXML private TextField usernameField;
    @FXML private Label lblPassword;
    @FXML private PasswordField passwordField;
    @FXML private Button btnLogin;
    @FXML private Button btnSignup;
    @FXML private MenuButton btnLanguage;
    @FXML private MenuItem itemPersian;
    @FXML private MenuItem itemChinese;
    @FXML private MenuItem itemEnglish;
    @FXML private AnchorPane rootAnchorPane;


    private UserDao userDao = new UserDao();



    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();
        applyTextDirection();

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
            showAlert(rb.getString("success.title"), rb.getString("success.loginMessage") + user.getUsername());
            Parent firstViewRoot = FXMLLoader.load(getClass().getResource("/first_view.fxml"));
            Scene firstViewScene = new Scene(firstViewRoot);


            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(firstViewScene);
            window.show();
        } else {
            showAlert(rb.getString("error.title"), rb.getString("invalid.message"));
        }
    }
    @FXML
    private void handleSignupRedirect(ActionEvent event) throws IOException {
        Parent signUpRoot = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
        Scene signUpScene = new Scene(signUpRoot);


        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(signUpScene);
        window.setTitle("Sign Up");
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
        //set texts
        headerLogin.setText(rb.getString("headerLogin.text"));
        lblUsername.setText(rb.getString("lblUsername.text"));
        lblPassword.setText(rb.getString("lblPassword.text"));
        btnLogin.setText(rb.getString("btnLogin.text"));
        btnSignup.setText(rb.getString("btnSignup.text"));
        btnLanguage.setText(rb.getString("btnLanguage.text"));
        itemPersian.setText(rb.getString("itemPersian.text"));
        itemChinese.setText(rb.getString("itemChinese.text"));
        itemEnglish.setText(rb.getString("itemEnglish.text"));

    }

    private void applyTextDirection() {
        String lang = LanguageManager.getCurrentLocale().getLanguage();
        boolean isRTL = lang.equals("fa");

        if (rootAnchorPane != null) {
            rootAnchorPane.setNodeOrientation(
                    isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT
            );
        }

        // Apply to text fields
        if (usernameField != null) {
            usernameField.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
        }
        if (passwordField != null) {
            passwordField.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
        }
    }

    @FXML
    public void onEnglishClick(ActionEvent event) throws IOException {
        LanguageManager.setLanguage("en", "US");
        reloadScene(event);

    }

    @FXML
    public void onPersianClick(ActionEvent event) throws IOException {
        LanguageManager.setLanguage("fa", "IR");
        reloadScene(event);

    }

    @FXML
    public void onChineseClick(ActionEvent event) throws IOException {
        LanguageManager.setLanguage("zh", "CN");
        reloadScene(event);

    }
    private void reloadScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Login.fxml"),
                LanguageManager.getBundle()  // Pass current ResourceBundle here
        );
        Parent root = loader.load();

        // use the MenuButton (btnLanguage) to find the Stage
        Stage stage = (Stage) btnLanguage.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}