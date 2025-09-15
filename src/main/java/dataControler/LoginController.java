package dataControler;

import dao.StudentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private TextField idField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLoginButton() {
        try {
            int id = Integer.parseInt(idField.getText());
            String username = usernameField.getText();

            StudentDAO dao = new StudentDAO();
            boolean goodlogin = dao.validateLogin(id, username);

            if (goodlogin) {
                messageLabel.setText("Login correct.");
                NewWindows();
            } else {
                messageLabel.setText("Dates incorrect.");
            }

        } catch (Exception e) {
            messageLabel.setText("Error.");
        }
    }
        //open new windows
    private void NewWindows() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("welcome");
            stage.show();

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            messageLabel.setText("the window cannot be opened.");
        }
    }
}