package jUnitTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import server.User;

public class UserTest {

    class TestUser extends User {
        public TestUser(String name, String password) {
            super(name, password);
        }

        public Role getRole() {
            return Role.STUDENT;
        }
    }

    @Test
    public void testGetIdAndName() {
        TestUser user = new TestUser("TestUser", "password123");
        assertEquals("TestUser", user.getName());
        assertTrue(user.getId() > 0);
    }

    @Test
    public void testAuthenticateSuccess() {
        TestUser user = new TestUser("Tester", "abc123");
        assertTrue(user.authenticate("abc123"));
    }

    @Test
    public void testAuthenticateFail() {
        TestUser user = new TestUser("Tester", "abc123");
        assertFalse(user.authenticate("wrong"));
    }

    @Test
    public void testGetRole() {
        TestUser user = new TestUser("Tester", "abc123");
        assertEquals(server.User.Role.STUDENT, user.getRole());
    }
}
