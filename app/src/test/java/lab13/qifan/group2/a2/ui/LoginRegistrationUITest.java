package lab13.qifan.group2.a2.ui;
import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static lab13.qifan.group2.a2.models.UserType.REGULAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginRegistrationUITest {

    private LoginRegistrationUI loginRegistrationUI;
    private UserDatabase userDatabase;
    private ByteArrayOutputStream outputStream;



    @BeforeEach
    public void setUp() {
        userDatabase = Mockito.mock(UserDatabase.class);
        loginRegistrationUI = new LoginRegistrationUI();
        loginRegistrationUI.setUserDatabase(userDatabase);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

   @AfterEach
   public void tearDown() {
       // Clean up the test environment, e.g., delete the test user from the database
       User testUser = new User("testUser", "testPassword", "test@test.com", 123456789, "11", REGULAR, "testSalt");
       userDatabase.removeUser(testUser);
   }
//
//    @Test
//    public void testDisplayRegistrationScreen_UsernameAlreadyExist() {
//        provideInput("testUser\ntestPassword\ntestPassword\n123456789\n11\n");
//        when(userDatabase.getUser("testUser")).thenReturn(null);
//        when(userDatabase.addUser(any(User.class))).thenReturn(true);
//
//        loginRegistrationUI.displayRegistrationScreen();
//
//        String expectedOutputFormat =
//        "-------------- REGISTRATION SCREEN --------------\n" +
//        "Username: Username already exists. Please choose a different username.\n" +
//        "Username: Password: The password you just entered is: testPassword\n" +
//        "Email: Phone: ID Key (leave empty for default): ";
//
//        assertEquals(expectedOutputFormat, outputStream.toString());
//
//    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            loginRegistrationUI = new LoginRegistrationUI();
        } finally {
            System.setIn(stdin);
        }
    }
}

