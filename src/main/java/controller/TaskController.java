package controller;
import dao.TaskDao;
import model.Task;
import model.TaskList;
import model.Status;
public class TaskController {
    private final TaskList taskList;

    public TaskController(TaskList taskList) {
        this.taskList = taskList;
    }

    public void addNewTask(String title, String description, Status status, String dueDate) {
        Task task = new Task(title, description, status, dueDate);
        taskList.addTask(task);

    }
    public void deleteTask(Task task) {
        taskList.deleteTask(task);
    }
}
