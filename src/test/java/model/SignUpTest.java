package model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignUpTest {
    @BeforeAll
    String username = "mikko";
    String password = "1234";
    String secondPassword = "1234";
    SignUp signUp = new SignUp(username,password,secondPassword);

//    SignUp signup = new SignUp("mikko" , "1234", "1234");

    @Test
    void getUsername() {
        assertTrue(true, "mikko");
    }

    @Test
    void setUsername() {

    }

    @Test
    void getPassword() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void getSecondPassword() {
    }

    @Test
    void setSecondPassword() {
    }
}