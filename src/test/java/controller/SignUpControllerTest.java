package controller;

import dao.UserDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(ApplicationExtension.class)
class SignUpControllerTest {

    private UserDao mockDao;
    private SignUpController controller;


    @Start
    public void start(Stage stage) throws Exception {
        mockDao = Mockito.mock(UserDao.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Field daoField = SignUpController.class.getDeclaredField("userDao");
        daoField.setAccessible(true);
        daoField.set(controller, mockDao);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }


    @Test
    void testCreateAccountButtonExists(FxRobot robot) {
        Button btn = robot.lookup("#btnCreateAccount").queryAs(Button.class);
        assertNotNull(btn);

        robot.clickOn("#usernameField").write("xxxxx");
        robot.clickOn("#passwordField").write("yyyy");
        robot.clickOn("#confirmPasswordField").write("xxxxx");
        WaitForAsyncUtils.waitForFxEvents();

        TextField username = robot.lookup("#usernameField").queryAs(TextField.class);
        PasswordField password = robot.lookup("#passwordField").queryAs(PasswordField.class);
        PasswordField confirm = robot.lookup("#confirmPasswordField").queryAs(PasswordField.class);

        assertEquals("xxxxx", username.getText());
        assertEquals("yyyy", password.getText());
        assertEquals("xxxxx", confirm.getText());
    }

    @Test
    void testEmptyFieldsShowAlert(FxRobot robot) {
        robot.clickOn("#btnCreateAccount");
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
        robot.clickOn(".button");
    }

    @Test
    void testUsernameAlreadyExistsShowsAlert(FxRobot robot) {
        User existing = new User("existingUser", "pass", "pass");
        doReturn(existing).when(mockDao).login("existingUser", "pass");

        robot.clickOn("#usernameField").write("existingUser");
        robot.clickOn("#passwordField").write("pass");
        robot.clickOn("#confirmPasswordField").write("pass");
        robot.clickOn("#btnCreateAccount");

        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
        robot.clickOn(".dialog-pane .button");

        verify(mockDao, never()).register(any());
    }

    @Test
    void testSuccessfulSignUpCallsRegister(FxRobot robot) {
        doReturn(null).when(mockDao).login("newuser", "newpass");
        doNothing().when(mockDao).register(any());

        robot.clickOn("#usernameField").write("newuser");
        robot.clickOn("#passwordField").write("newpass");
        robot.clickOn("#confirmPasswordField").write("newpass");
        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("#btnCreateAccount");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(robot.lookup(".dialog-pane").tryQuery().isPresent());
        robot.clickOn(".dialog-pane .button");

        verify(mockDao, times(1)).register(argThat(u ->
                u != null
                        && "newuser".equals(u.getUsername())
                        && "newpass".equals(u.getPassword())
                        && "newpass".equals(u.getConfirmPassword())
        ));
    }
}
