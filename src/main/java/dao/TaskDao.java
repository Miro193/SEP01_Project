package dao;
import jakarta.persistence.EntityManager;
import model.Task;
import java.util.List;

public class TaskDao {
    public void persist(Task task) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(task);
        em.getTransaction().commit();
    }
    public Task find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        Task task = em.find(Task.class, id);
        return task;
    }
    public List<Task> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<Task> tasks = em.createNamedQuery("SELECT t FROM Task t", Task.class).getResultList();
        return tasks;

    }
    public void update(Task task) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(task);
        em.getTransaction().commit();

    }
    public void delete(Task task) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(task);
        em.getTransaction().commit();

    }


    }



