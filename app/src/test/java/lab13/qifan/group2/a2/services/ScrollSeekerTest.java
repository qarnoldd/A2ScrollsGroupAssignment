package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ScrollSeekerTest {

    private ScrollSeeker scrollSeeker;
    private ScrollManager scrollManager;
    private User testUser;
    private ScrollDatabase scrollDatabase;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        testUser = new User("testU", "testP", "testE", 0, "TestIDKey", UserType.ADMIN, "testS");
        scrollManager = new ScrollManager();
        scrollDatabase = new ScrollDatabase();
        scrollSeeker = new ScrollSeeker(scrollManager, testUser);
        outputStream = new ByteArrayOutputStream();
        scrollDatabase = new ScrollDatabase();
        System.setOut(new PrintStream(outputStream));
    }


     @Test
     public void testDownloadScroll() {

        String fileContent = "00010 100110";
        DigitalScroll mockScroll = new DigitalScroll("1", "Scroll10", "virtualLibrary/Scroll19.bin", "TestIDKey", 0);
        scrollDatabase.addScroll(mockScroll);
        scrollManager.getAllScrolls().add(mockScroll);
        createMockBinaryFile(mockScroll.getFilePath(), fileContent);
        String input = "Scroll10";
        provideInput(input+"\n");
        try {
        scrollSeeker.downloadScroll(testUser);
        File downloadedFile = new File("DownloadedScrolls" + File.separator + input + ".bin");
        assertTrue(downloadedFile.exists(), "Downloaded file should exist");
        } finally {
            scrollDatabase.deleteScroll(mockScroll);
            deleteMockBinaryFile(mockScroll.getFilePath());
            deleteMockDownloadedFile(input);
        }
    }

    private void createMockBinaryFile(String filePath, String content) {
        try {
            Files.write(Path.of(filePath), content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMockBinaryFile(String filePath) {
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMockDownloadedFile(String scrollName) {
        try {

            Files.deleteIfExists(Path.of("DownloadedScrolls" + File.separator + scrollName + ".bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    // @Test
    // public void testSearchScrolls() {
    //     // Your test logic for searching scrolls
    //     ArrayList<DigitalScroll> scrolls = new ArrayList<DigitalScroll>();
    //     DigitalScroll mockScroll1 = new DigitalScroll("1", "Scroll1", "virtualLibrary/testPath", "user", 0);
    //     DigitalScroll mockScroll2 = new DigitalScroll("2", "Scroll2", "virtualLibrary/testPath1", "user", 0);
    //     DigitalScroll mockScroll3 = new DigitalScroll("3", "Scroll3", "virtualLibrary/testPath2", "user1", 0);
    //     DigitalScroll mockScroll4 = new DigitalScroll("4", "Scroll4", "virtualLibrary/testPath3", "user1", 0);
    //     mockScroll4.setDate(LocalDate.of(2000, 12, 25));
    //     scrolls.add(mockScroll1);
    //     scrolls.add(mockScroll2);
    //     scrolls.add(mockScroll3);
    //     scrolls.add(mockScroll4);
    //     provideInput("1\nuser\n");
    //     scrollSeeker.searchScrolls(scrolls, testUser);
    //     String expected = "What would you like to perform the search on?\r\n" + //
    //             "1. Uploader ID\r\n" + //
    //             "2. Scroll ID\r\n" + //
    //             "3. Scroll Name\r\n" + //
    //             "4. Date Uploaded\r\n" + //
    //             "5. Quit\r\n" + //
    //             "";
        
    //     assertEquals(expected, outputStream.toString());
    // }

    @Test
    public void testPreviewScroll() {
        String fileContent = "Mock binary data";
        String lineSeparator = System.lineSeparator();
        DigitalScroll mockScroll = new DigitalScroll("1", "Scroll1", "virtualLibrary/testPath.bin", "user1", 0);
        createMockBinaryFile(mockScroll.getFilePath(), fileContent);
        scrollSeeker.previewScroll(mockScroll, testUser);
        assertEquals("-------------- SCROLL PREVIEW --------------" + lineSeparator + "01001101 01101111 01100011 01101011 00100000 01100010 01101001 01101110 01100001 01110010 01111001 00100000 01100100 01100001 01110100 01100001 " + lineSeparator + "--------------------------------------------" + lineSeparator + "" + lineSeparator, outputStream.toString());

        deleteMockBinaryFile(mockScroll.getFilePath());
    }

    @Test
    public void testFindScrollByName() {
        // Your test logic for finding scrolls by name
    }
}
