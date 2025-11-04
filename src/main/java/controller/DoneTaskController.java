package controller;

import dao.TaskDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.CurrentUser;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DoneTaskController extends BaseController {

    @FXML private ListView<String> doneList;

    private final TaskDao taskDAO = new TaskDao();

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        loadDoneTasks();
    }

    @FXML
    private void backToList(ActionEvent event) throws IOException {
        URL fxmlUrl = getClass().getResource("/TaskList.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, getBundle());
        Parent root = loader.load();
        root.getProperties().put("fxmlLoaderLocation", fxmlUrl);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    private void loadDoneTasks() {
        if (CurrentUser.get() == null) {
            return;
        }

        List<Task> tasks = taskDAO.getTasksByUserId(CurrentUser.get().getId());
        for (Task task : tasks) {
            if ("Done".equalsIgnoreCase(task.getStatus())) {
                doneList.getItems().add(task.getTitle());
            }
        }
    }
}
