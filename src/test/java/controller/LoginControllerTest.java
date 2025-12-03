package controller;
import static org.junit.jupiter.api.Assertions.*;

import dao.UserDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import model.User;


@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {
    private UserDao mockDao;
    private LoginController controller;
    @Start
    public void start(Stage stage) throws Exception {
        mockDao = Mockito.mock(UserDao.class);
        //Load FXML manually
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        //Get controller
        controller = loader.getController();
        Field userDaoField = controller.getClass().getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(controller, mockDao);

        User fakeUser = new User("admin", "admin");
        when(mockDao.login("admin", "admin")).thenReturn(fakeUser);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }
    @Test
    void testLoginButtonExists(FxRobot robot) {
        Button btnLogin = robot.lookup("#btnLogin").queryAs(Button.class);
        assertNotNull(btnLogin);
    }

    @Test
    void testEmptyFieldsShowAlert(FxRobot robot) {
        robot.clickOn("#btnLogin");
        robot.lookup(".dialog-pane");
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
    }

    @Test
    void testWritingUsernameAndPassword(FxRobot robot) {
        TextField username = robot.lookup("#usernameField").queryAs(TextField.class);
        TextField password = robot.lookup("#passwordField").queryAs(TextField.class);

        robot.clickOn("#usernameField");
        robot.write("testuser");

        robot.clickOn("#passwordField");
        robot.write("12345");

        assertEquals("testuser", username.getText());
        assertEquals("12345", password.getText());
    }

    @Test
    void testValidLoginClick(FxRobot robot) {
        // Fill fields
        robot.clickOn("#usernameField");
        robot.write("admin");

        robot.clickOn("#passwordField");
        robot.write("admin");

        // Click
        robot.clickOn("#btnLogin");

        // Expect success alert
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
    }
  
}