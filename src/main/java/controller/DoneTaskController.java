package controller;
import dao.TaskDao;
import model.CurrentUser;
import model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class DoneTaskController {

    @FXML private ListView<String> doneList;

    private TaskDao taskDAO = new TaskDao();

    public void initialize() {
        loadDoneTasks();
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