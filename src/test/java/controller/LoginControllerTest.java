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
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String DIALOG_PANE_SELECTOR = ".dialog-pane";
    private static final String BUTTON_LOGIN = "#btnLogin";
    private static final String USERNAME = "#usernameField";
    private static final String PASSWORD = "#passwordField";

    @Start
    public void start(Stage stage) throws Exception {
        UserDao mockDao = Mockito.mock(UserDao.class);
        //Load FXML manually
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller = loader.getController();
        Field userDaoField = controller.getClass().getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(controller, mockDao);

        User fakeUser = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
        when(mockDao.login(ADMIN_USERNAME, ADMIN_PASSWORD)).thenReturn(fakeUser);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }
    @Test
    void testLoginButtonExists(FxRobot robot) {
        Button btnLogin = robot.lookup(BUTTON_LOGIN).queryAs(Button.class);
        assertNotNull(btnLogin);
    }

    @Test
    void testEmptyFieldsShowAlert(FxRobot robot) {
        robot.clickOn(BUTTON_LOGIN);
        robot.lookup(DIALOG_PANE_SELECTOR);
        assertTrue(robot.lookup(DIALOG_PANE_SELECTOR).tryQuery().isPresent());
    }

    @Test
    void testWritingUsernameAndPassword(FxRobot robot) {
        TextField username = robot.lookup(USERNAME).queryAs(TextField.class);
        TextField password = robot.lookup(PASSWORD).queryAs(TextField.class);

        robot.clickOn(USERNAME);
        robot.write("testuser");

        robot.clickOn(PASSWORD);
        robot.write("12345");

        assertEquals("testuser", username.getText());
        assertEquals("12345", password.getText());
    }

    @Test
    void testValidLoginClick(FxRobot robot) {
        // Fill fields
        robot.clickOn(USERNAME);
        robot.write(ADMIN_USERNAME);

        robot.clickOn(PASSWORD);
        robot.write(ADMIN_PASSWORD);

        // Click
        robot.clickOn(BUTTON_LOGIN);

        // Expect success alert
        assertTrue(robot.lookup(DIALOG_PANE_SELECTOR).tryQuery().isPresent());
    }
  
}