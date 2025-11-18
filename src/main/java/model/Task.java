package model;

import java.time.LocalDateTime;

public class Task {
    private int taskId;
    private int userId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private String language;


    public Task(int taskId, int userId, String title, String description, LocalDateTime dueDate, String status) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task() {}

    public int getTaskId() {
        return taskId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getLanguage() {return language;}


    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   public void setLanguage(String language) {this.language = language;}
}