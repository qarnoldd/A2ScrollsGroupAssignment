package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.ui.AdminPanelUI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminPanelUITest {

    private User testUser;
    private UserDatabase testDB;
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private AdminPanelUI adminPanelUI;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        testUser = new User("testU","testP","testE",0,"TestIDKey", UserType.ADMIN, "testS");
        testDB = new UserDatabase();
        scrollManager = new ScrollManager();
        scrollSeeker = new ScrollSeeker(scrollManager, testUser);
        adminPanelUI = new AdminPanelUI(testUser, testDB,scrollManager,scrollSeeker);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testDisplayMainUI() {
        provideInput("-1\n1\n5\n7\n/b\n8\nyes\n");
        adminPanelUI.displayMainUI();
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "------------===# Admin Interface #===------------" + lineSeparator +
                                "Hello testU ADMIN!" + lineSeparator +
                                "What would you like to do today? (1 ~ 8)" + lineSeparator +
                                "1. Manage your profile." + lineSeparator +
                                "2. Edit your Scrolls." + lineSeparator +
                                "3. Remove your Scrolls." + lineSeparator +
                                "4. Seek Scrolls." + lineSeparator +
                                "5. Delete Users." + lineSeparator +
                                "6. Statistics." + lineSeparator +
                                "7. Make new users." + lineSeparator +
                                "8. Print all users." + lineSeparator +
                                "9. Logout" + lineSeparator;
        assertEquals(expectedOutput, outputStream.toString());
    } 

    @Test
    public void testAddUser() {
        provideInput("t\nt\nt\n1\n\n");
        adminPanelUI.addUser();
        User user = testDB.getUser("t");
        assertNotNull(user);
    }

     @Test
     public void testManageUser() {
         provideInput("t\nyes\n");
         adminPanelUI.manageUser();
         User user = testDB.getUser("t");
         assertEquals(null,user);
     }

    @Test
    public void testLogoutConfirmYes() {
        provideInput("yes\n");
        adminPanelUI.logoutConfirmUI();
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "Are you sure you want to logout? (yes/no)" + lineSeparator +
                                "Logout Successfully" + lineSeparator;
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testLogoutConfirmNo() {
        provideInput("no\n");
        adminPanelUI.logoutConfirmUI();
        String lineSeparator = System.lineSeparator();
        String expectedOutput = "Are you sure you want to logout? (yes/no)" + lineSeparator;
        assertEquals(expectedOutput, outputStream.toString());
    }

     @Test
     public void testScrollStatistics(){
        provideInput("3\n");

        List<DigitalScroll> scrolls = new ArrayList<>();
        scrolls.add(new DigitalScroll("0","test1","virtualLibrary/Test.bin","TestIDKey",1));
        scrolls.add(new DigitalScroll("0","test2","virtualLibrary/Test2.bin","TestIDKey",3));
        adminPanelUI.scrollStatistics(scrolls,2);

        String lineSeparator = System.lineSeparator();
        String expectedOutput = "-------------- SCROLL STATISTICS --------------" + lineSeparator +
                                "Scrolls available: 2" + lineSeparator +
                                "Scrolls uploaded: 2" + lineSeparator +
                                "------------------------------------------------" + lineSeparator +
                                "List of available scrolls:" + lineSeparator +
                                "1. test1 (ID: 0) DOWNLOAD COUNT: 1" + lineSeparator +
                                "2. test2 (ID: 0) DOWNLOAD COUNT: 3" + lineSeparator +
                                "You are now at page 1/1." + lineSeparator + lineSeparator +
                                "Options:" + lineSeparator +
                                "1. Next Page" + lineSeparator +
                                "2. Previous Page" + lineSeparator +
                                "3. Quit" + lineSeparator +
                                "Enter your choice:";

        assertEquals(expectedOutput, outputStream.toString().trim());
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
}
