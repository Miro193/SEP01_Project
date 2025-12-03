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
import model.User;

@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {

    // Constants to avoid duplicated literals
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "12345";

    // FXML element IDs
    private static final String BTN_LOGIN_ID = "#btnLogin";
    private static final String USERNAME_FIELD_ID = "#usernameField";
    private static final String PASSWORD_FIELD_ID = "#passwordField";
    private static final String DIALOG_PANE_ID = ".dialog-pane";

    private UserDao mockDao;
    private LoginController controller;

    @Start
    public void start(Stage stage) throws Exception {
        mockDao = Mockito.mock(UserDao.class);

        // Load FXML manually
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        // Get controller
        controller = loader.getController();
        controller.setUserDao(mockDao); // minimal change: use setter instead of reflection

        // Use constants instead of duplicated literals
        User fakeUser = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
        when(mockDao.login(ADMIN_USERNAME, ADMIN_PASSWORD)).thenReturn(fakeUser);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    void testLoginButtonExists(FxRobot robot) {
        // Verify login button exists
        Button btnLogin = robot.lookup(BTN_LOGIN_ID).queryAs(Button.class);
        assertNotNull(btnLogin);
    }

    @Test
    void testEmptyFieldsShowAlert(FxRobot robot) {
        // Click login button without filling fields
        robot.clickOn(BTN_LOGIN_ID);
        assertTrue(robot.lookup(DIALOG_PANE_ID).tryQuery().isPresent());
    }

    @Test
    void testWritingUsernameAndPassword(FxRobot robot) {
        // Get text fields
        TextField username = robot.lookup(USERNAME_FIELD_ID).queryAs(TextField.class);
        TextField password = robot.lookup(PASSWORD_FIELD_ID).queryAs(TextField.class);

        // Write into fields
        robot.clickOn(USERNAME_FIELD_ID);
        robot.write(TEST_USERNAME);

        robot.clickOn(PASSWORD_FIELD_ID);
        robot.write(TEST_PASSWORD);

        // Verify values
        assertEquals(TEST_USERNAME, username.getText());
        assertEquals(TEST_PASSWORD, password.getText());
    }

    @Test
    void testValidLoginClick(FxRobot robot) {
        // Fill fields with valid credentials
        robot.clickOn(USERNAME_FIELD_ID);
        robot.write(ADMIN_USERNAME);

        robot.clickOn(PASSWORD_FIELD_ID);
        robot.write(ADMIN_PASSWORD);

        // Click login button
        robot.clickOn(BTN_LOGIN_ID);

        // Expect success alert
        assertTrue(robot.lookup(DIALOG_PANE_ID).tryQuery().isPresent());
    }
}
