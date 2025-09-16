

import java.sql.*;

import org.junit.*;


public class DatabaseTest {
    private static Connection conn;

    @BeforeClass
    public static void setup() throws Exception {
        conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/studyplanner", "root", "admin");
    }

    @Test
    public void testInsertStudentAndTask() throws Exception {
        PreparedStatement psStudent = conn.prepareStatement("INSERT INTO students (name) VALUES (?)", 1);
        psStudent.setString(1, "Test User");
        psStudent.executeUpdate();
        ResultSet rsStudent = psStudent.getGeneratedKeys();
        Assert.assertTrue(rsStudent.next());
        int studentId = rsStudent.getInt(1);
        PreparedStatement psTask = conn.prepareStatement("INSERT INTO Task (student_id, title, description, status, dueDate) VALUES (?, ?, ?, ?, ?)");
        psTask.setInt(1, studentId);
        psTask.setString(2, "Test Task");
        psTask.setString(3, "Testing DB logic");
        psTask.setString(4, "To Do");
        psTask.setDate(5, Date.valueOf("2025-09-20"));
        psTask.executeUpdate();
        PreparedStatement psCheck = conn.prepareStatement("SELECT COUNT(*) FROM Task WHERE student_id = ?");
        psCheck.setInt(1, studentId);
        ResultSet rsCheck = psCheck.executeQuery();
        rsCheck.next();
        int count = rsCheck.getInt(1);
        Assert.assertEquals(1L, (long)count);
    }

    @AfterClass
    public static void teardown() throws Exception {
        conn.close();
    }
}

