package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuestUITest {

    private GuestUI guestUI;
    private ByteArrayOutputStream outputStream;

    private String lineSeparator;

    ScrollManager scrollManager = new ScrollManager();
    ScrollSeeker scrollSeeker = new ScrollSeeker(scrollManager, null);

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ScrollManager scrollManager = new ScrollManager();
        ScrollSeeker scrollSeeker = new ScrollSeeker(scrollManager, null);
        guestUI = new GuestUI(scrollManager, scrollSeeker);
        lineSeparator = System.lineSeparator();
    }

//    @Test
//    public void testSeekScrollsOption() {
//        String input = "1\n";
//
//        provideInput(input);
//
//        String expectedOutput = "-------------- HOME PAGE --------------\n" +
//                "Hello Guest User!\n" +
//                "What would you like to do today? (1 ~ 4)\n" +
//                "1. Seek Scrolls.\n" +
//                "2. Login as an User.\n" +
//                "3. Register New User.\n" +
//                "4. Exit.\n" +
//                "Are you sure you want to exit? (yes/no)\n" +
//                "Exited Successfully\n";
//
//        assertEquals(expectedOutput, outputStream.toString());
//    }

    @Test
    public void testLoginOption() {
        String input = "2\nq\n";

        provideInput(input);

        String expectedOutput = "-------------- HOME PAGE --------------" + lineSeparator +
                "Hello Guest User!" + lineSeparator +
                "What would you like to do today? (1 ~ 4)"+ lineSeparator +
                "1. Seek Scrolls." + lineSeparator +
                "2. Login as an User." + lineSeparator +
                "3. Register New User." + lineSeparator +
                "4. Exit." + lineSeparator +
                "Press q to exit the program." + lineSeparator +
                "Do you want to log in as a guest? (yes/no): " + lineSeparator +
                "-------------- LOGIN SCREEN --------------" + lineSeparator +
                "Username: "  + lineSeparator;

        assertEquals(expectedOutput, outputStream.toString());
    }

     @Test
     public void testRegistrationOption() {
         String input = "3\n";
         provideInput(input);
         String expectedOutput = "-------------- HOME PAGE --------------" + lineSeparator +
                 "Hello Guest User!" + lineSeparator +
                 "What would you like to do today? (1 ~ 4)" + lineSeparator +
                 "1. Seek Scrolls." + lineSeparator +
                 "2. Login as an User." + lineSeparator +
                 "3. Register New User." + lineSeparator +
                 "4. Exit." + lineSeparator +
                 "-------------- REGISTRATION SCREEN --------------" + lineSeparator +
                 "Username: ";

         assertEquals(expectedOutput, outputStream.toString());
     }

    @Test
    public void testExitOption_Yes() {
        String input = "4\nyes\n";

        provideInput(input);

        String expectedOutput = "-------------- HOME PAGE --------------" + lineSeparator +
                "Hello Guest User!" + lineSeparator +
                "What would you like to do today? (1 ~ 4)" + lineSeparator +
                "1. Seek Scrolls." + lineSeparator +
                "2. Login as an User." + lineSeparator +
                "3. Register New User." + lineSeparator +
                "4. Exit." + lineSeparator +
                "Are you sure you want to exit? (yes/no)" + lineSeparator +
                "Exited Successfully" + lineSeparator;

        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testExitOption_No() {
        String input = "4\nno\n";

        provideInput(input);

        String expectedOutput = "-------------- HOME PAGE --------------" + lineSeparator +
                "Hello Guest User!" + lineSeparator +
                "What would you like to do today? (1 ~ 4)" + lineSeparator +
                "1. Seek Scrolls." + lineSeparator +
                "2. Login as an User." + lineSeparator +
                "3. Register New User." + lineSeparator +
                "4. Exit." + lineSeparator +
                "Are you sure you want to exit? (yes/no)" + lineSeparator +
                "-------------- HOME PAGE --------------" + lineSeparator +
                "Hello Guest User!" + lineSeparator +
                "What would you like to do today? (1 ~ 4)" + lineSeparator +
                "1. Seek Scrolls." + lineSeparator +
                "2. Login as an User." + lineSeparator +
                "3. Register New User." + lineSeparator +
                "4. Exit." + lineSeparator;

        assertEquals(expectedOutput, outputStream.toString());
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            guestUI = new GuestUI(scrollManager, scrollSeeker);
            guestUI.displayMainUI();
        } finally {
            System.setIn(stdin);
        }
    }
}
