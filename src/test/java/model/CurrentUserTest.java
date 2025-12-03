package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {
    @AfterEach
    void tearDown() {
        CurrentUser.set(null);
    }

    @Test
    void testSetAndGetUser_Success() {
        User testUser = new User("testuser", "password");
        testUser.setUserID(123);


        CurrentUser.set(testUser);

        User currentUser = CurrentUser.get();
        assertNotNull(currentUser, "CurrentUser.get() should not be null after setting a user.");
        assertEquals(testUser, currentUser, "The retrieved user should be the same as the one that was set.");
        assertEquals(123, currentUser.getUserID(), "The ID of the retrieved user should match.");
        assertEquals("testuser", currentUser.getUsername(), "The username of the retrieved user should match.");
    }

    @Test
    void testGet_WhenNoUserIsSet_ReturnsNull() {

        User currentUser = CurrentUser.get();


        assertNull(currentUser, "CurrentUser.get() should return null when no user has been set.");
    }

    @Test
    void testSet_WithNull_ClearsCurrentUser() {
        User testUser = new User("testuser", "password");
        CurrentUser.set(testUser);
        assertNotNull(CurrentUser.get(), "Precondition failed: A user should be set before testing clear.");

        CurrentUser.set(null);

        assertNull(CurrentUser.get(), "CurrentUser.get() should return null after setting the user to null.");
    }

    @Test
    void testSet_CanOverwriteExistingUser() {

        User initialUser = new User("initialUser", "pass1");
        initialUser.setUserID(1);
        CurrentUser.set(initialUser);

        User newUser = new User("newUser", "pass2");
        newUser.setUserID(2);
        CurrentUser.set(newUser);

        User currentUser = CurrentUser.get();
        assertNotNull(currentUser);
        assertEquals(newUser, currentUser, "The current user should be the new user.");
        assertEquals(2, currentUser.getUserID());
        assertEquals("newUser", currentUser.getUsername());
    }
}
