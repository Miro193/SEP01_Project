import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Apptest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //
        URL fxmlLocation = getClass().getResource("/LoginView.fxml");
        System.out.println("FXML Route Found: " + fxmlLocation);

        if (fxmlLocation == null) {
            throw new RuntimeException("The FXML file was not found in: /LoginView.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}