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



    @FXML
    public void initialize() {
        updateLanguage();
        languageTexts();

       // handleLanguage("en", "US");
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(rb.getString("error.title"), rb.getString("error.loginMessage"));
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
            labelUsername.setText(rb.getString("labelUsername.text"));
            labelPassword.setText(rb.getString("labelPassword.text"));
            btnLogin.setText(rb.getString("btnLogin.text"));
            btnSignup.setText(rb.getString("btnSignup.text"));
            btnLanguage.setText(rb.getString("btnLanguage.text"));
            itemPersian.setText(rb.getString("itemPersian.text"));
            itemFinnish.setText(rb.getString("itemFinnish.text"));
            itemEnglish.setText(rb.getString("itemEnglish.text"));

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
    public void onFinnishClick(ActionEvent event) throws IOException {
        LanguageManager.setLanguage("fi", "FI");
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
        stage.setTitle(LanguageManager.getBundle().getString("window.login.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }


}
