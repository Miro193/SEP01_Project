package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.LanguageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        LanguageManager langManager = LanguageManager.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", langManager.getCurrentLocale());

        URL fxmlUrl = getClass().getResource("/Login.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, bundle);
        Parent root = loader.load();

        root.getProperties().put("fxmlLoaderLocation", fxmlUrl);

        primaryStage.setTitle(bundle.getString("app.title"));
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
