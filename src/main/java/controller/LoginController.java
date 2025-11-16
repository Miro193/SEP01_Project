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
import javafx.stage.Stage;
import model.CurrentUser;
import model.User;
import java.io.IOException;
import utils.LanguageManager;
//import java.util.Locale;
//import java.util.ResourceBundle;

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


    private UserDao userDao = new UserDao();



    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();

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
           // showAlert(rb.getString("success.title"), rb.getString("success.loginMessage") + user.getUsername());
            showAlert(LanguageManager.getTranslation("success.title"), LanguageManager.getTranslation("success.loginMessage") + user.getUsername());
            Parent firstViewRoot = FXMLLoader.load(getClass().getResource("/first_view.fxml"));
            Scene firstViewScene = new Scene(firstViewRoot);


            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(firstViewScene);
            window.show();
        } else {
            showAlert(LanguageManager.getTranslation("error.title"), LanguageManager.getTranslation("invalid.message"));
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
    private void languageTexts() {
        //set texts
        headerLogin.setText(LanguageManager.getTranslation("headerLogin"));
        lblUsername.setText(LanguageManager.getTranslation("lblUsername"));
        lblPassword.setText(LanguageManager.getTranslation("lblPassword"));
        btnLogin.setText(LanguageManager.getTranslation("btnLogin"));
        btnSignup.setText(LanguageManager.getTranslation("btnSignup"));
        btnLanguage.setText(LanguageManager.getTranslation("btnLanguage"));
        itemPersian.setText(LanguageManager.getTranslation("itemPersian"));
        itemChinese.setText(LanguageManager.getTranslation("itemChinese"));
        itemEnglish.setText(LanguageManager.getTranslation("itemEnglish"));

        // Right-to-left for Persian  //Labels appear on the right of fields.
        //Text flows right-to-left.
      /*  if (language.equals("fa") || language.equalsIgnoreCase("ar")) {
            headerLogin.getScene().getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            headerLogin.getScene().getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        // Optionally set default locale
        Locale.setDefault(locale);*/
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
                getClass().getResource("/Login.fxml")
               // LanguageManager.getBundle()  // Pass current ResourceBundle here
        );
        Parent root = loader.load();

        // use the MenuButton (btnLanguage) to find the Stage
        Stage stage = (Stage) btnLanguage.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}