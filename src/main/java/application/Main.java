package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import utils.LanguageManager;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"), LanguageManager.getBundle());
        Parent root = loader.load();

        // Localized title
        primaryStage.setTitle(LanguageManager.getBundle().getString("window.login.title"));
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
