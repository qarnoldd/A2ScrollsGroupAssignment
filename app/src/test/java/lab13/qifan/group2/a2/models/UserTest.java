package lab13.qifan.group2.a2.models;

import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testUsername", "testPassword", "testEmail", 123456789, "testIdKey", UserType.REGULAR, "testSalt");
    }

    @Test
    void constructorAndGetters() {
        assertEquals("testUsername", testUser.getUsername());
        assertEquals("testPassword", testUser.getPassword());
        assertEquals("testEmail", testUser.getEmail());
        assertEquals(123456789, testUser.getPhone());
        assertEquals("testIdKey", testUser.getIdKey());
        assertEquals(UserType.REGULAR, testUser.getUserType());
        assertEquals("testSalt", testUser.getSalt());
    }

    @Test
    void setters() {
        testUser.setUsername("newUsername");
        testUser.setPassword("newPassword");
        testUser.setEmail("newEmail");
        testUser.setPhone(987654321);
        testUser.setIdKey("newIdKey");
        testUser.setUserType(UserType.ADMIN);
        testUser.setSalt("newSalt");

        assertEquals("newUsername", testUser.getUsername());
        assertEquals("newPassword", testUser.getPassword());
        assertEquals("newEmail", testUser.getEmail());
        assertEquals(987654321, testUser.getPhone());
        assertEquals("newIdKey", testUser.getIdKey());
        assertEquals(UserType.ADMIN, testUser.getUserType());
        assertEquals("newSalt", testUser.getSalt());
    }
}
