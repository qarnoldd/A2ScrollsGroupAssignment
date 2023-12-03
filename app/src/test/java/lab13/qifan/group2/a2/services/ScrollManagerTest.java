package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScrollManagerTest {

    private User testUser;
    private UserDatabase testDB;
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private ScrollDatabase scrollDatabase;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        testUser = new User("testU","testP","testE",0,"TestIDKey", UserType.ADMIN, "testS");
        testDB = new UserDatabase();
        scrollManager = new ScrollManager();
        scrollSeeker = new ScrollSeeker(scrollManager, testUser);
        scrollDatabase = new ScrollDatabase();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void getAllScrolls() {
        List<DigitalScroll> scrolls = scrollManager.getAllScrolls();
        assertNotNull(scrolls);
    }
    @Test
    void getUserScrolls() {
        List<DigitalScroll> userScrolls = scrollManager.getUserScrolls(testUser);
        assertNotNull(userScrolls);
    }

    @Test
    public void testRemoveScroll() {
        int ogSize = scrollDatabase.getArrayScrolls().size();
        DigitalScroll removeScroll = new DigitalScroll("removeScroll","removeScroll","removeScroll","TestIDKey",0);
        scrollDatabase.addScroll(removeScroll);
        provideInput("1\n");
        scrollManager.removeYourScroll(testUser);
        assertEquals(ogSize,scrollManager.getAllScrolls().size());
    }

    @Test
    void isUniqueName() {
        DigitalScroll testScroll = new DigitalScroll("testScrollId", "Test Scroll", "testFilePath", "testUserId", 0);
        scrollManager.addScroll(testScroll, testUser);
        assertFalse(scrollManager.isUniqueName("Test Scroll"));
    }

    @Test
    void isUniqueID() {
        DigitalScroll testScroll = new DigitalScroll("testScrollId", "Test Scroll", "testFilePath", "testUserId", 0);
        scrollManager.addScroll(testScroll, testUser);
        assertFalse(scrollManager.isUniqueID("testScrollId"));
    }

    @Test
    void checkIdExists() {
        DigitalScroll testScroll = new DigitalScroll("testScrollId", "Test Scroll", "testFilePath", "testUserId", 0);
        scrollManager.addScroll(testScroll, testUser);
        assertTrue(scrollManager.checkIdExists("testScrollId"));
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
}
