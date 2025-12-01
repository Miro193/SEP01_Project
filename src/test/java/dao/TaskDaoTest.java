package dao;

import model.Task;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TaskDaoTest {
    private static Connection conn;
    private TaskDao taskDao;
    private Task task;
    private static final String TEST_TITLE_1 = "Test Task";
    private static final String TEST_TITLE_2 = "Task 1";
    private static final String TEST_TITLE_3 = "Task 2";
    private static final String TEST_DESCRIPTION_1 = "Test Desc";
    private static final Logger log = Logger.getLogger(TaskDaoTest.class.getName());

    @BeforeAll
    static void setupDB() throws Exception {
        try {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            try(Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE task (task_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT, title VARCHAR(255), description VARCHAR(255), status VARCHAR(50), dueDate TIMESTAMP, language VARCHAR(50))");
            }
        } catch (Exception e) {
            log.info("context: " + e);
        }
    }

    @AfterAll
    static void closeDatabase() throws Exception {
        conn.close();
    }

    @BeforeEach
    void setUp() {
        taskDao = new TaskDao(conn);
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM task");
        }
    }

    @Test
    void testPersistAndFind() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle(TEST_TITLE_1);
        task.setDescription(TEST_DESCRIPTION_1);
        task.setStatus("TODO");
        task.setDueDate(LocalDateTime.now());
        task.setLanguage("en");

        taskDao.persist(task);
        assertNotNull(task.getTaskId());

        Task found = taskDao.find(task.getTaskId());
        assertNotNull(found);
        assertEquals(TEST_TITLE_1, found.getTitle());
        assertEquals(TEST_DESCRIPTION_1, found.getDescription());
        assertEquals("TODO", found.getStatus());
    }

    @Test
    void testUpdate() {
        Task task = new Task();
        task.setTaskId(1);
        task.setUserId(1);
        task.setTitle("Test Title");
        task.setDescription(TEST_DESCRIPTION_1);
        task.setStatus("IN_PROGRESS");
        task.setDueDate(LocalDateTime.now());

        taskDao.persist(task);
        assertNotNull(task.getTaskId());

        task.setTitle("Updated Title");
        taskDao.update(task);

        Task updated = taskDao.find(task.getTaskId());
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void testFindAll() {
        Task task1 = new Task();
        task1.setTaskId(1);
        task1.setUserId(1);
        task1.setTitle(TEST_TITLE_2);
        task1.setDescription("Desc 1");
        task1.setStatus("TODO");
        task1.setDueDate(LocalDateTime.now());
        task1.setLanguage("en");
        taskDao.persist(task1);

        Task task2 = new Task();
        task2.setTaskId(2);
        task2.setUserId(2);
        task2.setTitle(TEST_TITLE_3);
        task2.setDescription("Desc 2");
        task2.setStatus("DONE");
        task2.setDueDate(LocalDateTime.now());
        task2.setLanguage("en");
        taskDao.persist(task2);

        List<Task> allTasks = taskDao.findAll();
        assertNotNull(allTasks);
        assertEquals(2, allTasks.size());
    }

    @Test
    void testDelete() {
        Task task = new Task();
        task.setUserId(1);
        task.setTitle("Test for Delete");
        task.setDescription(TEST_DESCRIPTION_1);
        task.setStatus("DONE");
        task.setDueDate(LocalDateTime.now());
        taskDao.persist(task);

        assertNotNull(task.getTaskId());
        int taskId = task.getTaskId();
        taskDao.delete(task);
        Task deleted = taskDao.find(taskId);
        assertNull(deleted);
    }

    @Test
    void testGetTasksByUserId() {
        Task task1 = new Task();
        task1.setUserId(1);
        task1.setTitle(TEST_TITLE_2);
        task1.setDescription("Desc 1");
        task1.setStatus("TODO");
        task1.setDueDate(LocalDateTime.now());
        taskDao.persist(task1);

        Task task2 = new Task();
        task2.setUserId(2);
        task2.setTitle(TEST_TITLE_3);
        task2.setDescription("Desc 2");
        task2.setStatus("DONE");
        task2.setDueDate(LocalDateTime.now());
        task2.setLanguage("en");
        taskDao.persist(task2);

        List<Task> user1Tasks = taskDao.getTasksByUserId(1);
        assertNotNull(user1Tasks);
        assertEquals(1, user1Tasks.size());
        assertEquals(1, user1Tasks.get(0).getUserId());
        assertEquals(TEST_TITLE_2, user1Tasks.get(0).getTitle());

        List<Task> user2Tasks = taskDao.getTasksByUserId(2);
        assertNotNull(user2Tasks);
        assertEquals(1, user2Tasks.size());
        assertEquals(2, user2Tasks.get(0).getUserId());
        assertEquals(TEST_TITLE_3, user2Tasks.get(0).getTitle());
    }
}