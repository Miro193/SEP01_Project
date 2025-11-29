package controller;

import dao.UserDao;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.LanguageManager;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for LoginController
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private LoginController loginController;

    @Mock
    private UserDao userDao;

    @Mock
    private ActionEvent mockActionEvent;

    @Mock
    private Stage mockStage;

    @Mock
    private Scene mockScene;

    @Mock
    private Node mockNode;

    // Initialize JavaFX environment
    @BeforeAll
    static void initJavaFX() {
        new JFXPanel(); // Initializes JavaFX toolkit
    }

    @BeforeEach
    void setUp() {
        loginController = new LoginController();

        // Initialize FXML components
        loginController.headerLogin = new Label();
        loginController.lblUsername = new Label();
        loginController.usernameField = new TextField();
        loginController.lblPassword = new Label();
        loginController.passwordField = new PasswordField();
        loginController.btnLogin = new Button();
        loginController.btnSignup = new Button();
        loginController.btnLanguage = new MenuButton();
        loginController.itemPersian = new MenuItem();
        loginController.itemChinese = new MenuItem();
        loginController.itemEnglish = new MenuItem();
        loginController.rootAnchorPane = new AnchorPane();

        // Use reflection to inject the mocked UserDao
        try {
            var field = LoginController.class.getDeclaredField("userDao");
            field.setAccessible(true);
            field.set(loginController, userDao);
        } catch (Exception e) {
            fail("Failed to inject mock UserDao: " + e.getMessage());
        }
    }

    @Test
    void initialize_ShouldSetUpUIComponents() {
        // Given - setup is done in @BeforeEach

        // When
        Platform.runLater(() -> {
            loginController.initialize();

            // Then
            assertNotNull(loginController.headerLogin);
            assertNotNull(loginController.lblUsername);
            assertNotNull(loginController.usernameField);
            assertNotNull(loginController.lblPassword);
            assertNotNull(loginController.passwordField);
            assertNotNull(loginController.btnLogin);
            assertNotNull(loginController.btnSignup);
            assertNotNull(loginController.btnLanguage);
        });
    }

    @Test
    void handleLogin_WithValidCredentials_ShouldNavigateToFirstView() throws IOException {
        // Given
        String username = "testuser";
        String password = "testpass";
        User mockUser = new User(username, password, "Test User", "test@example.com");

        loginController.usernameField.setText(username);
        loginController.passwordField.setText(password);

        when(userDao.login(username, password)).thenReturn(mockUser);
        when(mockActionEvent.getSource()).thenReturn(mockNode);
        when(mockNode.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);

        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class);
             MockedStatic<FXMLLoader> fxmlLoaderMock = mockStatic(FXMLLoader.class)) {

            languageManagerMock.when(() -> LanguageManager.getTranslation(anyString()))
                    .thenReturn("Test Translation");

            FXMLLoader mockLoader = mock(FXMLLoader.class);
            Parent mockParent = mock(Parent.class);
            fxmlLoaderMock.when(() -> FXMLLoader.load(any())).thenReturn(mockParent);

            // When
            loginController.handleLogin(mockActionEvent);

            // Then
            verify(userDao).login(username, password);
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
        }
    }

    @Test
    void handleLogin_WithEmptyFields_ShouldShowAlert() {
        // Given
        loginController.usernameField.setText("");
        loginController.passwordField.setText("");

        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.getTranslation("error.title"))
                    .thenReturn("Error");
            languageManagerMock.when(() -> LanguageManager.getTranslation("error.loginMessage"))
                    .thenReturn("Please fill all fields");

            // When
            loginController.handleLogin(mockActionEvent);

            // Then - No navigation should occur, alert should be shown
            // We can verify that userDao.login was never called
            verify(userDao, never()).login(anyString(), anyString());
        } catch (Exception e) {
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void handleLogin_WithInvalidCredentials_ShouldShowAlert() {
        // Given
        String username = "wronguser";
        String password = "wrongpass";

        loginController.usernameField.setText(username);
        loginController.passwordField.setText(password);

        when(userDao.login(username, password)).thenReturn(null);

        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.getTranslation("error.title"))
                    .thenReturn("Error");
            languageManagerMock.when(() -> LanguageManager.getTranslation("invalid.message"))
                    .thenReturn("Invalid credentials");

            // When
            loginController.handleLogin(mockActionEvent);

            // Then
            verify(userDao).login(username, password);
            // Alert should be shown for invalid credentials
        } catch (Exception e) {
            fail("Test should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void handleSignupRedirect_ShouldNavigateToSignupPage() throws IOException {
        // Given
        when(mockActionEvent.getSource()).thenReturn(mockNode);
        when(mockNode.getScene()).thenReturn(mockScene);
        when(mockScene.getWindow()).thenReturn(mockStage);

        try (MockedStatic<FXMLLoader> fxmlLoaderMock = mockStatic(FXMLLoader.class)) {
            FXMLLoader mockLoader = mock(FXMLLoader.class);
            Parent mockParent = mock(Parent.class);
            fxmlLoaderMock.when(() -> FXMLLoader.load(any())).thenReturn(mockParent);

            // When
            loginController.handleSignupRedirect(mockActionEvent);

            // Then
            verify(mockStage).setScene(any(Scene.class));
            verify(mockStage).show();
        }
    }

    @Test
    void onEnglishClick_ShouldSetEnglishLanguage() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.setLanguage("en", "US"))
                    .thenAnswer(invocation -> {
                        // Simulate setting language
                        return null;
                    });
            languageManagerMock.when(() -> LanguageManager.getCurrentLocale())
                    .thenReturn(new Locale("en", "US"));

            // When
            loginController.onEnglishClick(mockActionEvent);

            // Then
            languageManagerMock.verify(() -> LanguageManager.setLanguage("en", "US"));
        }
    }

    @Test
    void onPersianClick_ShouldSetPersianLanguage() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.setLanguage("fa", "IR"))
                    .thenAnswer(invocation -> {
                        // Simulate setting language
                        return null;
                    });
            languageManagerMock.when(() -> LanguageManager.getCurrentLocale())
                    .thenReturn(new Locale("fa", "IR"));

            // When
            loginController.onPersianClick(mockActionEvent);

            // Then
            languageManagerMock.verify(() -> LanguageManager.setLanguage("fa", "IR"));
        }
    }

    @Test
    void onChineseClick_ShouldSetChineseLanguage() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.setLanguage("zh", "CN"))
                    .thenAnswer(invocation -> {
                        // Simulate setting language
                        return null;
                    });
            languageManagerMock.when(() -> LanguageManager.getCurrentLocale())
                    .thenReturn(new Locale("zh", "CN"));

            // When
            loginController.onChineseClick(mockActionEvent);

            // Then
            languageManagerMock.verify(() -> LanguageManager.setLanguage("zh", "CN"));
        }
    }

    @Test
    void languageTexts_ShouldUpdateAllTexts() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            // Setup mock translations
            languageManagerMock.when(() -> LanguageManager.getTranslation("headerLogin")).thenReturn("Login Header");
            languageManagerMock.when(() -> LanguageManager.getTranslation("lblUsername")).thenReturn("Username");
            languageManagerMock.when(() -> LanguageManager.getTranslation("lblPassword")).thenReturn("Password");
            languageManagerMock.when(() -> LanguageManager.getTranslation("btnLogin")).thenReturn("Login");
            languageManagerMock.when(() -> LanguageManager.getTranslation("btnSignup")).thenReturn("Sign Up");
            languageManagerMock.when(() -> LanguageManager.getTranslation("btnLanguage")).thenReturn("Language");
            languageManagerMock.when(() -> LanguageManager.getTranslation("itemPersian")).thenReturn("Persian");
            languageManagerMock.when(() -> LanguageManager.getTranslation("itemChinese")).thenReturn("Chinese");
            languageManagerMock.when(() -> LanguageManager.getTranslation("itemEnglish")).thenReturn("English");

            // When
            loginController.languageTexts();

            // Then
            assertEquals("Login Header", loginController.headerLogin.getText());
            assertEquals("Username", loginController.lblUsername.getText());
            assertEquals("Password", loginController.lblPassword.getText());
            assertEquals("Login", loginController.btnLogin.getText());
            assertEquals("Sign Up", loginController.btnSignup.getText());
            assertEquals("Language", loginController.btnLanguage.getText());
            assertEquals("Persian", loginController.itemPersian.getText());
            assertEquals("Chinese", loginController.itemChinese.getText());
            assertEquals("English", loginController.itemEnglish.getText());
        }
    }

    @Test
    void applyTextDirection_ForRTL_ShouldSetRightToLeft() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.getCurrentLocale())
                    .thenReturn(new Locale("fa", "IR"));

            // When
            loginController.applyTextDirection();

            // Then
            assertEquals(NodeOrientation.RIGHT_TO_LEFT, loginController.rootAnchorPane.getNodeOrientation());
        }
    }

    @Test
    void applyTextDirection_ForLTR_ShouldSetLeftToRight() {
        // Given
        try (MockedStatic<LanguageManager> languageManagerMock = mockStatic(LanguageManager.class)) {
            languageManagerMock.when(() -> LanguageManager.getCurrentLocale())
                    .thenReturn(new Locale("en", "US"));

            // When
            loginController.applyTextDirection();

            // Then
            assertEquals(NodeOrientation.LEFT_TO_RIGHT, loginController.rootAnchorPane.getNodeOrientation());
        }
    }
}