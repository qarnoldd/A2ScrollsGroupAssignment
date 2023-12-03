package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.services.UserManager;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static lab13.qifan.group2.a2.models.UserType.REGULAR;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserManagerTest {

    private UserManager userManager;
    private UserDatabase userDatabase;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testPassword", "test@email.com", 123456789, "10", REGULAR, "testSalt");
        userManager = new UserManager(user);
        userDatabase = new UserDatabase();
    }

    @Test
    public void testGetUser() {
        User retrievedUser = userManager.getUser();
        assertEquals("testUser", retrievedUser.getUsername());
        assertEquals("testPassword", retrievedUser.getPassword());
        assertEquals("test@email.com", retrievedUser.getEmail());
        assertEquals(123456789, retrievedUser.getPhone());
    }

    @Test
    public void testEditUsername() {
        provideInput("1\nnewUsername\n");
        userManager.editProfile(user);
        assertEquals("newUsername", userManager.getUser().getUsername());
    }

    @Test
    public void testEditPassword() {
        provideInput("2\nnewPassword\n");
        userManager.editProfile(user);
        assertNotEquals("testPassword", userManager.getUser().getPassword());
    }

    @Test
    public void testEditEmail() {
        provideInput("3\nnewEmail@email.com\n");
        userManager.editProfile(user);
        assertEquals("newEmail@email.com", userManager.getUser().getEmail());
    }

    @Test
    public void testEditPhone() {
        provideInput("4\n987654321\n");
        userManager.editProfile(user);
        assertEquals(987654321, userManager.getUser().getPhone());
    }

    @Test
    public void testInvalidInput() {
        InputStream stdin = System.in;
        provideInput("invalidInput\nnewUsername\n");
        userManager.editProfile(user);
        assertEquals("testUser", userManager.getUser().getUsername());
        System.setIn(stdin);
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        userManager = new UserManager(userManager.getUser());
        System.setIn(stdin);
    }

}
