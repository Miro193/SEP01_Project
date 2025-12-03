package controller;

import dao.UserDao;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class SignUpControllerTest {

    // CONSTANTS to avoid duplicated literals
    private static final String EXISTING_USER = "existingUser";
    private static final String PASSWORD = "pass";
    private static final String NEW_USER = "newuser";
    private static final String NEW_PASS = "newpass";

    private static final String BTN_CREATE_ACCOUNT = "#btnCreateAccount";
    private static final String USERNAME_FIELD = "#usernameField";
    private static final String PASSWORD_FIELD = "#passwordField";
    private static final String CONFIRM_PASSWORD_FIELD = "#confirmPasswordField";

    private static final String DIALOG_PANE = ".dialog-pane";
    private static final String BUTTON = ".button";

    private UserDao mockDao;
    private SignUpController controller;

    @Start
    public void start(Stage stage) throws Exception {
        // create mock dao and load FXML
        mockDao = Mockito.mock(UserDao.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        // inject mock dao into controller using reflection
        injectDao(controller, mockDao);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    // helper method for reflection injection
    private static void injectDao(SignUpController controller, UserDao dao) throws Exception {
        Field daoField = SignUpController.class.getDeclaredField("userDao");
        daoField.setAccessible(true);
        daoField.set(controller, dao);
    }

    @Test
    void testCreateAccountButtonExists(FxRobot robot) {
        // correct fx:id in your FXML is btnCreateAccount
        Button btn = robot.lookup(BTN_CREATE_ACCOUNT).queryAs(Button.class);
        assertNotNull(btn, "btnCreateAccount should be present in FXML");
    }

    @Test
    void testEmptyFieldsShowAlert(FxRobot robot) {
        // click the create account button with empty fields
        robot.clickOn(BTN_CREATE_ACCOUNT);

        assertTrue(robot.lookup(DIALOG_PANE).tryQuery().isPresent(),
                "Alert should be shown when fields are empty");
        robot.clickOn(BUTTON);
    }

    @Test
    void testUsernameAlreadyExistsShowsAlert(FxRobot robot) {
        // simulate existing user: login(...) returns a User
        User existing = new User(EXISTING_USER, PASSWORD, PASSWORD);
        when(mockDao.login(EXISTING_USER, PASSWORD)).thenReturn(existing);

        robot.clickOn(USERNAME_FIELD).write(EXISTING_USER);
        robot.clickOn(PASSWORD_FIELD).write(PASSWORD);
        robot.clickOn(CONFIRM_PASSWORD_FIELD).write(PASSWORD);
        robot.clickOn(BTN_CREATE_ACCOUNT);

        // expect alert
        assertTrue(robot.lookup(DIALOG_PANE).tryQuery().isPresent(),
                "Alert should be shown when username already exists");
        robot.clickOn(BUTTON);

        // register should NOT be called
        verify(mockDao, never()).register(any());
    }

    @Test
    void testSuccessfulSignUpCallsRegister(FxRobot robot) {
        // user does not exist
        when(mockDao.login(NEW_USER, NEW_PASS)).thenReturn(null);

        robot.clickOn(USERNAME_FIELD).write(NEW_USER);
        robot.clickOn(PASSWORD_FIELD).write(NEW_PASS);
        robot.clickOn(CONFIRM_PASSWORD_FIELD).write(NEW_PASS);
        robot.clickOn(BTN_CREATE_ACCOUNT);

        // success alert shown
        assertTrue(robot.lookup(DIALOG_PANE).tryQuery().isPresent(),
                "Success alert should be shown");
        robot.clickOn(BUTTON); // close the alert

        // verify register called with a User that has expected values
        verify(mockDao, times(1)).register(argThat(u ->
                u != null
                        && NEW_USER.equals(u.getUsername())
                        && NEW_PASS.equals(u.getPassword())
                        && NEW_PASS.equals(u.getConfirmPassword())
        ));
    }
}
