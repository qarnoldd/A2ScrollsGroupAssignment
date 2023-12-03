package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.ui.DashboardUI;
import lab13.qifan.group2.a2.services.UserManager;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static lab13.qifan.group2.a2.models.UserType.REGULAR;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class DashboardUITest {

    private DashboardUI ui;
    private final PrintStream stdout = System.out;
    private ByteArrayOutputStream outputStream;
    private ScrollManager scrollManager = new ScrollManager();
    private final User user = new User("testUser", "testPassword", "test@email.com", 123456789, "10", REGULAR, "mockSaltValue");
    private ScrollSeeker scrollSeeker = new ScrollSeeker(scrollManager, user);


    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        scrollSeeker = new ScrollSeeker(scrollManager, user);
        scrollManager = new ScrollManager();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testLogoutUI(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String input = "5\nyes\n";

        provideInput(input);

        String expectedOutput = "-------------- HOME PAGE --------------\nHello testUser! (REGULAR)\nWhat would you like to do today? (1 ~ 4)\n1. Manage your profile.\n2. Edit your Scrolls.\n" +
                "3. Remove your Scrolls.\n" +
                "4. Seek Scrolls.\n" +
                "5. Logout\nAre you sure you want to logout? (yes/no)\nLogout Successfully\n";

//         assertTrue(outputStream.toString().equals(expectedOutput));
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testProfileUI(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String input = "1\n5\n4\nyes\n";

        provideInput(input);

        String expectedOutput = "-------------- HOME PAGE --------------\n" +
                "Hello testUser! (REGULAR)\n" +
                "What would you like to do today? (1 ~ 4)\n" +
                "1. Manage your profile.\n" +
                "2. Edit your Scrolls.\n" +
                "3. Remove your Scrolls.\n" +
                "4. Seek Scrolls.\n" +
                "5. Logout\n" +
                "-------------- EDIT PROFILE --------------\n" +
                "1. Edit Username\n" +
                "2. Edit Password\n" +
                "3. Edit Email\n" +
                "4. Edit Phone\n" +
                "5. Return to Home\n" +
                "-------------- HOME PAGE --------------\n" +
                "Hello testUser! (REGULAR)\n" +
                "What would you like to do today? (1 ~ 4)\n" +
                "1. Manage your profile.\n" +
                "2. Edit your Scrolls.\n" +
                "3. Remove your Scrolls.\n" +
                "4. Seek Scrolls.\n" +
                "5. Logout\n" +
                "Are you sure you want to logout? (yes/no)\n" +
                "Invalid input.\n" +
                "-------------- HOME PAGE --------------\n" +
                "Hello testUser! (REGULAR)\n" +
                "What would you like to do today? (1 ~ 4)\n" +
                "1. Manage your profile.\n" +
                "2. Edit your Scrolls.\n" +
                "3. Remove your Scrolls.\n" +
                "4. Seek Scrolls.\n" +
                "5. Logout\n" +
                "Invalid input.\n" +
                "-------------- HOME PAGE --------------\n" +
                "Hello testUser! (REGULAR)\n" +
                "What would you like to do today? (1 ~ 4)\n" +
                "1. Manage your profile.\n" +
                "2. Edit your Scrolls.\n" +
                "3. Remove your Scrolls.\n" +
                "4. Seek Scrolls.\n" +
                "5. Logout\n";

        // assertTrue(outputStream.toString().equals(expectedOutput));
        assertEquals(expectedOutput, outputStream.toString());
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            ui = new DashboardUI(user, scrollManager, scrollSeeker);
            ui.displayMainUI();
        } finally {
            System.setIn(stdin);
        }
    }
}
