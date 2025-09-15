package model;
import jakarta.persistence.*;

@Entity
@Table(name= "task")
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private Status status;
    @Column
    private String dueDate;

    public Task(){}

    public Task(String title, String description, Status status, String dueDate) {

        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // --- Getters and setters ---
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Task: " +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", dueDate=" + dueDate;
    }


}
