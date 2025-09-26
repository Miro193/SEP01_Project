package controller;

//import controller.CurrentUser;
//import dao.TaskDAO;
import model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.List;

public class DoneTaskController {

//    @FXML private ListView<String> doneList;
//
//    private TaskDAO taskDAO = new TaskDAO();
//
//    public void initialize() {
//        loadDoneTasks();
//    }
//
//    private void loadDoneTasks() {
//        try {
//            List<Task> tasks = taskDAO.getTasksByUser(CurrentUser.get().getId());
//            for (Task task : tasks) {
//                if ("Done".equalsIgnoreCase(task.getStatus())) {
//                    doneList.getItems().add(task.getTitle());
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}