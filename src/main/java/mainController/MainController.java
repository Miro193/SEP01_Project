package mainController;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeLabel;
    public void initialize() {
        welcomeLabel.setText("Moi!");
        }
    }


